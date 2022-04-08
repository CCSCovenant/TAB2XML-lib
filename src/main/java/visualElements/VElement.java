package visualElements;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.HashMap;

public class VElement implements VConfigAble {
	public Group group = new Group();
	double W = 0;
	double H = 0;
	public HashMap<String,Double> configMap = new HashMap<>();
	public HashMap<String, Pair<Double,Double>> limitMap = new HashMap<>();
	public HashMap<String,Boolean> configAble = new HashMap<>();
	public HashMap<String,Double> stepMap = new HashMap<>();
	public VElement(){
		group.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Selected.getInstance().setSElement(getCurrentElement());
				if (getCurrentElement() instanceof VNoteHead){

				}
				event.consume();
			}
		});
	}

	public void setHighLight(boolean states){
		Color color;
		if (states){
			color	= VConfig.getInstance().getHighLightColor();
		}else {
			color = VConfig.getInstance().getDefaultColor();
		}
		Blend blend = new Blend();
		Bounds bounds = group.getBoundsInLocal();
		ColorInput colorinput = new ColorInput(bounds.getMinX(),bounds.getMinY(),bounds.getWidth(),bounds.getHeight(),color);
		blend.setTopInput(colorinput);
		blend.setMode(BlendMode.SRC_ATOP);
		group.setEffect(blend);
	}
	public Group getShapeGroups(){
		return group;
	}
	public double getH(){
		return group.getBoundsInLocal().getHeight();
	}
	public double getW(){
		return group.getBoundsInLocal().getWidth();
	}
	public VElement getCurrentElement(){
		return this;
	}
	public void initConfigElement(String id,double initValue,double lower,double upper){
		initConfigElement(id,initValue,lower,upper,true);
	}
	public void initConfigElement(String id,double initValue,double lower,double upper,boolean states){
		initConfigElement(id,initValue,lower,upper,1d,states);
	}
	public void initConfigElement(String id,double initValue,double lower,double upper,double step,boolean states) {
		configMap.put(id,initValue);
		limitMap.put(id,new Pair<>(lower,upper));
		configAble.put(id,states);
		stepMap.put(id,step);

	}


		@Override
	public HashMap<String, Double> getConfigAbleList() {
		return configMap;
	}

	public HashMap<String, Double> getStepMap() {
		return stepMap;
	}

	@Override
	public HashMap<String, Pair<Double, Double>> getLimits() {
		return limitMap;
	}

	public HashMap<String, Boolean> getConfigAble() {
		return configAble;
	}

	@Override
	public void updateConfig(String id, double value) {
		if (configMap.containsKey(id)){
			configMap.put(id,value);
		}
	}
}
