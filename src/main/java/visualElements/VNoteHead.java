package visualElements;

import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
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
	HashMap<String,Double> config = new HashMap<>();
	int relative = 0;
	String instrument = "";
	boolean isGrace = false;
	double step = VConfig.getInstance().getGlobalConfig("Step");
	public VNoteHead(String AssetName,int dots,int relative,boolean isGrace){
		instrument = "";
		this.isGrace = isGrace;
		initConfig();
		this.relative = relative;
		List<Integer> staff = VConfig.getInstance().getStaffDetail();
		if (relative<staff.get(0)||relative>staff.get(staff.size()-1)){
			if (Math.abs(relative-staff.get(0))%2==1){
				group.getChildren().add(line);
			}
		}
		imageView.setImage(new Image(imageResourceHandler.getImage(AssetName)));
		group.getChildren().add(background);
		group.getChildren().add(imageView);
		W = group.getBoundsInLocal().getWidth();
		initDots(dots);
	}
	public VNoteHead(int fret,int dots,int relative,boolean isGrace){
		instrument = "TAB";

		this.isGrace = isGrace;
		initConfig();
		this.relative = relative-2; // double-spaced for tab
		text.setText(fret+"");

		group.getChildren().add(background);
		group.getChildren().add(text);
		W = group.getBoundsInLocal().getWidth();
		initDots(dots);
	}
	public void initConfig(){
		config.put("scale",1d);
		config.put("graceScale",0.7d);

		config.put("defaultSize",10d);
		config.put("dotGap",5d);
	}
	public void setFlip(){
		imageView.setRotate(180);
		imageView.setRotationAxis(new Point3D(0,1,0));
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

	public void setGrace(boolean grace) {
		isGrace = grace;
	}

	public void alignment(){
		step = VConfig.getInstance().getGlobalConfig("Step");
		group.setLayoutY(relative*step);
		double s = config.get("scale");
		if (isGrace){
			s = config.get("graceScale");
		}

		if (isGrace){
			imageView.setFitWidth(step*2*s*2);
			Bounds bounds = group.getBoundsInLocal();
			if (instrument.equals("TAB")){

			}else {
				group.setLayoutY(relative*step-bounds.getHeight()/1.8);
			}
		}else {
			imageView.setFitWidth(step*2*s);
		}
		imageView.setPreserveRatio(true);
		Bounds bounds = imageView.getBoundsInLocal();
		background.setWidth(bounds.getWidth());
		background.setHeight(bounds.getHeight());
		background.setFill(Color.TRANSPARENT);


		text.setFont(new Font(step*2*s*1.3));
		Bounds bounds1 = text.getBoundsInLocal();
		background.setWidth(bounds1.getWidth());
		background.setHeight(bounds1.getHeight()*0.75);
		background.setFill(VConfig.getInstance().backGroundColor);
		background.setLayoutY(-bounds1.getHeight()*0.75);


		W = step*2*s;
		line.setLayoutY(step);
		line.setEndX(1.25*W);
		line.setStartX(-0.25*W);
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
	}
}
