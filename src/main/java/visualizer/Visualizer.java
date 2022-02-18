package visualizer;


import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Point;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import converter.Score;
import custom_exceptions.TXMLException;
import models.Part;
import models.ScorePartwise;
import models.measure.Measure;
import models.measure.attributes.*;
import models.measure.barline.BarLine;
import models.measure.note.Note;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This Class is use for visualize musicXML file.
 *
 * @author Kuimou
 * */
public class Visualizer {
	// Note: A4 size: 2048px * 2929px
	private final int lineGap = 50; // px lineGap between staff.
	private final int measureGap = 200; //px Gap between measure.
	private final int measureBaseStart = 200;// where the first line of measure base start.
	private final int noteWidth = 50; //px, the width of a note element
	private final int clefWidth = 50; //px, the width of a clef element
	private final int timeWidth = 50; //px, the width of a time element
	private final int keyWidth = 50; //px, the width of a key element;
	private final int gapSize = 20; //px, the width between elements.
	private final int marginX = 50; // px, the width of margin.
	private final int marginY = 50; // px, the width of margin.
	private final int titleSpace = 200; // px, for title and author

	public String temp_dest = "resources/templeFile/tempSheet.pdf";
	private ScorePartwise score;
	private PdfCanvas canvas;
	private PdfDocument pdf;
	private int measureCounter = 0;
	private int durationCounter = 0; // count for distance for this note.
	private int lineCounter = 0;//which measure are we currently printing
	private int pageCounter = 0; // which page are we currently printing
	private int currentY = marginY;
	private int currentX = marginX + titleSpace;
	private Time time = new Time(4,4); // default time: 4/4
	private boolean shouldDrawTime = false;

	private Map<String,Integer> noteType2Int = new HashMap<>();

 	public Visualizer(Score score) throws TXMLException {
		initConverter();
		this.score = score.getModel();
		this.measureCounter = 0;
	}
	/**
	 * This method is going to init PDF file.
	 *
	 * */
	public void initPDF() throws FileNotFoundException {
		File file = new File(temp_dest);
		file.getParentFile().mkdir();
		this.pdf = new PdfDocument(new PdfWriter(temp_dest));
		// A4 size: 2048px * 2929px
		PageSize pageSize = PageSize.A4.rotate();
		PdfPage page = pdf.addNewPage(pageSize);

		canvas = new PdfCanvas(page);
	}
	/**
	 * This method is going to draw musicXML
	 * visualizer will create a PDF file.
	 * after drawing is finished, it will project PDF file into preview canvas
	 *
	 *
	 * */

	public PdfDocument draw() throws FileNotFoundException {
		initPDF();
		// Parts is collection of part
		drawParts(score.getParts());
		return pdf;
	}
	public void drawParts(List<Part> parts){
		//draw each part
		for (Part part:parts){
			drawPart(part);
		}
	}
	public void drawPart(Part part){
		//draw each measures
		// there will be additional metadata in the Part that we have to draw
		List<Measure> measures = part.getMeasures();
		//draw each measure
		for (Measure measure:measures){
			drawMeasure(measure);
		}
	}

	public void drawMeasure(Measure measure){
		// attribute contain metadatas for whole measure
		// we need time information to determine how long should we draw for the single Measure.
		drawMeasureBackground(measure);

		drawAttributes(measure.getAttributes());
		// draw Notes
		durationCounter = 0;//reset durationCounter
		drawNotes(measure.getNotesBeforeBackup());
		drawNotes(measure.getNotesAfterBackup());
		// draw Barlines
		drawBarlines(measure.getBarlines());
		measureCounter++;
	}
	public void drawMeasureBackground(Measure measure){
		//current measure count:
		// this.measureCounter;
		if (measure.getAttributes().getStaffDetails()!=null){
			drawModifiedBackground(measure);
		}else {
			drawDefaultBackground(measure);
		}

	}


	public void drawAttributes(Attributes attributes){
		drawClef(attributes.getClef());
		drawKeySignature(attributes.getKey());

		if (attributes.getTime()!=null){
			time = attributes.getTime();
			shouldDrawTime = true;
		}

		if (shouldDrawTime){
			drawTimeSignature(time);
			shouldDrawTime = false;
		}
	}
	public void drawNotes(List<Note> notes){
		for (Note note:notes){
			drawNote(note);
		}
	}
	public void drawBarlines(List<BarLine> barLines){
		for (BarLine barLine:barLines){
			drawBarline(barLine);
		}
	}
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

	public void drawNote(Note note){

		if (note.getChord()!=null){
			this.durationCounter += note.getDuration();
		}

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
	}

	/**
	 * draw the time signature.
	 *
	 * @param time the time signature of this measure.
	 * */

	private void drawTimeSignature(Time time){

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

	}

	private void drawDefaultBackground(Measure measure){
		int length = getMeasurePxLength(measure);

	}
	private void drawModifiedBackground(Measure measure){
		int length = getMeasurePxLength(measure);

	}

	private void switchLine(){
		currentX = marginX;
		lineCounter++;
	}
	private void switchPage(){
		currentX = marginX;
		lineCounter = 0;
		currentY = 0;
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
		noteType2Int.put("64th",128);
		noteType2Int.put("256th",256);
		noteType2Int.put("512th",512);
		noteType2Int.put("1024th",1024);
	}
	private int getMaxNoteFraction(Measure measure){
		int maxFraction = 1;
		for (Note note:measure.getNotesBeforeBackup()){
			maxFraction = Math.max(noteTypeToInt(note.getType()),maxFraction);
		}
		for (Note note:measure.getNotesAfterBackup()){
			maxFraction = Math.max(noteTypeToInt(note.getType()),maxFraction);
		}
		return maxFraction;
	}

	private int getNoteCount(Measure measure){
		return measure.getNotesAfterBackup().size()+measure.getNotesBeforeBackup().size();
	}

	private int getMeasurePxLength(Measure measure){
		//px length = time*minFraction*noteWidth+Notes*gapsize.
		int baseLength = getMaxNoteFraction(measure)*time.getBeats()*time.getBeatType()*noteWidth+getNoteCount(measure)*gapSize;
		int totalLength = baseLength;
		if (measure.getAttributes().getClef()!=null){
			totalLength+=clefWidth+gapSize;
		}
		if (shouldDrawTime){
			totalLength+=timeWidth+gapSize;
		}
		if (measure.getAttributes().getKey()!=null){
			totalLength+=keyWidth*measure.getAttributes().getKey().fifths+gapSize;
		}
		return totalLength;
	}



}
