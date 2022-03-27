package visualElements;

import java.util.HashMap;

public interface VConfigAble {
	abstract HashMap<String,Double> getConfigAbleList( );
	abstract void updateConfig(String id,double value);
}
