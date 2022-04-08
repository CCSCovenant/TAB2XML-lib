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
		initConfigElement("size",1.5d,0d,VConfig.getInstance().getGlobalConfig("PageX"));
		initConfigElement("gap_with_last_element",5d,0d,VConfig.getInstance().getGlobalConfig("PageX"));

	}

	@Override
	public void setHighLight(HighLight states) {
		Color color = null;
		highLight = states;
		switch (states){
			case NULL -> color = VConfig.getInstance().getDefaultColor();
			case PLAY -> color = VConfig.getInstance().getPlayColor();
			case SELECTED -> color = VConfig.getInstance().getHighLightColor();
		}
		circle.setStroke(color);
	}

	@Override
	public Group getShapeGroups() {
		return group;
	}

	public void alignment(){
		W = circle.getRadius()*2;
		setHighLight(highLight);
	}
}
