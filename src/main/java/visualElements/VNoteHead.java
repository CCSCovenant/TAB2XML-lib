package visualElements;

import javafx.geometry.Bounds;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import visualizer.ImageResourceHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VNoteHead extends VElement implements VConfigAble{
	ImageView imageView = new ImageView();
	Text text = new Text();
	ImageResourceHandler imageResourceHandler = ImageResourceHandler.getInstance();
	List<VDot> dots = new ArrayList<>();
	Rectangle background = new Rectangle();
	Line line = new Line();
	HashMap<String,Double> config = VConfig.getInstance().getDefaultConfigMap("noteHead");
	int relative = 0;
	double step = VConfig.getInstance().getGlobalConfig().get("Step");
	public VNoteHead(String AssetName,int dots,int relative){
		this.relative = relative;
		List<Integer> staff = VConfig.getInstance().getStaffDetail();
		if (relative<staff.get(0)||relative>staff.get(staff.size()-1)){
			if (Math.abs(relative-staff.get(0))%2==0){
				group.getChildren().add(line);
			}
		}
		imageView.setImage(new Image(imageResourceHandler.getImage(AssetName)));
		imageView.setFitHeight(step*2*config.get("scale"));
		imageView.setFitWidth(step*2*config.get("scale"));

		Bounds bounds = imageView.getBoundsInLocal();
		background.setWidth(bounds.getWidth());
		background.setHeight(bounds.getHeight());
		background.setFill(Color.TRANSPARENT);
		group.getChildren().add(background);
		group.getChildren().add(imageView);
		W = group.getBoundsInLocal().getWidth();
		initDots(dots);
	}
	public VNoteHead(int fret,int dots,int relative){
		this.relative = relative-2; // double-spaced for tab
		text.setText(fret+"");
		text.setFont(new Font(step*2*config.get("scale")*1.3));
		Bounds bounds = text.getBoundsInLocal();
		background.setWidth(bounds.getWidth());
		background.setHeight(bounds.getHeight()*0.75);
		background.setFill(Color.WHITESMOKE);
		background.setLayoutY(-bounds.getHeight()*0.75);
		group.getChildren().add(background);
		group.getChildren().add(text);
		W = group.getBoundsInLocal().getWidth();
		initDots(dots);
	}
	public void setFlip(boolean states){

	}
	@Override
	public void setHighLight(boolean states) {

		Color color;
		if (states){
			color	= VConfig.getInstance().getHighLightColor();
		}else {
			color = VConfig.getInstance().getDefaultColor();
		}
		Blend blend = new Blend();
		Bounds bounds = imageView.getBoundsInLocal();
		ColorInput colorinput = new ColorInput(bounds.getMinX(),bounds.getMinY(),bounds.getWidth(),bounds.getHeight(),color);
		blend.setTopInput(colorinput);
		blend.setMode(BlendMode.SRC_ATOP);
		imageView.setEffect(blend);
		text.setFill(color);
		for (VDot dot:dots){
			dot.setHighLight(states);
		}
	}


	public void initDots(int number){
		for (int i = 0; i < number; i++) {
			dots.add(new VDot());
			group.getChildren().add(dots.get(i).getShapeGroups());
		}
	}
	public void alignment(){
		step = VConfig.getInstance().getGlobalConfig().get("Step");
		group.setLayoutY(relative*step);
		W = step*2*config.get("scale");
		line.setLayoutY(step);
		line.setEndX(W);
		double dotGap = config.get("dotGap");
		if (dots.size()>0){
			for (int i = 0; i < dots.size(); i++) {
				W += dotGap;
				dots.get(i).getShapeGroups().setLayoutX(W);
				dots.get(i).getShapeGroups().setLayoutY(step-dots.size()/2);
				W += dots.get(i).getW();
			}
		}
	}
	@Override
	public HashMap<String, Double> getConfigAbleList() {
		return null;
	}

	@Override
	public void updateConfig(String id, double value) {
		config.put(id,value);

		switch (id){
			case "offsetX":
				break;
			case "offsetY":
				break;
			case "scale":
				imageView.setFitHeight(step*2*config.get("scale"));
				imageView.setFitWidth(step*2*config.get("scale"));
				text.setFont(new Font(step*2*config.get("scale")*1.3));
				Bounds bounds = text.getBoundsInLocal();
				background.setWidth(bounds.getWidth());
				background.setHeight(bounds.getHeight()*0.75);
				background.setFill(Color.WHITESMOKE);
				background.setLayoutY(-bounds.getHeight()*0.75);
			break;
			case "color":
				break;
		}
	}
}
