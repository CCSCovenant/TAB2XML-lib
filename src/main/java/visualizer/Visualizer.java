package visualizer;


import com.itextpdf.io.image.ImageData;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Point;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import converter.Score;
import custom_exceptions.TXMLException;
import models.Part;
import models.ScorePartwise;
import models.measure.Measure;
import models.measure.attributes.*;
import models.measure.barline.BarLine;
import models.measure.note.Dot;
import models.measure.note.Note;
import models.measure.note.Notehead;
import models.measure.note.notations.Slur;
import models.measure.note.notations.Tied;
import models.measure.note.notations.technical.Technical;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * This Class is use for visualize musicXML file.
 *
 * @author Kuimou
 * */
public class Visualizer {
	// Note: A4 size: 597.6 unit * 842.4 unit
	private final int measureGap = 80; //px Gap between measure.
	private final int noteWidth = 8; //px, the width of a note element
	private final int clefWidth = 10; //px, the width of a clef element
	private final int timeWidth = 10; //px, the width of a time element
	private final int keyWidth = 10; //px, the width of a key element;
	private final int stepSize = 4; //px, the width between steps.
	private final int marginX = 10; // px, the width of margin.
	private final int marginY = 10; // px, the width of margin.
	private final int titleSpace = 150; // px, for title and author
	private final int A4Width = 597;
	private final int A4Height =  842;
	private final int eighthGap = noteWidth/2;
	private final int defaultShift = 20; // where we should put next note.
	private final int bendShift = 10;
	private final String temp_dest = "tmp.pdf";

	private ScorePartwise score;
	private PdfCanvas canvas;
	private PdfDocument pdf;
	private int measureCounter = 1;
	private int lineCounter = 0;//which measure are we currently printing
	private int pageCounter = 0; // which page are we currently printing
	private double currentY = marginY + titleSpace; // the position of center C in current line.
	private double currentX = marginX;
	private  double planShift = 0; // where we should put next note.
	private double measureStart = 0;
	private double measureEnd = 0;

	private Time time = new Time(4,4); // default time: 4/4
	private boolean shouldDrawTime = false;
	private StaffDetails staffDetails;
	private Clef clef;
	private EighthFlag eighthFlag = new EighthFlag(currentX);
	private ImageResourceHandler imageResourceHandler = ImageResourceHandler.getInstance();
	private Map<String,Integer> noteType2Int = new HashMap<>();
	private HashSet<Integer> relatives = new HashSet<>();

	private Queue<TieElement> TieElements = new LinkedList<>() ;
	private Map<Integer,TieElement> slurElements = new HashMap<>();
 	public Visualizer(Score score) throws TXMLException {
		initConverter();
		this.score = score.getModel();
		this.measureCounter = 1;
		// create init staff for the drum
		List<StaffTuning> staffs = new ArrayList<>();
		staffs.add(new StaffTuning(1,"E",4));
		staffs.add(new StaffTuning(1,"G",4));
		staffs.add(new StaffTuning(1,"B",4));
		staffs.add(new StaffTuning(1,"D",5));
		staffs.add(new StaffTuning(1,"F",5));

		this.staffDetails = new StaffDetails(5,staffs);
	}
	/**
	 * This method is going to init PDF file.
	 *
	 * */
	public void initPDF(File file) throws FileNotFoundException {

		this.pdf = new PdfDocument(new PdfWriter(file));
		// A4 size: 2048px * 2929px
		PageSize pageSize = PageSize.A4;
		PdfPage page = pdf.addNewPage(pageSize);
		canvas = new PdfCanvas(page);
	}

	/**
	 * This method is going to draw musicXML
	 * visualizer will create a PDF file.
	 * after drawing is finished, it will project PDF file into preview canvas
	 *
	 * @param file the temple file that used to draw and preview.
	 * */

	public PdfDocument draw(File file) throws FileNotFoundException {
		initPDF(file);
		// Parts is collection of part
		drawParts(score.getParts());
		return pdf;
	}
	/**
	 * This method is going to draw "Parts" element in the musicXML
	 * each musicXML file is made with different parts.
	 * */
	public void drawParts(List<Part> parts){
		//draw each part
		for (Part part:parts){
			drawPart(part);
		}
	}
	/**
	 * This method is going to draw "Part" element in the musicXML
	 * a Part contain measures
	 * */
	public void drawPart(Part part){
		//draw each measures
		// there will be additional metadata in the Part that we have to draw
		List<Measure> measures = part.getMeasures();
		//draw each measure
		for (Measure measure:measures){
			drawMeasure(measure);
		}
	}

	/**
	 * This method is going to draw "Measure" element in the musicXML
	 * a Measure is collection of notes
	 * */

	public void drawMeasure(Measure measure){

		//new line check
		if (getMeasureTotalLength(measure)+currentX>A4Width-marginX){
			switchLine();
		}
		if (measure.getAttributes().getStaffDetails()!=null){
			List<StaffTuning> staffTunings = new ArrayList<>();
			// CDEFGAB
			char baseS = 'E';
			int baseO = 4;
			for (StaffTuning staffTuning:measure.getAttributes().staffDetails.getStaffTuning()){
				staffTunings.add(new StaffTuning(staffTuning.line,baseS+"",baseO));
				baseS += 3;
				if (baseS=='H'){
					baseS = 'A';
				}
				if (baseS=='I'){
					baseS = 'B';
				}
				if (baseS=='J'){
					baseS = 'C';
				}
				if (baseS == 'C'||baseS=='D'||baseS=='E'){
					baseO++;
				}
			}
			staffDetails = new StaffDetails(staffTunings.size(),staffTunings);
		}
		// attribute contain metadatas for whole measure
		// we need time information to determine how long should we draw for the single Measure.
		measureStart = currentX;
		drawAttributes(measure.getAttributes());
		// draw Notes
		drawNotes(measure.getNotesBeforeBackup());
		//drawNotes(measure.getNotesAfterBackup());

		drawEighthFlag();
		currentX += planShift;
		planShift = 0;
		eighthFlag = new EighthFlag(currentX+noteWidth);

		measureEnd = currentX;

		// draw Barlines

		drawBarlines(measure.getBarlines());
		measureCounter++;
	}
	/**
	 * This method is going to draw "Attributes"  in the measure
	 * include clef, key and time
	 * */

	public void drawAttributes(Attributes attributes){
		drawBackground(noteWidth*2);//empty space at begin
		currentX += planShift;
		planShift = 0;
		if (attributes.getClef()!=null){
			drawClef(attributes.getClef());
			clef = attributes.getClef();
			currentX += planShift;
			planShift = 0;
		}
		drawKeySignature(attributes.getKey());

		if (attributes.getTime()!=null){
			time = attributes.getTime();
			shouldDrawTime = true;
			currentX += planShift;
			planShift = 0;
		}

		if (shouldDrawTime){
			drawTimeSignature(time);
			shouldDrawTime = false;
			currentX += planShift;
			planShift = 0;
		}
	}

	/**
	 * This method is going to draw Notes
	 * */

	public void drawNotes(List<Note> notes){
		if (notes!=null){
			for (Note note:notes){
				drawNote(note);
			}
		}
	}

	/**
	 * This method is going to draw barlines
	 * */

	public void drawBarlines(List<BarLine> barLines){
		//default measure barlines
		double maxY = Integer.MIN_VALUE;
		double minY = Integer.MAX_VALUE;
		for (StaffTuning staffTuning:staffDetails.getStaffTuning()){
			int relative = getRelative(staffTuning.tuningStep,staffTuning.tuningOctave);

			maxY = Math.max(currentY+stepSize*relative,maxY);
			minY = Math.min(currentY+stepSize*relative,minY);
		}
		//left
		Point startL = new Point(measureStart,A4Height-maxY);
		Point endL = new Point(measureStart,A4Height-minY);
		drawLine(startL,endL);
		//right
		Point startR = new Point(measureEnd,A4Height-maxY);
		Point endR = new Point(measureEnd,A4Height-minY);
		drawLine(startR,endR);
		drawLine(startR,endR);
		//more barline
		if (barLines!=null){
			for (BarLine barLine:barLines){
				drawBarline(barLine);
			}
		}
		int numberFix = 5;
		addTextAt(startL.x,startL.y-numberFix,noteWidth*4,noteWidth,new Paragraph(measureCounter+""));

	}
	// we only have two kind of barline left and right
	public void drawBarline(BarLine barLine){

	}
	/*
	 what inside of note:

	 Grace grace; // need to draw slash. if it appeared , have to be smaller
	 Chord chord; // if this notation appear, don't move forward.

	 group of elemens that only can show once.
	 Rest rest;
	 Pitch pitch;
	 Unpitched unpitched;

	 Integer duration;
	 Integer voice;

	 String type; // determine type of note. only 16th and eighth.
	 List<Dot> dots;// need to handle

	 TimeModification timemodification;

	 String stem; // always up if there are one.
	 Notehead notehead;// three type of Notehead: x diamond and normal
	 // reference https://www.w3.org/2021/06/musicxml40/musicxml-reference/data-types/notehead-value/


	 Beam beam; //useless, ignore. maybe need to use it in the future

	 Notations notations; //need to handle
	 */

	private void drawNote(Note note){
		// handle chord
		if (note.getChord()==null){
			// if there is no chord. move the next note
			drawEighthFlag();
			relatives = new HashSet<>();
			currentX += planShift;
			planShift = 0;
			eighthFlag = new EighthFlag(currentX+noteWidth);
		}else {

		}

		if (clef.getSign().equals("TAB")){
			if (note.getChord()==null){
				if (note.getNotations().getTechnical().getBend()!=null){
					drawBackground(defaultShift+bendShift+noteWidth);
				}else {
					drawBackground(defaultShift+noteWidth);
				}
			}

			drawTechnical(note);
			// tab note only need draw technical.
		}else if (clef.getSign().equals("percussion")){
			drawBackground(defaultShift+noteWidth);
			if (note.getRest()==null){
				drawNoteHead(note);
				if (!note.getType().equals("whole")){
					drawNoteStem(note);
				}
			}else {
				if (note.getType()!=null) {
					ImageData image = imageResourceHandler.getImage(note.getType() + "_rest");
					double x = currentX;
					//offset by one because of image height
					int relative = getRelative("G", 4);
					double y = A4Height - (currentY + stepSize * (relative + 1));
					if (note.getType().equals("half")||note.getType().equals("whole")){
						drawImageAt(x-noteWidth,y+stepSize,noteWidth*2,noteWidth,image);
					}else {
						drawImageAt(x,y,noteWidth,noteWidth*2,image);
					}
					drawDots(note,currentX,y+stepSize*2);

				}
			}
		}
	}
	private void drawNoteHead(Note note){
		//System.out.println("drawing");
		Notehead notehead = note.getNotehead();
		String type = "";
		if (note.getType()!=null){
			if (note.getType().equals("whole")){
				type = "whole_";
			}else if (note.getType().equals("half")){
				type = "half_";
			}else {

			}
		}
		ImageData image;
		if (notehead!=null&&notehead.getType()!=null){
			image = imageResourceHandler.getImage(type+notehead.getType());
		}else {
			image = imageResourceHandler.getImage(type+"normal");
		}

		if (image!=null){
			int relative = 0;
			int referenceRelative = 0;

				referenceRelative = getRelative(staffDetails.staffTuning.get(0).tuningStep,staffDetails.staffTuning.get(0).tuningOctave);
			if (note.getUnpitched()!=null) {
				relative = getRelative(note.getUnpitched().getDisplayStep(), note.getUnpitched().getDisplayOctave());
			}else if (note.getPitch()!=null){
				relative = getRelative(note.getPitch().getStep(), note.getPitch().getOctave());
			}

			boolean shouldFlip = false;
			if (relatives.contains(relative + 1) || relatives.contains(relative - 1) || relatives.contains(relative)) {
				shouldFlip = true;
			}

			relatives.add(relative);
			double x = currentX;
			//offset by one because of image height
			double y = A4Height-(currentY+stepSize*(relative+1));

			double accW = noteWidth;

			if (note.getGrace()!=null){
				accW = noteWidth*0.6;
				eighthFlag.x = currentX+accW;
				eighthFlag.isGrace = true;
			}
			if (shouldFlip){
				drawImageAt(x+noteWidth*2,y,-accW,accW,image);
				if (referenceRelative%2==relative%2){
					drawLine(new Point(x+noteWidth,y+stepSize),new Point(x+noteWidth,y+stepSize));
				}
				drawDots(note,currentX+noteWidth,y+stepSize);
			}else {
				drawImageAt(x,y,accW,accW,image);
				if (referenceRelative%2==relative%2){
					drawLine(new Point(x,y+stepSize),new Point(x+noteWidth,y+stepSize));
				}
				drawDots(note,currentX,y+stepSize);
			}


			if (note.getNotations()!=null&&note.getNotations().getTieds()!=null){
				List<Tied> tiedList = note.getNotations().getTieds();
				for (Tied tied:tiedList){
					if (tied.getType().equals("start")){
						TieElements.add(new TieElement(x,y));
					}else if (tied.getType().equals("stop")){
						TieElement tieElement = TieElements.poll();
						tieElement.x2 = x;
						tieElement.y2 = y;
						if (tieElement!=null){
							drawTied(tieElement);
						}
					}
				}
			}

			if (note.getNotations()!=null&&note.getNotations().getSlurs()!=null){
				List<Slur> slurs  = note.getNotations().getSlurs();
				for (Slur slur:slurs){
					if (slur.getType().equals("start")){
						slurElements.put(slur.getNumber(),new TieElement(x,y));
					}else if (slur.getType().equals("stop")){
						TieElement tieElement = slurElements.get(slur.getNumber());
						tieElement.x2 = x;
						tieElement.y2 = y;
						if (tieElement!=null){
							drawTied(tieElement);
						}
					}
				}
			}
			//System.out.println("drawing at"+ currentX);
		}else {
			//System.out.println("fail to draw");
		}


	}
	private void drawTied(TieElement tieElement){

		double shift = stepSize;
		double x1 = tieElement.x1+noteWidth/2;
		double x2 = tieElement.x2+noteWidth/2;
		double y1 = tieElement.y1+stepSize;
		double y2 = tieElement.y2-stepSize;
		double start = 220;
		double extent = 100;
		if (tieElement.placement.equals("above")){
			start = 40;
		}
		canvas.arc(x1,y1,x2,y2,start,extent);

	}
	private void drawDots(Note note,double x,double y){
		if (note.getDots()!=null){
			double x_dot = x+noteWidth;
			double dot_shift = 4;
			for (Dot dot:note.getDots()){
				x_dot += dot_shift;
				drawDotAt(x_dot,y);
			}
		}
	}
	private void drawDotAt(double x,double y){
		canvas.circle(x,y,1.5);
		canvas.fill();


	}
	private void drawNoteStem(Note note){
		int xOffset = 0;
		if (note.getNotehead()!=null){
			if (note.getNotehead().getType()!=null){
				if (note.getNotehead().getType().equals("x")){
					xOffset = 1;
					//System.out.println("offseted");
				}
			}
		}
		int relative = 0;
		if (note.getUnpitched()!=null) {
			relative = getRelative(note.getUnpitched().getDisplayStep(), note.getUnpitched().getDisplayOctave());
		}else if (note.getPitch()!=null){
			relative = getRelative(note.getPitch().getStep(), note.getPitch().getOctave());
		}
		if (note.getGrace()!=null){
			eighthFlag.miny = Math.min(currentY+stepSize*(relative-4-xOffset),eighthFlag.miny);
		}else {
			eighthFlag.miny = Math.min(currentY+stepSize*(relative-6-xOffset),eighthFlag.miny);
		}
		eighthFlag.maxy = Math.max(currentY+stepSize*(relative-xOffset),eighthFlag.maxy);
		eighthFlag.type = noteTypeToInt(note.getType());
		//we assume every stem is up currently.
	}
	private void drawEighthFlag(){
		if (eighthFlag.type<2){
			return;
		}else {
			//System.out.println("Drawing");
			Point start = new Point(eighthFlag.x,A4Height-eighthFlag.miny);
			Point end = new Point(eighthFlag.x,A4Height-eighthFlag.maxy);

			drawLine(start,end);

			ImageData image = imageResourceHandler.getImage("eighthFlag");
			int postCounter = 0;
			for (int i = eighthFlag.type;i>=8;i/=2){

				double accW = noteWidth;
				if (eighthFlag.isGrace){
					accW*=0.6;
				}

				double x = eighthFlag.x;
				//offset by one because of image height
				double y = A4Height-(eighthFlag.miny+postCounter*eighthGap+accW*2);

				drawImageAt(x,y,accW/1.5,accW*2,image);
				postCounter++;
			}
		}
	}

	private void drawTechnical(Note note){
		if (note.getNotations()!=null&&note.getNotations().getTechnical()!=null){
			Technical technical = note.getNotations().getTechnical();
			int string = technical.getString();
			int fret = technical.getFret();

			StaffTuning staffTuning = staffDetails.staffTuning.get(staffDetails.staffTuning.size()-string);
			int relative = getRelative(staffTuning.tuningStep,staffTuning.tuningOctave);

			double x = currentX;
			//offset by one because of text height
			double y = A4Height-(currentY+stepSize*(relative)-1.5);

			addTextAt(x,y,12,4,new Paragraph(fret+"").setBackgroundColor(new DeviceRgb(255,255,255)).setFontSize(9).setTextAlignment(TextAlignment.CENTER));
			if (note.getNotations().getTechnical().getBend()!=null){
				drawBend(x+10,y,note.getNotations().getTechnical().getBend().getBendAlter());
			}

			if (note.getNotations()!=null&&note.getNotations().getSlurs()!=null){
				List<Slur> slurs  = note.getNotations().getSlurs();
				for (Slur slur:slurs){
					if (slur.getType().equals("start")){
						slurElements.put(slur.getNumber(),new TieElement(x,y));
						if (slur.getPlacement()!=null){
							slurElements.get(slur.getNumber()).placement = slur.getPlacement();
						}
					}else if (slur.getType().equals("stop")){
						TieElement tieElement = slurElements.get(slur.getNumber());
						tieElement.x2 = x;
						tieElement.y2 = y;

						if (slur.getPlacement()!=null){
							tieElement.placement = slur.getType();
						}
						if (tieElement!=null){
							drawTied(tieElement);
						}
					}
				}
			}
		}
	}
	private void drawBend(double x,double y,double bendAlter){
		StaffTuning staffTuning = staffDetails.staffTuning.get(staffDetails.staffTuning.size()-1);
		int relative = getRelative(staffTuning.tuningStep,staffTuning.tuningOctave);

		double topY = A4Height-(currentY+stepSize*(relative-1));

		double x1 = x-bendShift;
		double x2 = x+bendShift;
		double y1 = y;
		double y2 = y+2*(topY-y);
		canvas.arc(x1,y1,x2,y2,-90,90);

		ImageData imageData = imageResourceHandler.getImage("arrow");
		drawImageAt(x2-2.5,y+(topY-y),5,5,imageData);

		if (bendAlter==2.0){
			addTextAt(x2-5,y+(topY-y)+10,15,8,new Paragraph("full").setFontSize(8));
		}else {
			addTextAt(x2-5,y+(topY-y)+10,15,8,new Paragraph((int)bendAlter+"/2").setFontSize(8));
		}
	}
	private void addTextAt(double x,double y,double w,double h,Paragraph text){
		int marginFix = 7;
		Rectangle rectangle = new Rectangle((float) x,(float)(y-h+marginFix),(float) w,(float) h);
		//debug
		//canvas.rectangle(rectangle);
		//canvas.stroke();
		//
		Canvas localCanvas = new Canvas(canvas,rectangle);
		localCanvas.add(text);
	}
	/**
	 * draw a line from start point to end point
	 *
	 * @param start start point
	 * @param end end point
	 * */
	private void drawLine(Point start,Point end){
		canvas.moveTo(start.x,start.y);
		canvas.lineTo(end.x,end.y);
		canvas.closePathStroke();
	}
	private void drawImageAt(double x,double y,double w,double h,ImageData imageData){
		AffineTransform at = AffineTransform.getTranslateInstance(x, y);
		at.concatenate(AffineTransform.getScaleInstance(w, h));
		float[] m = new float[6];
		at.getMatrix(m);
		canvas.addImageWithTransformationMatrix(imageData,m[0], m[1], m[2], m[3], m[4], m[5]);
	}
	/**
	 * draw the time signature.
	 *
	 * @param t the time signature of this measure.
	 * */
	private void drawTimeSignature(Time t){


			StaffTuning staffTuning = staffDetails.staffTuning.get(0);

			int relative = getRelative(staffTuning.tuningStep,staffTuning.tuningOctave);

			double x = currentX;
			double y = A4Height-(currentY+stepSize*(relative-4));
			double y2 = A4Height-(currentY+stepSize*(relative-8));

			double w1 = stepSize*4*Math.max(1,(int)Math.log10(t.getBeats())+1);
			double w2 = stepSize*4*Math.max(1,(int)Math.log10(t.getBeatType())+1);

			addTextAt(x,y,w1,stepSize*4,new Paragraph(t.getBeats()+"").setFontSize(21).setBold());
			addTextAt(x,y2,w2,stepSize*4,new Paragraph(t.getBeatType()+"").setFontSize(21).setBold());
			//System.out.println(t.getBeats()+" "+t.getBeatType());
			drawBackground(defaultShift+Math.max(w1,w2));

	}
	/**
	 * draw the key signature.
	 *
	 * @param key the key signature of this measure
	 * */
	private void drawKeySignature(Key key){

	}
	/**
	 * draw the clef
	 *
	 * @param clef the key signature of this measure
	 * */

	private void drawClef(Clef clef){
		ImageData imageData = imageResourceHandler.getImage(clef.getSign());
		StaffTuning staffTuning = staffDetails.staffTuning.get(0);
		StaffTuning staffTuning2 = staffDetails.staffTuning.get(staffDetails.staffTuning.size()-1);

		int relative = getRelative(staffTuning.tuningStep,staffTuning.tuningOctave);
		int relative2 = getRelative(staffTuning2.tuningStep,staffTuning2.tuningOctave);

		double x = currentX;
		double y = A4Height-(currentY+stepSize*relative);
		double h = stepSize*(relative-relative2);
		double w = h/2;

		drawImageAt(x,y,w,h,imageData);
		drawBackground(defaultShift+w);


	}

	private void drawBackground(double length){
		planShift = length;
		for (StaffTuning staffTuning:staffDetails.staffTuning){
			int relative = getRelative(staffTuning.tuningStep,staffTuning.tuningOctave);

			Point start = new Point(currentX,A4Height-(currentY+stepSize*relative));
			Point end = new Point(currentX+length,A4Height-(currentY+stepSize*relative));
			drawLine(start,end);
		}
	}

	/**
	 * return the relative location relative to center c
	 *
	 *
	 * */
	private int getRelative(String step,int octave){
		//center C is the baseline.
		int centerOctave = 3;
		char stepAdjusted = step.charAt(0);
		char centerStep = 'C';
		if (stepAdjusted<'C'){
			if (stepAdjusted=='A'){
				stepAdjusted = 'H';
			}else if (stepAdjusted=='B'){
				stepAdjusted = 'I';
			}
		}
		return (centerStep-stepAdjusted)-(7*(octave-centerOctave));
	}
	private void switchLine(){
		if (currentY+measureGap+marginY>A4Height){
			switchPage();
		}else {
			currentX = marginX;
			currentY += measureGap;
			lineCounter++;
		}
	}
	private void switchPage(){
		currentX = marginX;
		lineCounter = 0;
		currentY = marginY+titleSpace;
		PageSize pageSize = PageSize.A4;
		PdfPage page = pdf.addNewPage(pageSize);
		canvas = new PdfCanvas(page);
		pageCounter ++;
	}
	/**
	 * covert Note type string into Fraction number.
	 * e.g whole number = 1
	 *     half = 2.
	 * if String is not a noteType, return -1.
	 * @param noteType note type
	 * @return integer fraction of the note.
	 * */
	private int noteTypeToInt(String noteType){
		if (noteType2Int.containsKey(noteType)){
			return noteType2Int.get(noteType);
		}
		return -1;
	}
	private void initConverter(){
		noteType2Int.put("whole",1);
		noteType2Int.put("half",2);
		noteType2Int.put("quarter",4);
		noteType2Int.put("eighth",8);
		noteType2Int.put("16th",16);
		noteType2Int.put("32nd",32);
		noteType2Int.put("64th",64);
		noteType2Int.put("128th",128);
		noteType2Int.put("256th",256);
		noteType2Int.put("512th",512);
		noteType2Int.put("1024th",1024);
	}
	private int getMeasureLength(Measure measure){
		int length = 0;
		if (measure.getNotesBeforeBackup()!=null){
			for (Note note:measure.getNotesBeforeBackup()){
				if (note.getNotations()!=null) {
					if (note.getNotations().getTechnical()!= null){
						if (note.getNotations().getTechnical().getBend()!=null){
							length += noteWidth + defaultShift+bendShift;
						}else {
							length += noteWidth+defaultShift;
						}
					}else {
						length += noteWidth+defaultShift;
					}
				} else {
					length += noteWidth+defaultShift;
				}
			}
		}
		//we don't need noteafter backup now
		/*
		for (Note note:measure.getNotesAfterBackup()){
			if (note.getNotations().getTechnical().getBend()==null){
				length += noteWidth+defaultShift;
			}else {
				length += noteWidth+defaultShift+bendShift;
			}		}

		*/
		return length;
	}

	private int getMeasureTotalLength(Measure measure){
		int totalLength = 0;

		if (measure.getAttributes().getClef()!=null){
			totalLength+=clefWidth+defaultShift;
		}
		if (shouldDrawTime){
			totalLength+=timeWidth+defaultShift;
		}
		if (measure.getAttributes().getKey()!=null){
			totalLength+=keyWidth*measure.getAttributes().getKey().fifths+defaultShift;
		}

		return totalLength+getMeasureLength(measure);
	}
}
