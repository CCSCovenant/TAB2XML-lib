package visualElements;

import models.measure.note.notations.Slur;
import models.measure.note.notations.Tied;
import visualElements.Notations.VCurvedNotation;

import java.util.*;

public class VLine extends VElement{
	List<VMeasure> measures = new ArrayList<>();
	List<VCurvedNotation> curvedNotations = new ArrayList<>();
	ArrayList<VNoteHead> tiedElements = new ArrayList<>();
	ArrayList<VNoteHead> slurElements = new ArrayList<>();
	double MarginX = VConfig.getInstance().getGlobalConfig("MarginX");
	double PageW = VConfig.getInstance().getGlobalConfig("PageX");
	VClef vClef;
	double gapCount = 0;
	public VLine(String stafftype){
		W = MarginX;
		vClef = new VClef(stafftype);
		group.getChildren().add(vClef.getShapeGroups());
		vClef.alignment();
		W += vClef.getW();
	}


	@Override
	public void setHighLight(HighLight states) {

	}

	public boolean addNewMeasure(VMeasure newMeasure){
		// calculate if new Measure's length will exceed the width of a page
		// if new measure is exceed the page width: return false and adjust rest of measure in order to fit in the page
		// otherwise, add this measure into this line.

			newMeasure.alignment();
			double minW = newMeasure.getWInMinWidth();
			if (W+minW>PageW-MarginX){
				if (measures.size()==0){
					//TODO give user a warning that this line is squeezed under current setting. please adjust the setting.
					measures.add(newMeasure);
					newMeasure.setVLine(this);
					group.getChildren().add(newMeasure.getShapeGroups());
					return true;
				}else {
					return false;
				}
			}else {
				if (measures.size()==0){
					newMeasure.setShowClef(true);
					newMeasure.alignment();
				}else {
					newMeasure.setShowClef(false);
				}
				measures.add(newMeasure);
				newMeasure.setVLine(this);
				W += newMeasure.getWInMinWidth();
				group.getChildren().add(newMeasure.getShapeGroups());
				return true;
			}

	}
	public void initCurvedNotations(){
		for (VMeasure measure:measures){
			for (VNoteHead noteHead:measure.getTieNoteHead()){
				tiedElements.add(noteHead);
			}
		}
		for (VMeasure measure:measures){
			for (VNoteHead noteHead:measure.getSlurNoteHead()){
				slurElements.add(noteHead);
			}
		}
		Queue<Integer> queue = new LinkedList<>();
		for (int i=0;i<tiedElements.size();i++){
			VNoteHead noteHead = tiedElements.get(i);
			for (Tied tied:noteHead.tieds){
				if (tied.getType().equals("start")){
					queue.add(i);
				}else {
					VCurvedNotation curvedNotation = new VCurvedNotation("Tied");
					curvedNotation.setEndID(i);
					if (queue.size()<1){;
					}else {
						curvedNotation.setStartID(queue.poll());
					}
					curvedNotations.add(curvedNotation);
					group.getChildren().add(curvedNotation.getShapeGroups());
				}
			}
		}
		while (queue.size()>0){
			VCurvedNotation curvedNotation = new VCurvedNotation("Tied");
			curvedNotation.setStartID(queue.poll());
			curvedNotations.add(curvedNotation);
			group.getChildren().add(curvedNotation.getShapeGroups());
		}
		HashMap<Integer,Integer> slurMap = new HashMap<>();
		HashMap<Integer,Slur> slurMap2 = new HashMap<>();

		for (int i=0;i<slurElements.size();i++){
			VNoteHead noteHead = slurElements.get(i);
			for (Slur slur:noteHead.slurs){
				if (slur.getType().equals("start")){
					slurMap.put(slur.getNumber(),i);
					slurMap2.put(slur.getNumber(),slur);
				}else {
					int start = -2;
					Slur slur1 = null;
					if (slurMap.containsKey(slur.getNumber())){
						start = slurMap.get(slur.getNumber());
						slur1 = slurMap2.get(slur.getNumber());
						slurMap.remove(slur.getNumber());
					}

					VCurvedNotation curvedNotation = new VCurvedNotation("Slur");
					curvedNotation.setStartID(start);
					curvedNotation.setEndID(i);
					curvedNotations.add(curvedNotation);
					group.getChildren().add(curvedNotation.getShapeGroups());
					if (slur.getPlacement()!=null){
						if (slur.getPlacement().equals("above")){
							curvedNotation.setPositive(true);
						}
					}
					if (slur1!=null&&slur1.getPlacement()!=null){
						if (slur1.getPlacement().equals("above")){
							curvedNotation.setPositive(true);
						}
					}
				}
			}
		}
		for (Integer i:slurMap.keySet()){
			VCurvedNotation curvedNotation = new VCurvedNotation("Slur");
			curvedNotation.setStartID(slurMap.get(i));
			curvedNotations.add(curvedNotation);
			group.getChildren().add(curvedNotation.getShapeGroups());
		}


	}
	public void alignmentCurved(){
		double lineStart = 0;
		double lineEnd = 0;
		lineStart = vClef.getW();
		lineEnd = W;
		double step =VConfig.getInstance().getGlobalConfig("Step");
		for (VCurvedNotation curvedNotation:curvedNotations){
			if (curvedNotation.getType().equals("Tied")){
				double startX = lineStart;
				double endX = lineEnd;
				double Y = 0;
				if (curvedNotation.getStartID()>=0){
					VNoteHead noteHead = tiedElements.get(curvedNotation.getStartID());
					Y = noteHead.relative* noteHead.step;
					if (VConfig.getInstance().getInstrument().equals("TAB")){
						Y -= 2*noteHead.step;
					}
					VNote pNote = noteHead.getParentNote();
					VMeasure pMeasure = pNote.getParentMeasure();
					startX = pMeasure.getShapeGroups().getLayoutX()+pNote.getShapeGroups().getLayoutX()+step ;
				}
				if (curvedNotation.getEndID()>=0){
					VNoteHead noteHead = tiedElements.get(curvedNotation.getEndID());
					Y = noteHead.relative* noteHead.step;
					if (VConfig.getInstance().getInstrument().equals("TAB")){
						Y -= 2*noteHead.step;
					}
					VNote pNote = noteHead.getParentNote();
					VMeasure pMeasure = pNote.getParentMeasure();
					endX = pMeasure.getShapeGroups().getLayoutX()+pNote.getShapeGroups().getLayoutX();
				}
				if (Y<3*step){
					curvedNotation.setPositive(true);
				}
				curvedNotation.alignment(startX,endX,Y);

			}else if (curvedNotation.getType().equals("Slur")){
				double startX = lineStart;
				double endX = lineEnd;
				double Y = 0;
				if (curvedNotation.getStartID()>=0){
					VNoteHead noteHead = slurElements.get(curvedNotation.getStartID());
					Y = noteHead.relative* noteHead.step;
					if (VConfig.getInstance().getInstrument().equals("TAB")){
						Y -= 2*noteHead.step;
					}
					VNote pNote = noteHead.getParentNote();
					VMeasure pMeasure = pNote.getParentMeasure();
					startX = pMeasure.getShapeGroups().getLayoutX()+pNote.getShapeGroups().getLayoutX()+step;
				}
				if (curvedNotation.getEndID()>=0){
					VNoteHead noteHead = slurElements.get(curvedNotation.getEndID());
					Y = noteHead.relative* noteHead.step;
					if (VConfig.getInstance().getInstrument().equals("TAB")){
						Y -= 2*noteHead.step;
					}
					VNote pNote = noteHead.getParentNote();
					VMeasure pMeasure = pNote.getParentMeasure();
					endX = pMeasure.getShapeGroups().getLayoutX()+pNote.getShapeGroups().getLayoutX();
				}
				curvedNotation.alignment(startX,endX,Y);

			}
		}
	}

	public List<VMeasure> getMeasures() {
		return measures;
	}

	public void alignment() {
		W = MarginX + vClef.getW();
		double availableLength = PageW - MarginX -W;
		double lengthBeforeAlignment = 0;
		double[] measureLengthBeforeAlignment = new double[measures.size()];
		double radio = 1;
		gapCount = 0;
		for (int i=0;i<measures.size();i++){
			double measureLength = measures.get(i).getWInMinWidth();
			lengthBeforeAlignment += measureLength;
			measureLengthBeforeAlignment[i] = measureLength;
		}
		radio = (availableLength-lengthBeforeAlignment)/lengthBeforeAlignment;
		for (int i=0;i<measures.size();i++){
			VMeasure measure = measures.get(i);
			double idealLengthDiff = measureLengthBeforeAlignment[i]*radio;
			double ideaGapDiff = idealLengthDiff / measure.getGapCount();
			measure.updateConfig("gapBetweenElement",measure.getConfigAbleList().get("MinNoteDistance")+ideaGapDiff);
		}

		W = 0;
		vClef.alignment();
		W += vClef.getW();
		
		for (int i=0;i<measures.size();i++){
			measures.get(i).getShapeGroups().setLayoutX(W);
			measures.get(i).alignment();
			W = W+measures.get(i).getW();
		}

		alignmentCurved();
	}

}
