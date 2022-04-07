package visualElements;

import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import models.measure.note.notations.Slur;
import models.measure.note.notations.Tied;
import visualizer.ImageResourceHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VNoteHead extends VElement{
	ImageView imageView = new ImageView();
	Text text = new Text();
	ImageResourceHandler imageResourceHandler = ImageResourceHandler.getInstance();
	List<VDot> dots = new ArrayList<>();
	Rectangle background = new Rectangle();
	Line line = new Line();
	int relative = 0;
	String instrument = "";
	boolean isGrace = false;
	double step = VConfig.getInstance().getGlobalConfig("Step");
	List<Tied> tieds;
	List<Slur> slurs;
	VNote parentNote;
	QuadCurve quadCurve;
	Text bendText;
	ImageView arrow;
	int dotC = 0;
	public VNoteHead(String AssetName,int dots,int relative,boolean isGrace,VNote parentNote){
		this.parentNote = parentNote;
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
		imageView.setImage(imageResourceHandler.getImage(AssetName));
		group.getChildren().add(background);
		group.getChildren().add(imageView);
		W = group.getBoundsInLocal().getWidth();
		initDots(dots);
	}
	public VNoteHead(int fret,int dots,int relative,boolean isGrace,VNote parentNote){
		this.parentNote = parentNote;
		instrument = "TAB";
		this.isGrace = isGrace;
		initConfig();
		this.relative = relative-2; // double-spaced for tab
		text.setText(fret+"");

		group.getChildren().add(background);
		group.getChildren().add(text);
		W = group.getBoundsInLocal().getWidth();

		dotC = dots;
	}

	public void addBend(double bendAlter){
		bendText = new Text();
		if (bendAlter==2.0){
			bendText.setText("full");
		}else {
			bendText.setText((int)bendAlter+"/2");
		}
		quadCurve = new QuadCurve();
		arrow = new ImageView();
		arrow.setImage(imageResourceHandler.getImage("arrow"));
		group.getChildren().add(arrow);
		group.getChildren().add(quadCurve);
		group.getChildren().add(bendText);
	}
	public void initConfig(){
		initConfigElement("scale",1d,0d,VConfig.getInstance().getGlobalConfig("PageX"));
		initConfigElement("graceScale",0.7d,0d,VConfig.getInstance().getGlobalConfig("PageX"));
		initConfigElement("defaultSize",10d,0d,VConfig.getInstance().getGlobalConfig("PageX"));
		initConfigElement("dotGap",5d,0d,VConfig.getInstance().getGlobalConfig("PageX"));
		initConfigElement("blendHeight",-20d,-50d,VConfig.getInstance().getGlobalConfig("PageX"));
		initConfigElement("blendOffsetX",15d,0d,VConfig.getInstance().getGlobalConfig("PageX"));
		initConfigElement("blendTextOffsetY",-3d,-10d,VConfig.getInstance().getGlobalConfig("PageX"));

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
		if (quadCurve!=null){
			quadCurve.setStroke(color);
			bendText.setFill(color);
			bounds = arrow.getBoundsInLocal();
			colorinput = new ColorInput(bounds.getMinX(),bounds.getMinY(),bounds.getWidth(),bounds.getHeight(),color);
			blend = new Blend();
			blend.setTopInput(colorinput);
			blend.setMode(BlendMode.SRC_ATOP);
			arrow.setEffect(blend);
		}
	}


	public void initDots(int number){
		for (int i = 0; i < number; i++) {
			dots.add(new VDot());
			group.getChildren().add(dots.get(i).getShapeGroups());
		}
	}

	public int getDotC() {
		return dotC;
	}

	public void setGrace(boolean grace) {
		isGrace = grace;
	}

	public void setTieds(List<Tied> tieds) {
		this.tieds = tieds;
	}

	public void setSlurs(List<Slur> slurs) {
		this.slurs = slurs;
	}

	public void alignment(){
		step = VConfig.getInstance().getGlobalConfig("Step");
		group.setLayoutY(relative*step);
		double s = configMap.get("scale");
		if (isGrace){
			s = configMap.get("graceScale");
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

		//alignment text
		text.setFont(new Font(step*2*s*1.3));
		Bounds bounds1 = text.getBoundsInLocal();
		background.setWidth(bounds1.getWidth());
		background.setHeight(bounds1.getHeight()*0.75);
		background.setFill(VConfig.getInstance().backGroundColor);
		background.setLayoutY(-bounds1.getHeight()*0.75);
		//alignment bend
		if (quadCurve!=null){
			double blendHeight = configMap.get("blendHeight");
			double blendOffsetX = configMap.get("blendOffsetX");
			double textOffset = configMap.get("blendTextOffsetY");

			double notePosRight = step*2*s;
			double ajustedY = -step*relative+blendHeight;
			quadCurve.setStartX(notePosRight);
			quadCurve.setEndX(notePosRight+blendOffsetX);
			quadCurve.setStartY(-step);
			quadCurve.setEndY(ajustedY+step);
			quadCurve.setControlX(notePosRight+blendOffsetX);
			quadCurve.setControlY(-step);
			quadCurve.setFill(Color.TRANSPARENT);
			quadCurve.setStroke(Color.BLACK);
			arrow.setLayoutX(step+blendOffsetX);
			arrow.setFitWidth(step*2);
			arrow.setFitHeight(step*2);
			arrow.setLayoutY(ajustedY);
			bendText.setLayoutY(ajustedY+textOffset);
			bendText.setLayoutX(notePosRight+step);
		}

		W = step*2*s;
		line.setLayoutY(step);
		line.setEndX(1.25*W);
		line.setStartX(-0.25*W);
		double dotGap = configMap.get("dotGap");
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


	public VNote getParentNote() {
		return parentNote;
	}
}
