package visualElements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VNote extends VElement {
	int number;
	int noteHeadCount = 1;
	int dots = 0;
	double maxVPos = 0;
	double offsetX = 0;
	HashMap<Integer,Boolean> blockedPos = new HashMap<>();
	List<VNoteHead> noteHeads = new ArrayList<>();
	boolean isGrace = false;
	boolean isRest = false;
	VMeasure parentMeasure;
	String type;
	public VNote(int i,VMeasure parentMeasure){
		this.parentMeasure = parentMeasure;
		this.number = i;
		initConfig();
	}

	public void addNoteHead(VNoteHead noteHead){
		blockedPos.put(noteHead.relative,true);
		boolean forward = blockedPos.containsKey(noteHead.relative+1);
		boolean backward = blockedPos.containsKey(noteHead.relative-1);
		if (forward||backward){
			noteHead.getShapeGroups().setLayoutX(VConfig.getInstance().getGlobalConfig("Step")*2);
			noteHead.setFlip();
		}

		noteHeads.add(noteHead);
		noteHead.alignment();
		dots = noteHead.getDotC();
		group.getChildren().add(noteHead.getShapeGroups());
	}
	public void initConfig(){
		initConfigElement("graceOffset",5,0,10,false);
	}
	public void setNoteType(String type){
		if (type==null){
			this.type = "quarter";
		}else {
			this.type = type;
		}

	}


	public void setGrace(boolean grace) {
		isGrace = grace;
	}

	public void setRest(boolean rest) {
		isRest = rest;
	}

	public void alignment(){

		for(VNoteHead noteHead:noteHeads){
			noteHead.alignment();
			W = Math.max(W,noteHead.getW());
			maxVPos = Math.max(maxVPos,noteHead.getShapeGroups().getLayoutY());
		}
		W = group.getBoundsInLocal().getWidth();
	}

	public VMeasure getParentMeasure() {
		return parentMeasure;
	}
}
