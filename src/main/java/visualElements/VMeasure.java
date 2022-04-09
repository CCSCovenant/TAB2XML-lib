package visualElements;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import models.measure.Measure;
import models.measure.attributes.Time;
import models.measure.barline.BarLine;
import models.measure.note.Note;
import visualElements.Notations.VDrumGNotation;
import visualElements.Notations.VGNotation;
import visualElements.Notations.VGuitarGNotation;

import java.util.ArrayList;
import java.util.List;

public class VMeasure extends VElement{
	private int number;
	private List<VNote> Notes;
	private List<VGNotation> Notations = new ArrayList<>();
	private List<VSign> Signs = new ArrayList<>();
	private List<Line> staffLines = new ArrayList<>();
	private List<VBarline> barlines = new ArrayList<>();
	private List<VNoteHead> tieNoteHead = new ArrayList<>();
	private List<VNoteHead> slurNoteHead = new ArrayList<>();
	VLine line;
	Rectangle background = new Rectangle();
	String instrument = "";
	double gapCount = 0;
	boolean showClef = false;
	public VMeasure(Measure measure,String instrument,List<Integer> staffInfo){
		initConfig();
		group.getChildren().add(background);
		number = measure.getNumber();
		if (measure.getAttributes().getTime()!=null){
			Time time = measure.getAttributes().getTime();
			VTime vTime = new VTime(time.getBeats(),time.getBeatType());
			Signs.add(vTime);
			group.getChildren().add(vTime.getShapeGroups());
		}
		this.instrument = instrument;
		int i = 0;
		Notes = new ArrayList<>();
		initStaffLines(staffInfo);
		initBarlines(measure.getBarlines());
		if (measure.getNotesBeforeBackup()!=null){
			VNote vNote = null;
			boolean hasKeyNote = true;
			for (Note note:measure.getNotesBeforeBackup()){
				if (note.getChord()==null){
						if (vNote!=null){
							Notes.add(vNote);
						}
						vNote = new VNote(i,this);
						i++;
						addNoteHead(note,vNote);
					}else {
						addNoteHead(note,vNote);
					}
			}
			if (vNote!=null){
				Notes.add(vNote);
			}
		}

		initNoteGroups();
		if (instrument.equals("TAB")){
			initGuitarNotations();
		}else {
			initDrumNotations();
		}
	}
	//  N / G G N C / N
	public void initConfig(){
		initConfigElement("MinNoteDistance",10d,0d,VConfig.getInstance().getGlobalConfig("PageX"));
		initConfigElement("gapBeforeMeasure",20d,0d,VConfig.getInstance().getGlobalConfig("PageX"));
		initConfigElement("gapBetweenElement",VConfig.getInstance().getGlobalConfig("MinNoteDistance"),0d,VConfig.getInstance().getGlobalConfig("PageX"),false);
		initConfigElement("gapBetweenGrace",5d,0d,VConfig.getInstance().getGlobalConfig("PageX"));
	}

	public void initDrumNotations(){
		double durationCounter = 0;
		VGNotation notation;
		notation = new VDrumGNotation();

		for (VNote note:Notes){
			String type = "quarter";
			if (note.type!=null){
				type = note.type;
			}
			if (VUtility.NoteType2Integer(type)<=2){ // do not create notation for whole and half notes.

			}else {
				if (!note.isGrace&&!note.isRest){
					notation.addNote(note.number, type, note.dots);
				}
			}
			durationCounter += (1d/VUtility.NoteType2Integer(type));

			if (durationCounter>=0.5){
				durationCounter = 0;
				Notations.add(notation);
				notation = new VDrumGNotation();
			}
		}

		if (notation.getSize()>0){
			Notations.add(notation);
		}
		for (VGNotation notationf:Notations){
			notationf.initElements();
			group.getChildren().add(notationf.getShapeGroups());
		}
	}
	public void initGuitarNotations(){
		double durationCounter = 0;
		VGNotation notation;
		notation = new VGuitarGNotation();

		for (VNote note:Notes){
			String type = "quarter";
			if (note.type!=null){
				type = note.type;
			}
			if (!note.isGrace&&!note.isRest){
				notation.addNote(note.number, type,note.dots);
			}

			durationCounter += (1d/VUtility.NoteType2Integer(type));

			if (durationCounter>=0.5){
				durationCounter = 0;
				Notations.add(notation);
				notation = new VGuitarGNotation();
			}
		}

		if (notation.getSize()>0){
			Notations.add(notation);
		}
		for (VGNotation notationf:Notations){
			notationf.initElements();
			group.getChildren().add(notationf.getShapeGroups());
		}
	}
	public void initNoteGroups(){
		for (VNote vNote:Notes){
			group.getChildren().add(vNote.getShapeGroups());
		}
	}

	public VLine getVLine() {
		return line;
	}

	public void setVLine(VLine line) {
		this.line = line;
	}

	public void addNoteHead(Note note, VNote vNote){
		int dots = 0;
		if (note.getDots()!=null){
			dots = note.getDots().size();
		}

		VNoteHead noteHead = null;
		String step;
		int octave;
		int relative = 1;
		//TODO set relative to default rest position. calculate based on the staffline.
		if (note.getRest()!=null){
			noteHead = new VNoteHead(VUtility.getDrumAssetName(note),0,relative,false,vNote);
			noteHead.updateConfig("scale",3);
			vNote.setRest(true);
		}else {
			if (instrument.equals("TAB")){
				if (note.getNotations()!=null&&note.getNotations().getTechnical()!=null){
					relative = note.getNotations().getTechnical().getString()*3; // since tab staff is double-space\
					noteHead = new VNoteHead(note.getNotations().getTechnical().getFret(),dots,relative,note.getGrace()!=null,vNote);
					if (note.getNotations().getTechnical().getBend()!=null){
						noteHead.addBend(note.getNotations().getTechnical().getBend().getBendAlter());
					}
				}
			}else {
				String result = VUtility.getDrumAssetName(note);
				step = note.getUnpitched().getDisplayStep();
				octave = note.getUnpitched().getDisplayOctave();
				relative = VUtility.getRelative(step,octave);
				noteHead = new VNoteHead(result,dots,relative,note.getGrace()!=null,vNote);
			}
		}
		if (note.getNotations()!=null){
			if (note.getNotations().getTieds()!=null&&note.getRest()==null){
				noteHead.setTieds(note.getNotations().getTieds());
				tieNoteHead.add(noteHead);
			}
			if (note.getNotations().getSlurs()!=null&&note.getRest()==null){
				noteHead.setSlurs(note.getNotations().getSlurs());
				slurNoteHead.add(noteHead);
			}
		}
		if (note.getGrace()!=null){
			vNote.setGrace(true);
		}
		if (note.getType()!=null) {
			vNote.setNoteType(note.getType());
		}
		noteHead.setNote(note);
		vNote.addNoteHead(noteHead);
	}




	@Override
	public void setHighLight(HighLight states) {
		Color color = null;
		highLight = states;
		switch (states){
			case NULL -> color = VConfig.getInstance().getDefaultColor();
			case PLAY -> color = VConfig.getInstance().getPlayColor();
			case SELECTED -> color = VConfig.getInstance().getHighLightColor();
		}
		for (VGNotation notation:Notations){
			notation.setHighLight(states);
		}
		for (Line line:staffLines){
			line.setStroke(color);
		}
		for (VNote note:Notes){
			note.setHighLight(states);
		}
		for (VBarline barline:barlines){
			barline.setHighLight(states);
		}
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public double getGapCount(){
		return gapCount;
	}

	public void initStaffLines(List<Integer> staffInfo){
		//staffInfo contain offset of each staff
		for (Integer i:staffInfo){
			Line line = new Line(0,0,0,0);
			double gap = VConfig.getInstance().getGlobalConfig("Step");
			line.setLayoutY(i*gap);//
			staffLines.add(line);
			group.getChildren().add(line);
		}
	}
	public void updateStaffLine(double W){
		for (Line line:staffLines){
			line.setEndX(W);
		}
	}
	public void setShowClef(boolean states){
		this.showClef = states;
	}
	public void initBarlines(List<BarLine> barLine){
		barlines = new ArrayList<>();
		double length = staffLines.get(staffLines.size()-1).getLayoutY();
		boolean hasRightEnd = false;
		if (barLine!=null){
			for (BarLine barLine1:barLine){
				barlines.add(new VBarline(length,barLine1.barStyle,barLine1.repeat,barLine1.location));
				if (barLine1.location.equals("right")){
					hasRightEnd = true;
				}
			}
		}
		if (!hasRightEnd){
			barlines.add(new VBarline(length,"default",null,"right"));
		}
		for (VBarline vBarline:barlines){
			group.getChildren().add(vBarline.getShapeGroups());
		}
	}
	public void alignmentBarlines(){
		for (VBarline vBarline:barlines){
			vBarline.alignment();
			if (vBarline.location.equals("right")){
				vBarline.getShapeGroups().setLayoutX(W-vBarline.getConfigAbleList().get("distanceBetweenLine"));
			}else {
				vBarline.getShapeGroups().setLayoutX(0);
			}
		}
	}
	public void alignmentNotations(){
		for (VGNotation notation:Notations){
			List<Double> HPosition = new ArrayList<>();
			for (Integer i:notation.getNotes()){
				double offset = VConfig.getInstance().getGlobalConfig("Step")*2;
				if (instrument.equals("TAB")){
					HPosition.add(Notes.get(i).getShapeGroups().getLayoutX()+offset/2);
				}else {
					HPosition.add(Notes.get(i).getShapeGroups().getLayoutX()+offset);

				}
			}
			//H position for each note
			List<Double> VPosition = new ArrayList<>();
			for (Integer i:notation.getNotes()){
				VPosition.add(Notes.get(i).maxVPos);
			}
			notation.alignment(HPosition,VPosition);
			//V position for the most bottom note
		}
	}
	public void addGapBetweenElements(double width){
		W += width;
		gapCount++;
	}
	public void alignment(){
		W = 0;
		background.setLayoutY(0);
		background.setLayoutX(0);
		background.setWidth(0);
		background.setHeight(0);
		W += configMap.get("gapBeforeMeasure");
		gapCount = 0;
		double gapBetweenElement = configMap.get("gapBetweenElement");
		double gapBetweenGrace = configMap.get("gapBetweenGrace");

		for (VSign sign:Signs){
			sign.alignment();
			sign.getShapeGroups().setLayoutX(W);
			W += sign.getW();
			addGapBetweenElements(gapBetweenElement);
		}
		for (VNote note:Notes){
			note.alignment();
			double offsetX = note.offsetX;
			note.getShapeGroups().setLayoutX(W+offsetX);
			W += note.getW()+offsetX;
			if (note.isGrace){
				W += gapBetweenGrace;
			}else {
				addGapBetweenElements(gapBetweenElement);
			}
		}
		addGapBetweenElements(gapBetweenElement);
		//update the staffline.
		updateStaffLine(W);
		Bounds bounds = group.getBoundsInLocal();
		background.setLayoutX(bounds.getMinX());
		background.setLayoutY(bounds.getMinY());
		background.setHeight(bounds.getHeight());
		background.setWidth(W);
		background.setFill(Color.TRANSPARENT);
		background.toBack();
		alignmentBarlines();
		alignmentNotations();
		setHighLight(highLight);
	}
	public double getWInMinWidth(){
		double minW = 0;
		minW += configMap.get("gapBeforeMeasure");
		double gapBetweenElement = configMap.get("MinNoteDistance");
		double gapBetweenGrace = configMap.get("gapBetweenGrace");
		for (VSign sign:Signs){
			sign.alignment();
			minW += sign.getW();
			minW += gapBetweenElement;
		}
		for (VNote note:Notes){
			note.alignment();
			double offsetX = note.offsetX;
			minW += note.getW()+offsetX;
			if (note.isGrace){
				minW += gapBetweenGrace;
			}else {
				minW += gapBetweenElement;
			}
		}
		minW += gapBetweenElement;
		return minW;
	}
	public List<VNoteHead> getTieNoteHead() {
		return tieNoteHead;
	}
	public List<VNoteHead> getSlurNoteHead() {
		return slurNoteHead;
	}
	public List<VNote> getNotes() {
		return Notes;
	}

	public int getNumber() {
		return number;
	}
}
