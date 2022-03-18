package visualElements;

import java.util.HashMap;

public interface VConfigAble {
	abstract void updateConfigList(HashMap<String,Double> configs);
	abstract HashMap<String,Double> getConfigAbleList( );
	abstract void updateConfig(String id,double value);

}
