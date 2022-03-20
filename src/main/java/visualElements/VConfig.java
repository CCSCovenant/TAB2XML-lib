package visualElements;

import java.util.HashMap;

public class VConfig {
	private static VConfig config = new VConfig();;
	public HashMap<String,HashMap<String,Double>>configMap = new HashMap<>();
	private VConfig(){
		initDefaultConfig();
	}

	private void initDefaultConfig(){
		HashMap<String,Double> globalConfig = new HashMap<>();
		globalConfig.put("pageX",1080d);
		globalConfig.put("pageY",1920d);
		globalConfig.put("MarginX",20d);
		globalConfig.put("MarginY",20d);
		globalConfig.put("step",10d);
		globalConfig.put("minNoteDistance",10d);
		configMap.put("global",globalConfig);

		HashMap<String,Double> measureConfig = new HashMap<>();
		measureConfig.put("noteDistance",10d);
		configMap.put("measure",measureConfig);
		HashMap<String,Double> dotConfig = new HashMap<>();
		configMap.put("dot",dotConfig);
		dotConfig.put("size",1d);
		dotConfig.put("gap_with_last_element",5d);

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
}
