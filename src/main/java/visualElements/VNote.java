package visualElements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VNote extends VElement implements VConfigAble {
	int number;
	int noteHeadCount = 1;
	double maxVPos = 0;
	HashMap<String,Double> configMap = VConfig.getInstance().getDefaultConfigMap("note");
	HashMap<Integer,Boolean> blockedPos = new HashMap<>();
	List<VNoteHead> noteHeads = new ArrayList<>();
	double graceOffset = configMap.get("graceOffset");
	double internalOffset = 0;
	String type;
	public VNote(int i){
		this.number = i;
	}

	public void addNoteHead(VNoteHead noteHead){
		blockedPos.put(noteHead.relative,true);
		boolean forward = blockedPos.containsKey(noteHead.relative+1);
		boolean backward = blockedPos.containsKey(noteHead.relative-1);
		if (forward||backward){
			noteHead.getShapeGroups().setLayoutX(VConfig.getInstance().getGlobalConfig().get("Step"));
			noteHead.setFlip(true);
		}
		System.out.println(internalOffset);
		noteHead.getShapeGroups().setLayoutX(internalOffset);

		if (noteHead.isGrace){
			internalOffset += graceOffset + noteHead.getW();
			noteHead.updateConfig("scale",0.7d);
		}
		noteHeads.add(noteHead);
		noteHead.alignment();
		group.getChildren().add(noteHead.getShapeGroups());
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

	}

	public void alignment(){
		for(VNoteHead noteHead:noteHeads){
			noteHead.alignment();
			if (!noteHead.isGrace){
				W = Math.max(W,noteHead.getW());
			}
			maxVPos = Math.max(maxVPos,noteHead.getShapeGroups().getLayoutY());
		}
		W += internalOffset;
	}

	@Override
	public HashMap<String, Double> getConfigAbleList() {
		return configMap;
	}

	@Override
	public void updateConfig(String id, double value) {

	}
}
