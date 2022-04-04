package visualElements.Notations;

import javafx.scene.shape.QuadCurve;
import visualElements.VConfig;
import visualElements.VConfigAble;
import visualElements.VElement;

import java.util.HashMap;

public class VCurvedNotation extends VElement implements VConfigAble {
	boolean positive;
	String type;
	public HashMap<String,Double> configMap = new HashMap<>();
	int startID = -2; // measure start
	int endID = -1; // measure end
	QuadCurve quadCurve = new QuadCurve();
	public VCurvedNotation(String type){
		this.type = type;
		group.getChildren().add(quadCurve);
	}
	public void Alignment(double x1,double x2,double y){

	}
	public void setStartID(int i){
		startID = i;
	}
	public void setEndID(int i){
		endID = i;
	}

	public String getType() {
		return type;
	}


	public int getStartID() {
		return startID;
	}

	public int getEndID() {
		return endID;
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
