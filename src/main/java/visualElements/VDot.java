package visualElements;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashMap;

public class VDot extends VElement implements VConfigAble{
	Circle circle = new Circle();
	HashMap<String,Double> config = VConfig.getInstance().getDefaultConfigMap("dot");
	public VDot(){
		group.getChildren().add(circle);
	}

	@Override
	public HashMap<String, Double> getConfigAbleList() {
		return null;
	}

	@Override
	public void updateConfig(String id, double value) {
		switch (id){
			case "size":
				config.put("size",value);
				circle.setRadius(config.get("size"));
				break;
			case "gap_with_last_element":
				config.put("gap_with_last_element",value);
				circle.setLayoutX(config.get("gap_with_last_element"));
				break;
		}
	}

	@Override
	public void setHighLight(boolean states) {
		if (states){
			circle.setFill(Color.BLUE);
		}else {
			circle.setFill(Color.BLACK);
		}
	}

	@Override
	public Group getShapeGroups() {
		return group;
	}

	@Override
	public double getH() {
		return circle.getRadius()*2;
	}

	@Override
	public double getW() {
		return config.get("gap_with_last_element")+circle.getRadius()*2;
	}
}
