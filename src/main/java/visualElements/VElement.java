package visualElements;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;

public class VElement implements VConfigAble {
	public Group group = new Group();
	double W = 0;
	double H = 0;
	HashMap<String,Double> configMap = new HashMap<>();
	public VElement(){
		group.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Selected.getInstance().setSElement(getCurrentElement());
				event.consume();
			}
		});
	}

	public void setHighLight(boolean states){

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

	@Override
	public HashMap<String, Double> getConfigAbleList() {
		return configMap;
	}

	@Override
	public void updateConfig(String id, double value) {
		if (configMap.containsKey(id)){
			configMap.put(id,value);
		}
	}
}
