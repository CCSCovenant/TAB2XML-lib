package visualElements;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class VConfig {
	private static VConfig config = new VConfig();;
	public HashMap<String,Double> configMap = new HashMap<>();
	public HashMap<String,Double> maxValue = new HashMap<>();
	public HashMap<String,Double> minValue = new HashMap<>();
	public HashMap<String,Double> step = new HashMap<>();
	private VConfig(){
		initDefaultConfig();
	}

	private void initDefaultConfig(){
		configMap.put("measureGap",80d); //
		configMap.put("noteWidth",8d); // *
		configMap.put("stepSize",4d); //*
		configMap.put("defaultShift",20d); //*
	}

	public static VConfig getInstance() {
		return config;
	}

	public double getDoubleConfig(String id){
		return configMap.get(id);
	}

	public int getIntConfig(String id){
		return (int)(double)configMap.get(id);
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
}
