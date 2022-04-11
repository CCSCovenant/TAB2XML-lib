package visualElements;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

import java.util.HashMap;

public class VElement implements VConfigAble {
	public Group group = new Group();
	public double W = 0;
	public double H = 0;
	public HashMap<String,Double> configMap = new HashMap<>();
	public HashMap<String, Pair<Double,Double>> limitMap = new HashMap<>();
	public HashMap<String,Boolean> configAble = new HashMap<>();
	public HashMap<String,Double> stepMap = new HashMap<>();
	public HighLight highLight = HighLight.NULL;
	public VElement(){
		group.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				GUISelector.getInstance().setSElement(getCurrentElement());
				if (getCurrentElement() instanceof VNoteHead){

				}
				event.consume();
			}
		});
	}

	public void setHighLight(HighLight states) {

	}
	public Group getShapeGroups(){
		return group;
	}
	public void alignment(){

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
		}else {
		}
	}
}
