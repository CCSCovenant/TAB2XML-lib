package visualElements;

import javafx.scene.Group;
import visualElements.Notations.VINotation;

import java.util.HashMap;
import java.util.List;

public class VNote extends VElement implements VConfigAble {
	int number;
	double H = 0;
	double W = 0;
	double step = VConfig.getInstance().getGlobalConfig().get("step");
	List<VNoteHead> noteHeads;
	List<VINotation> notations;

	public VNote(int number){
		this.number = number;
	}

	public void addNoteHead(VNoteHead noteHead, int relative){
		noteHeads.add(noteHead);
		noteHead.getShapeGroups().setLayoutY(relative*step);
		group.getChildren().add(noteHead.getShapeGroups());
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
