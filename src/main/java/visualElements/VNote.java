package visualElements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VNote extends VElement implements VConfigAble {
	int number;
	int noteHeadCount = 1;

	double maxVPos = 0;
	HashMap<String,Double> configMap = new HashMap<>();
	double offsetX = 0;
	HashMap<Integer,Boolean> blockedPos = new HashMap<>();
	List<VNoteHead> noteHeads = new ArrayList<>();
	boolean isGrace = false;
	boolean isRest = false;
	String type;
	public VNote(int i){
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
		group.getChildren().add(noteHead.getShapeGroups());
	}
	public void initConfig(){
		configMap.put("graceOffset",5d);
	}
	public void setNoteType(String type){
		if (type==null){
			this.type = "quarter";
		}else {
			this.type = type;
		}

	}
	@Override
	public void setHighLight(boolean states) {
		for (VNoteHead noteHead:noteHeads){
			noteHead.setHighLight(states);
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

	@Override
	public HashMap<String, Double> getConfigAbleList() {
		return configMap;
	}

	@Override
	public void updateConfig(String id, double value) {
		configMap.put("id",value);
	}
}
