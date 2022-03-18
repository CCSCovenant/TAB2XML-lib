package visualElements;

import java.util.HashMap;

public class VConfig {
	private static VConfig config = new VConfig();;
	public HashMap<String,HashMap<String,Double>>configMap = new HashMap<>();
	private VConfig(){
		initDefaultConfig();
	}

	private void initDefaultConfig(){

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
