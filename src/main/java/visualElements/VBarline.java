package visualElements;

import javafx.scene.Group;

import java.util.HashMap;

public class VBarline implements VElement,VConfigAble{
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
