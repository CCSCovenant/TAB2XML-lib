package visualElements;

import javafx.scene.Group;
import javafx.scene.shape.Line;
import models.measure.barline.Repeat;

import java.util.HashMap;

public class VBarline extends VElement implements VConfigAble{
	HashMap<String,Double> config = VConfig.getInstance().getDefaultConfigMap("barline");
	String location;
	public VBarline(double length, String style, Repeat repeat,String location){
		double offset = config.get("distanceBetweenLine");
		this.location = location;
		Line Lightline;
		Line Hevayline;
		switch (style){
			case "heavy-light":
				Hevayline = new Line(0,0,0,length);
				Hevayline.setStrokeWidth(2);
				Lightline = new Line(offset,0,offset,length);
				Lightline.setStrokeWidth(1);
				group.getChildren().add(Hevayline);
				group.getChildren().add(Lightline);
				break;
			case "light-heavy":
				Hevayline = new Line(offset,0,offset,length);
				Hevayline.setStrokeWidth(2);
				Lightline = new Line(0,0,0,length);
				Lightline.setStrokeWidth(1);
				group.getChildren().add(Hevayline);
				group.getChildren().add(Lightline);
				break;
			default:
				Lightline = new Line(0,0,0,length);
				Lightline.setStrokeWidth(1);
				group.getChildren().add(Lightline);
		}

	}

	@Override
	public HashMap<String, Double> getConfigAbleList() {
		return config;
	}

	@Override
	public void updateConfig(String id, double value) {
		config.put(id,value);
	}

	@Override
	public void setHighLight(boolean states) {

	}

	@Override
	public Group getShapeGroups() {
		return group;
	}


	@Override
	public double getW() {
		return 0;
	}
}
