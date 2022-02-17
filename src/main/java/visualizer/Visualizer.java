package visualizer;


import com.itextpdf.io.image.ImageData;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Point;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import converter.Score;
import custom_exceptions.TXMLException;
import javafx.scene.canvas.Canvas;
import models.Part;
import models.ScorePartwise;
import models.measure.Measure;
import models.measure.attributes.Attributes;
import models.measure.attributes.Clef;
import models.measure.attributes.Key;
import models.measure.attributes.Time;
import models.measure.barline.BarLine;
import models.measure.note.Note;
import models.part_list.PartList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
/**
 * This Class is use for visualize musicXML file.
 *
 * @author Kuimou
 * */
public class Visualizer {
	public String temp_dest = "resources/templeFile/tempSheet.pdf";
	private ScorePartwise score;
	private PdfCanvas canvas;
	private PdfDocument pdf;
	private int measureCounter;
	private int noteCounter; // count for distance for this note.

	// Note: A4 size: 2048px * 2929px
	private final int lineGap = 50; // px lineGap between staff.
	private final int measureGap = 200; //px Gap between measure.

	public Visualizer(Score score) throws TXMLException {
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
		drawMeasureBackground(measure.getAttributes().getTime());

		drawAttributes(measure.getAttributes());
		// draw Notes
		noteCounter = 0;//reset noteCounter
		drawNotes(measure.getNotesBeforeBackup());
		drawNotes(measure.getNotesAfterBackup());
		// draw Barlines
		drawBarlines(measure.getBarlines());
		measureCounter++;
	}
	public void drawMeasureBackground(Time time){
		//current measure count:
		// this.measureCounter;
		int measureSize = time.getBeats();
		int unitLength = time.getBeatType();

	}


	public void drawAttributes(Attributes attributes){
		drawClef(attributes.getClef());
		drawKeySignature(attributes.getKey());
		drawTimeSignature(attributes.getTime());
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
	/**
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
	 * */
	public void drawNote(Note note){

		if (note.getChord()!=null){
			this.noteCounter++;
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
}
