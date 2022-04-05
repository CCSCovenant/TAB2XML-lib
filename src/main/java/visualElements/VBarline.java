package visualElements;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import models.measure.barline.Repeat;

public class VBarline extends VElement{
	String location;
	Text notation = new Text();
	Line Lightline;
	Line Hevayline;
	Circle circle = new Circle(2);
	Circle circle1 = new Circle(2);
	public VBarline(double length, String style, Repeat repeat,String location){
		initConfig();
		double offset = configMap.get("distanceBetweenLine");
		this.location = location;

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
		if (repeat!=null){
			if (repeat.getTimes()!=null){
				double notationHeight = configMap.get("notationHeight");
				double notationSize = configMap.get("notationSize");
				notation.setText(repeat.getTimes()+"x");
				notation.setFont(new Font(notationSize));
				notation.setLayoutY(notationHeight);
				group.getChildren().add(notation);
			}
			if (repeat.getDirection().equals("forward")){
				circle.setLayoutX(offset*2);
				circle1.setLayoutX(offset*2);
				notation.setLayoutX(offset*2);

				circle.setLayoutY(length*1/6);
				circle1.setLayoutY(length*5/6);

				group.getChildren().add(circle);
				group.getChildren().add(circle1);
			}else if (repeat.getDirection().equals("backward")){
				circle.setLayoutX(-offset);
				circle1.setLayoutX(-offset);
				notation.setLayoutX(-offset-notation.getBoundsInLocal().getWidth());

				circle.setLayoutY(length*1/6);
				circle1.setLayoutY(length*5/6);

				group.getChildren().add(circle);
				group.getChildren().add(circle1);
			}
		}

	}

	public void initConfig(){
		configMap.put("distanceBetweenLine",5d);
		configMap.put("notationHeight",-20d);
		configMap.put("notationSize",15d);
	}
	@Override
	public void setHighLight(boolean states) {
		Color color;
		if (states){
			color	= VConfig.getInstance().getHighLightColor();
		}else {
			color = VConfig.getInstance().getDefaultColor();
		}
		notation.setFill(color);
		circle.setFill(color);
		circle1.setFill(color);
		if (Lightline!=null){
			Lightline.setFill(color);
		}
		if (Hevayline!=null){
			Hevayline.setFill(color);
		}
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
