package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class PreviewConfig {
	private static PreviewConfig config = new PreviewConfig();;
	public HashMap<String,Double> configMap = new HashMap<>();
	public HashMap<String,Double> maxValue = new HashMap<>();
	public HashMap<String,Double> minValue = new HashMap<>();
	public HashMap<String,Double> step = new HashMap<>();
	private PreviewConfig(){
		initDefaultConfig();
	}

	private void initDefaultConfig(){
		configMap.put("measureGap",80d); // *
		maxValue.put("measureGap",120d); // *
		minValue.put("measureGap",80d); // *
		step.put("measureGap",5d);

		configMap.put("noteWidth",8d); // *
		maxValue.put("noteWidth",12d); // *
		minValue.put("noteWidth",8d); // *
		step.put("noteWidth",1d);

		configMap.put("stepSize",4d); //*
		maxValue.put("stepSize",5d); // *
		minValue.put("stepSize",4d); // *
		step.put("stepSize",1d);

		configMap.put("defaultShift",20d); //*
		maxValue.put("defaultShift",30d); // *
		minValue.put("defaultShift",20d); // *
		step.put("defaultShift",5d);
	}

	public static PreviewConfig getInstance() {
		return config;
	}

	public double getDoubleConfig(String id){
		return configMap.get(id);
	}

	public void setConfig(String id, double value){
		if (value>maxValue.get(id)){
			configMap.put(id,maxValue.get(id));
		}else if (value<minValue.get(id)){
			configMap.put(id,minValue.get(id));
		}else {
			configMap.put(id,value);
		}
	}
	public ObservableList<Double> getValues(String id){
		ObservableList<Double> list = FXCollections.observableArrayList();
		for (double i = minValue.get(id);i<=maxValue.get(id);i+=step.get(id)){
			list.add(i);
		}
		return list;
	}
	public ObservableList<String> getConfigList(){
		ObservableList<String> list = FXCollections.observableArrayList();
		for (String s:configMap.keySet()){
			list.add(s);
		}
		return list;
	}
	public int getIntConfig(String id){
		return (int)(double)configMap.get(id);
	}
}
