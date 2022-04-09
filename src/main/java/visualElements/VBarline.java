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
	String style;
	Repeat repeat;
	double length;
	public VBarline(double length, String style, Repeat repeat,String location){
		initConfig();
		double offset = configMap.get("distanceBetweenLine");
		this.location = location;
		this.style =style;
		this.repeat = repeat;
		this.length = length;
		Hevayline = new Line();
		Lightline = new Line();
		switch (style){
			case "heavy-light":
			case "light-heavy":
				group.getChildren().add(Hevayline);
				group.getChildren().add(Lightline);
				break;
			default:
				group.getChildren().add(Lightline);
		}
		if (repeat!=null){
			if (repeat.getTimes()!=null){
				group.getChildren().add(notation);
			}
				group.getChildren().add(circle);
				group.getChildren().add(circle1);
		}
	}

	public void initConfig(){
		initConfigElement("distanceBetweenLine",5d,0d,VConfig.getInstance().getGlobalConfig("PageX"));
		initConfigElement("notationHeight",-20d,-50d,VConfig.getInstance().getGlobalConfig("PageX"));
		initConfigElement("notationSize",15d,0d,VConfig.getInstance().getGlobalConfig("PageX"));
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
		notation.setFill(color);
		circle.setFill(color);
		circle1.setFill(color);
		if (Lightline!=null){
			Lightline.setFill(color);
			Lightline.setStroke(color);
		}
		if (Hevayline!=null){
			Hevayline.setFill(color);
			Hevayline.setStroke(color);
		}
	}
	public void alignment(){
		double offset = configMap.get("distanceBetweenLine");

		switch (style){
			case "heavy-light":
				Hevayline.setStartX(0);
				Hevayline.setEndX(0);
				Hevayline.setStartY(0);
				Hevayline.setEndY(length);

				Lightline.setStartX(offset);
				Lightline.setEndX(offset);
				Lightline.setStartY(0);
				Lightline.setEndY(length);
				break;
			case "light-heavy":
				Lightline.setStartX(0);
				Lightline.setEndX(0);
				Lightline.setStartY(0);
				Lightline.setEndY(length);

				Hevayline.setStartX(offset);
				Hevayline.setEndX(offset);
				Hevayline.setStartY(0);
				Hevayline.setEndY(length);
				break;
			default:
				Lightline = new Line(0,0,0,length);
				Lightline.setStrokeWidth(1);
		};
		if (repeat!=null){
			if (repeat.getTimes()!=null){
				double notationHeight = configMap.get("notationHeight");
				double notationSize = configMap.get("notationSize");
				notation.setText(repeat.getTimes()+"x");
				notation.setFont(new Font(notationSize));
				notation.setLayoutY(notationHeight);
			}
			if (repeat.getDirection().equals("forward")){
				circle.setLayoutX(offset*2);
				circle1.setLayoutX(offset*2);
				notation.setLayoutX(offset*2);

				circle.setLayoutY(length*1/6);
				circle1.setLayoutY(length*5/6);
			}else if (repeat.getDirection().equals("backward")){
				circle.setLayoutX(-offset);
				circle1.setLayoutX(-offset);
				notation.setLayoutX(-offset-notation.getBoundsInLocal().getWidth());
				circle.setLayoutY(length*1/6);
				circle1.setLayoutY(length*5/6);
			}
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
