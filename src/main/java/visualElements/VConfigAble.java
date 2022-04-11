package visualElements;

import javafx.util.Pair;

import java.util.HashMap;

public interface VConfigAble {
	abstract HashMap<String,Double> getConfigAbleList( );
	abstract HashMap<String, Pair<Double,Double>> getLimits();
	abstract HashMap<String,Boolean> getConfigAble();
	abstract HashMap<String,Double> getStepMap();
	abstract void updateConfig(String id,double value);
}
