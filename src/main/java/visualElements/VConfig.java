package visualElements;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.List;

public class VConfig {
	private static VConfig config = new VConfig();;
	public 		HashMap<String,Double> globalConfig = new HashMap<>();
	public Color highLightColor;
	public Color defaultColor;
	public Color backGroundColor;
	public List<Integer> staffDetail;
	public String instrument;
	private VConfig(){
		initDefaultConfig();
	}

	private void initDefaultConfig(){
		backGroundColor = Color.ALICEBLUE;
		highLightColor = Color.BLUEVIOLET;
		defaultColor = Color.BLACK;
		globalConfig.put("PageX",1224d);
		globalConfig.put("PageY",1584d);
		globalConfig.put("MarginX",20d);
		globalConfig.put("MarginY",20d);
		globalConfig.put("Step",5d);
		globalConfig.put("MinNoteDistance",20d);
		globalConfig.put("MeasureDistance",150d);


	}

	public static VConfig getInstance() {
		return config;
	}

	public Double getGlobalConfig(String id){
		return globalConfig.get(id);
	}

	public List<Integer> getStaffDetail() {
		return staffDetail;
	}

	public void setStaffDetail(List<Integer> staffDetail) {
		this.staffDetail = staffDetail;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public String getInstrument() {
		return instrument;
	}

	public Color getHighLightColor() {
		return highLightColor;
	}

	public Color getDefaultColor() {
		return defaultColor;
	}

	public void setHighLightColor(Color highLightColor) {
		this.highLightColor = highLightColor;
	}

	public void setDefaultColor(Color defaultColor) {
		this.defaultColor = defaultColor;
	}
}

