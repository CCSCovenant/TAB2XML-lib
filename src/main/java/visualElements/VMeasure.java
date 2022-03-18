package visualElements;

import javafx.scene.Group;
import visualElements.Notations.VGNotation;

import java.util.HashMap;
import java.util.List;

public class VMeasure implements VElement,VConfigAble {
	private int number;
	private List<VNote> Notes;
	private List<VGNotation> Notations;
	private List<VStaffLine> staffLines;
	private List<VBarline> barlines;
	private HashMap<String,Double> Configs = VConfig.getInstance().getDefaultConfigMap("VMeasure");


	public VMeasure(){

	}


	@Override
	public void updateConfigList(HashMap<String, Double> configs) {

	}

	@Override
	public HashMap<String, Double> getConfigAbleList() {
		return null;
	}

	@Override
	public void updateConfig(String id, double value) {

	}

	@Override
	public void setHighLight(boolean states) {

	}

	@Override
	public Group getShapeGroups() {
		return null;
	}
}
