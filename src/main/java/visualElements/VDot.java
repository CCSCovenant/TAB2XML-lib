package visualElements;

import javafx.scene.Group;

import java.util.HashMap;

public class VDot implements VElement,VConfigAble{
	public VDot(){

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

	@Override
	public double getH() {
		return 0;
	}

	@Override
	public double getW() {
		return 0;
	}
}
