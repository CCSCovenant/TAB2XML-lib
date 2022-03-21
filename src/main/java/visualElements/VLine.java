package visualElements;

import javafx.scene.Group;

import java.util.HashMap;
import java.util.List;

public class VLine extends VElement{
	List<VMeasure> measures;
	HashMap<String,Double> config = VConfig.getInstance().getDefaultConfigMap("global");
	double MarginX = VConfig.getInstance().getGlobalConfig().get("MarginX");
	double PageW = VConfig.getInstance().getGlobalConfig().get("pageX");
	double minGap = VConfig.getInstance().getGlobalConfig().get("minNoteDistance");
	double W = MarginX;
	double gapCount = 0;
	public VLine(){

	}


	@Override
	public void setHighLight(boolean states) {

	}

	@Override
	public Group getShapeGroups() {
		return group;
	}

	@Override
	public double getH() {
		return 0;
	}

	@Override
	public double getW() {
		return 0;
	}


	public boolean addNewMeasure(VMeasure newMeasure){
		// calculate if new Measure's length will exceed the width of a page
		// if new measure is exceed the page width: return false and adjust rest of measure in order to fit in the page
		// otherwise, add this measure into this line.

			newMeasure.alignment();
			if (W+newMeasure.getW()>PageW-MarginX){
				if (measures.size()==0){
					//TODO give user a warning that this line is squeezed under current setting. please adjust the setting.
					double idealLengthDiff = PageW-MarginX-W;
					double ideaGapDiff = idealLengthDiff/gapCount;
					//find the gap that fit into the page
					for (VMeasure measure:measures){
						measure.updateConfig("gapBetweenElement",minGap+ideaGapDiff);
					}
					alignment(); // update measure with new config.
					measures.add(newMeasure);
					return false;
				}else {
					double idealLengthDiff = PageW-MarginX-W;
					double ideaGapDiff = idealLengthDiff/gapCount;
					//find the gap that fit into the page
					for (VMeasure measure:measures){
						measure.updateConfig("gapBetweenElement",minGap+ideaGapDiff);
					}
					alignment(); // update measure with new config.
					return false;
				}
			}else {
				measures.add(newMeasure);
				gapCount += newMeasure.getGapCount();
				return true;
			}

	}
	public void alignment(){
		for (int i=1;i<measures.size();i++){
			measures.get(i-1).alignment();
			measures.get(i).getShapeGroups().setLayoutY(measures.get(i-1).getW());
		}
		measures.get(measures.size()-1).alignment();
	}
}
