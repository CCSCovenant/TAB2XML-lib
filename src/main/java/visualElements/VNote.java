package visualElements;

import javafx.scene.Group;
import visualElements.Notations.VINotation;

import java.util.HashMap;
import java.util.List;

public class VNote extends VElement implements VConfigAble {
	int number;
	List<VNoteHead> noteHeads;
	List<VINotation> notations;

	public VNote(){

	}


	@Override
	public void setHighLight(boolean states) {

	}

	@Override
	public Group getShapeGroups() {
		return null;
	}

	@Override
	public double getH() {
		return 0;
	}

	@Override
	public double getW() {
		return 0;
	}



	@Override
	public HashMap<String, Double> getConfigAbleList() {
		return null;
	}

	@Override
	public void updateConfig(String id, double value) {

	}
}
