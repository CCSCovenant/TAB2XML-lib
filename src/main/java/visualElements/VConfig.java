package visualElements;

import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public class VConfig implements VConfigAble {
	private static VConfig config = new VConfig();;
	public HashMap<String,Double> globalConfig = new HashMap<>();
	public HashMap<String,Boolean> configAble = new HashMap<>();
	public HashMap<String,Pair<Double, Double>> limitMap = new HashMap<>();
	public HashMap<String,Double> stepMap = new HashMap<>();
	public Color highLightColor;
	public Color defaultColor;
	public Color backGroundColor;
	public Color playColor;
	public List<Integer> staffDetail;
	public String instrument;
	public boolean enableRepeat;
	private VConfig(){
		initDefaultConfig();
	}

	public void initDefaultConfig(){
		backGroundColor = Color.ALICEBLUE;
		highLightColor = Color.BLUEVIOLET;
		defaultColor = Color.BLACK;
		playColor = Color.PALEVIOLETRED;
		enableRepeat = true;
		initConfig("PageX",1224d,0,10000,10,true);
		initConfig("PageY",1584d,0,10000,10,true);
		initConfig("MarginX",20d,0,500,1,true);
		initConfig("MarginY",20d,0,500,1,true);
		initConfig("Step",5d,0,100,1,true);
		initConfig("MinNoteDistance",20d,0,500,1,false);
		initConfig("MeasureDistance",150d,0,500,1,true);
		initConfig("defaultControlPoint",10d,0,500,1,false);
		initConfig("tempo",120d,0,500,1,false);
	}
	public void initConfig(String key,double value){
		initConfig(key,value,0,Double.POSITIVE_INFINITY,1);
	}
	public void initConfig(String key,double value,double lower,double upper,double step){
		initConfig(key,value,lower,upper,1,false);
	}
	public void initConfig(String key,double value,double lower,double upper,double step,boolean canConfig){
		globalConfig.put(key,value);
		limitMap.put(key,new Pair<>(lower,upper));
		stepMap.put(key,step);
		configAble.put(key,canConfig);
	}

	public void setEnableRepeat(boolean enableRepeat) {
		this.enableRepeat = enableRepeat;
	}

	public boolean getEnableRepeat(){
		return enableRepeat;
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

	public Color getPlayColor(){
		return playColor;
	}

	public void setHighLightColor(Color highLightColor) {
		this.highLightColor = highLightColor;
	}

	public void setDefaultColor(Color defaultColor) {
		this.defaultColor = defaultColor;
	}

	@Override
	public HashMap<String, Double> getConfigAbleList() {
		return globalConfig;
	}

	@Override
	public HashMap<String, Pair<Double, Double>> getLimits() {
		return limitMap;
	}

	@Override
	public HashMap<String, Boolean> getConfigAble() {
		return configAble;
	}

	@Override
	public HashMap<String, Double> getStepMap() {
		return stepMap;
	}

	@Override
	public void updateConfig(String id, double value) {
		globalConfig.put(id,value);
	}
}

