package visualElements;

import javafx.scene.Group;
import visualElements.Notations.VINotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VNote extends VElement implements VConfigAble {
	int number;
	int noteHeadCount = 1;
	double H = 0;
	double W = 0;
	double maxVPos = 0;
	HashMap<String,Double> configMap = VConfig.getInstance().getDefaultConfigMap("note");

	List<VNoteHead> noteHeads = new ArrayList<>();
	List<VINotation> notations;
	String type;
	public VNote(int i){
		this.number = i;
	}

	public void addNoteHead(VNoteHead noteHead){
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

	@Override
	public Group getShapeGroups() {
		return group;
	}

	@Override
	public double getH() {
		return H;
	}

	@Override
	public double getW() {
		return W;
	}

	public void alignment(){
		for(VNoteHead noteHead:noteHeads){
			noteHead.alignment();
			W = Math.max(W,noteHead.getW());
			maxVPos = Math.max(maxVPos,noteHead.getShapeGroups().getLayoutY());
		}
	}

	@Override
	public HashMap<String, Double> getConfigAbleList() {
		return null;
	}

	@Override
	public void updateConfig(String id, double value) {

	}
}
