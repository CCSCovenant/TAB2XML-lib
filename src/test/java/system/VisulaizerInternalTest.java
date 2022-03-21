package system;

import GUI.PreviewConfig;
import converter.Score;
import custom_exceptions.TXMLException;
import models.measure.Measure;
import models.measure.attributes.*;
import models.measure.note.Note;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import visualizer.Visualizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

class VisulaizerInternalTest {
	public static PreviewConfig c = PreviewConfig.getInstance();
	@Test
	void getRelativeTest() throws TXMLException {
		String[] steps = {"A","B","C","D","E","F","G","A","B","C","D","E","F","G","A","B"};
		int[] oct = {2,2,3,3,3,3,3,3,3,4,4,4,4,4,4,4};
		int[] relative = {2,1,0,-1,-2,-3,-4,-5,-6,-7,-8,-9,-10,-11,-12,-13};

		Visualizer visualizer = new Visualizer(new Score(""));
		for (int i=0;i<steps.length;i++){
			Assertions.assertEquals(relative[i],visualizer.getRelative(steps[i],oct[i]));
		}

	}
	@Test
	void getMeasureLengthTest() throws TXMLException {
		Measure measure = new Measure();
		List<Note> notes = new ArrayList<>();

		notes.add(new Note());
		measure.setNotesBeforeBackup(notes);

		Visualizer visualizer = new Visualizer(new Score(""));
		Assertions.assertEquals(visualizer.getMeasureLength(measure),visualizer.defaultShift+visualizer.noteWidth);

	}
	@Test
	void initPDFTest() throws TXMLException, FileNotFoundException {
		File file = new File("./tmp.pdf");
		Visualizer visualizer = new Visualizer(new Score(""));
		visualizer.initPDF(file);
	}
	@Test
	void MeasureAttributesTest() throws FileNotFoundException, TXMLException {
		File file = new File("./tmp.pdf");
		Visualizer visualizer = new Visualizer(new Score(""));
		visualizer.initPDF(file);

		Measure measure = new Measure();
		Attributes attributes = new Attributes();
		Time time = new Time(8,4);
		Clef clef = new Clef("TAB", 5);
		attributes.setTime(time);
		attributes.setClef(clef);

		List<StaffTuning> staffs = new ArrayList<>();
		staffs.add(new StaffTuning(1,"E",4));
		staffs.add(new StaffTuning(2,"G",4));
		staffs.add(new StaffTuning(3,"B",4));
		staffs.add(new StaffTuning(4,"D",5));
		staffs.add(new StaffTuning(5,"F",5));

		StaffDetails staffDetails = new StaffDetails(5,staffs);

		attributes.setStaffDetails(staffDetails);
		measure.setAttributes(attributes);
		visualizer.drawMeasure(measure);
		Assertions.assertEquals(visualizer.time,time);
		Assertions.assertEquals(visualizer.clef,clef);

		List<StaffTuning> staffs2 = visualizer.staffDetails.getStaffTuning();

		Assertions.assertEquals(staffs2.size(),staffs.size());
	}
	@Test
	void drawNullNoteTest() throws FileNotFoundException, TXMLException{
		File file = new File("./tmp.pdf");
		Visualizer visualizer = new Visualizer(new Score(""));
		visualizer.initPDF(file);

		visualizer.drawNotes(null);
	}
	@Test
	void switchLineTest() throws FileNotFoundException, TXMLException {
		File file = new File("./tmp.pdf");
		Visualizer visualizer = new Visualizer(new Score(""));
		visualizer.initPDF(file);

		visualizer.switchLine();
		Assertions.assertEquals(visualizer.currentY,visualizer.marginY+visualizer.titleSpace+c.getIntConfig("measureGap"));
	}
	@Test
	void switchPageTest() throws FileNotFoundException, TXMLException {
		File file = new File("./tmp.pdf");
		Visualizer visualizer = new Visualizer(new Score(""));
		visualizer.initPDF(file);

		visualizer.switchPage();
		Assertions.assertEquals(visualizer.currentX,visualizer.marginX);
		Assertions.assertEquals(visualizer.currentY,visualizer.marginY+visualizer.titleSpace);
	}
}
