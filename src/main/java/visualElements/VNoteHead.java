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
import models.measure.note.Note;
import models.measure.note.notations.Slur;
import models.measure.note.notations.Tied;
import visualizer.ImageResourceHandler;

import java.util.ArrayList;
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
	boolean flip = false;
	double step = VConfig.getInstance().getGlobalConfig("Step");
	List<Tied> tieds;
	List<Slur> slurs;
	VNote parentNote;
	QuadCurve quadCurve;
	Text bendText;
	ImageView arrow;
	Note note;
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
		if (dots>0){
			initConfigWithDot();
		}
		if (isGrace){
			initConfigWithGrace();
		}
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
		if (isGrace){
			initConfigWithGrace();
		}
	}

	public void setNote(Note note) {
		this.note = note;
	}

	public Note getNote() {
		return note;
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
		initConfigWithBlend();
	}
	public void initConfig(){
		initConfigElement("offsetX",0d,-50,50,1,true);
		initConfigElement("scale",1d,0d,2d,0.1,true);
		initConfigElement("graceScale",0.7d,0d,VConfig.getInstance().getGlobalConfig("PageX"),false);
		initConfigElement("dotGap",5d,0d,VConfig.getInstance().getGlobalConfig("PageX"),false);
		initConfigElement("blendHeight",-20d,-50d,VConfig.getInstance().getGlobalConfig("PageX"),false);
		initConfigElement("blendOffsetX",15d,0d,VConfig.getInstance().getGlobalConfig("PageX"),false);
		initConfigElement("blendTextOffsetY",-3d,-10d,VConfig.getInstance().getGlobalConfig("PageX"),false);
	}
	public void initConfigWithDot(){
		initConfigElement("dotGap",5d,0d,VConfig.getInstance().getGlobalConfig("PageX"),true);
	}
	public void initConfigWithBlend(){
		initConfigElement("blendHeight",-20d,-50d,VConfig.getInstance().getGlobalConfig("PageX"),true);
		initConfigElement("blendOffsetX",15d,0d,VConfig.getInstance().getGlobalConfig("PageX"),true);
		initConfigElement("blendTextOffsetY",-3d,-10d,VConfig.getInstance().getGlobalConfig("PageX"),true);
	}
	public void initConfigWithGrace(){
		initConfigElement("graceScale",0.7d,0.5d,1.0,0.1,true);
		initConfigElement("scale",1d,0d,2d,0.1,false);

	}

	public void setFlip(){
		this.flip = true;
		imageView.setRotate(180);
		imageView.setRotationAxis(new Point3D(0,1,0));
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
		if (flip){
			group.setLayoutX(configMap.get("offsetX")+2*step);
		}else {
			group.setLayoutX(configMap.get("offsetX"));
		}
		group.setLayoutY(relative*step);
		double s = configMap.get("scale");
		if (isGrace){
			s = configMap.get("graceScale");
		}
		imageView.setPreserveRatio(true);
		if (isGrace){

			if (instrument.equals("TAB")){

			}else {
				imageView.setFitWidth(step*2*s*2);
				double height = imageView.getImage().getHeight()/imageView.getImage().getWidth()*step*2*s*2;
				imageView.setLayoutY(-height+step+step*s);
			}
		}else {
			imageView.setFitHeight(step*2*s);
		}
		//alignment text
		if (instrument.equals("TAB")){
			text.setFont(new Font(step*2*s*1.3));
			Bounds bounds1 = text.getBoundsInLocal();
			background.setWidth(bounds1.getWidth());
			background.setHeight(bounds1.getHeight()*0.75);
			background.setFill(VConfig.getInstance().backGroundColor);
			background.setLayoutY(-bounds1.getHeight()*0.75);
		}else {
			Bounds bounds1 = imageView.getBoundsInLocal();
			background.setWidth(bounds1.getWidth());
			background.setHeight(bounds1.getHeight());
			background.setLayoutX(0);
			background.setLayoutX(0);
			background.setFill(new Color(1,1,1,0.001));

		}

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
			arrow.setLayoutX(notePosRight+blendOffsetX-step);
			arrow.setFitWidth(step*2);
			arrow.setFitHeight(step*2);
			arrow.setLayoutY(ajustedY);
			bendText.setLayoutY(ajustedY+textOffset);
			bendText.setLayoutX(notePosRight+blendOffsetX-2*step);
		}

		W = step*2*s;
		line.setLayoutY(step);
		line.setEndX(1.25*W);
		line.setStartX(-0.25*W);
		double dotGap = configMap.get("dotGap");
		if (dots.size()>0){
			for (int i = 0; i < dots.size(); i++) {
				W += dotGap;
				dots.get(i).alignment();
				dots.get(i).getShapeGroups().setLayoutX(W);
				dots.get(i).getShapeGroups().setLayoutY(step-dots.size()/2);
				W += dots.get(i).getW();
			}
		}
		if (GUISelector.getInstance().getSElement()==this){
			setHighLight(HighLight.SELECTED);
		}
	}
	public VNote getParentNote() {
		return parentNote;
	}
}
