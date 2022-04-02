package visualElements;

import java.util.HashMap;
import java.util.List;

public class VConfig {
	private static VConfig config = new VConfig();;
	public HashMap<String,HashMap<String,Double>>configMap = new HashMap<>();
	public List<Integer> staffDetail;
	public String instrument;
	private VConfig(){
		initDefaultConfig();
	}

	private void initDefaultConfig(){
		HashMap<String,Double> globalConfig = new HashMap<>();
		globalConfig.put("PageX",1080d);
		globalConfig.put("PageY",1920d);
		globalConfig.put("MarginX",20d);
		globalConfig.put("MarginY",20d);
		globalConfig.put("Step",5d);
		globalConfig.put("MinNoteDistance",20d);
		globalConfig.put("MeasureDistance",150d);

		configMap.put("global",globalConfig);

		HashMap<String,Double> measureConfig = new HashMap<>();
		measureConfig.put("noteDistance",10d);
		measureConfig.put("gapBeforeMeasure",20d);
		measureConfig.put("gapBetweenElement",globalConfig.get("MinNoteDistance"));
		measureConfig.put("gapBetweenGrace",5d);
		configMap.put("measure",measureConfig);
		HashMap<String,Double> dotConfig = new HashMap<>();
		configMap.put("dot",dotConfig);
		dotConfig.put("size",1.5d);
		dotConfig.put("gap_with_last_element",5d);
		HashMap<String,Double> noteConfig = new HashMap<>();
		noteConfig.put("graceOffset",5d);
		configMap.put("note",noteConfig);
		HashMap<String,Double> noteHeadConfig = new HashMap<>();
		noteHeadConfig.put("scale",1d);
		noteHeadConfig.put("defaultSize",10d);
		noteHeadConfig.put("dotGap",5d);
		configMap.put("noteHead",noteHeadConfig);
		HashMap<String,Double> barlineConfig = new HashMap<>();
		barlineConfig.put("distanceBetweenLine",5d);
		configMap.put("barline",barlineConfig);
		HashMap<String,Double> gNotationConfig = new HashMap<>();
		gNotationConfig.put("GuitarNotationStartHeight",100d);
		gNotationConfig.put("GuitarNotationEndHeight",120d);

		gNotationConfig.put("DrumNotationHeight",-30d);

		gNotationConfig.put("notationGap",10d);
		gNotationConfig.put("thickness",5d);
		configMap.put("gNotation",gNotationConfig);

	}

	public static VConfig getInstance() {
		return config;
	}

	public HashMap<String,Double> getDefaultConfigMap(String type){
		HashMap<String,Double> defaultConfig = new HashMap<>();
		HashMap<String,Double> targetConfig = configMap.get(type);
		for (String s:targetConfig.keySet()){
			defaultConfig.put(s,targetConfig.get(s));
		}
		return defaultConfig;
	}
	public HashMap<String,Double> getGlobalConfig(){
		return configMap.get("global");
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
}
