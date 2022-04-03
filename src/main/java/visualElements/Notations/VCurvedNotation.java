package visualElements.Notations;

import visualElements.VConfigAble;
import visualElements.VElement;

import java.util.HashMap;

public class VCurvedNotation extends VElement implements VConfigAble {
	boolean positive;
	int startMeasure;
	int startNote;
	int endMeasure;
	int endNote;

	public VCurvedNotation(){

	}
	@Override
	public HashMap<String, Double> getConfigAbleList() {
		return null;
	}

	@Override
	public void updateConfig(String id, double value) {

	}
}
