package visualElements;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class VDot extends VElement{
	Circle circle = new Circle();
	public VDot(){
		initConfig();
		circle.setRadius(configMap.get("size"));
		group.getChildren().add(circle);
	}
	public void initConfig(){
		configMap.put("size",1.5d);
		configMap.put("gap_with_last_element",5d);
	}

	@Override
	public void setHighLight(boolean states) {
		Color color;
		if (states){
			color	= VConfig.getInstance().getHighLightColor();
		}else {
			color = VConfig.getInstance().getDefaultColor();
		}
		circle.setStroke(color);
	}

	@Override
	public Group getShapeGroups() {
		return group;
	}

	public void alignment(){
		W = circle.getRadius()*2;
	}
}
