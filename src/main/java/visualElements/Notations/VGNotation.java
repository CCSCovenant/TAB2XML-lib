package visualElements.Notations;

import javafx.scene.Group;
import visualElements.VConfigAble;
import visualElements.VElement;

import java.util.HashMap;

public class VGNotation implements VElement, VConfigAble {
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
	public void updateConfigList(HashMap<String, Double> configs) {

	}

	@Override
	public HashMap<String, Double> getConfigAbleList() {
		return null;
	}

	@Override
	public void updateConfig(String id, double value) {

	}
}
