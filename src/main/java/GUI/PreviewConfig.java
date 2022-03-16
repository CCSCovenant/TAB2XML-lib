package GUI;

import java.util.HashMap;

public class PreviewConfig {
	private static PreviewConfig config = new PreviewConfig();;
	public HashMap<String,Double> configMap = new HashMap<>();
	private PreviewConfig(){
		initDefaultConfig();
	}

	private void initDefaultConfig(){
		configMap.put("measureGap",80d); // *
		configMap.put("noteWidth",8d); // *
		configMap.put("clefWidth",10d);
		configMap.put("timeWidth",10d);
		configMap.put("keyWidth",10d);
		configMap.put("stepSize",4d); //*
		configMap.put("marginX",10d);
		configMap.put("marginY",10d);
		configMap.put("titleSpace",150d);
		configMap.put("eighthGap",4d);
		configMap.put("defaultShift",20d); //*
		configMap.put("bendShift",10d);
	}

	public static PreviewConfig getInstance() {
		return config;
	}

	public double getDoubleConfig(String id){
		return configMap.get(id);
	}

	public int getIntConfig(String id){
		return (int)(double)configMap.get(id);
	}
}
