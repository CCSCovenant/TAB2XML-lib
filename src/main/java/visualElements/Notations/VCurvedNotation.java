package visualElements.Notations;

import javafx.scene.shape.QuadCurve;
import visualElements.VConfig;
import visualElements.VConfigAble;
import visualElements.VElement;

import java.util.HashMap;

public class VCurvedNotation extends VElement implements VConfigAble {
	boolean positive;
	public HashMap<String,Double> configMap = new HashMap<>();
	QuadCurve quadCurve = new QuadCurve();
	public VCurvedNotation(){
		group.getChildren().add(quadCurve);
	}
	public void Alignment(){

	}
	public void initConfig(){
		configMap.put("defaultControlPoint", VConfig.getInstance().getGlobalConfig("Step"));
	}
	@Override
	public HashMap<String, Double> getConfigAbleList() {
		return null;
	}

	@Override
	public void updateConfig(String id, double value) {

	}
}
