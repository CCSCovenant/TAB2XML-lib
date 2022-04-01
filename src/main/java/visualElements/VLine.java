package visualElements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VLine extends VElement{
	List<VMeasure> measures = new ArrayList<>();
	HashMap<String,Double> config = VConfig.getInstance().getDefaultConfigMap("global");
	double MarginX = VConfig.getInstance().getGlobalConfig().get("MarginX");
	double PageW = VConfig.getInstance().getGlobalConfig().get("PageX");
	double gapCount = 0;
	public VLine(){
		W = MarginX;

	}


	@Override
	public void setHighLight(boolean states) {

	}

	public boolean addNewMeasure(VMeasure newMeasure){
		// calculate if new Measure's length will exceed the width of a page
		// if new measure is exceed the page width: return false and adjust rest of measure in order to fit in the page
		// otherwise, add this measure into this line.

			newMeasure.alignment();
			if (W+newMeasure.getW()>PageW-MarginX){
				if (measures.size()==0){
					//TODO give user a warning that this line is squeezed under current setting. please adjust the setting.
					measures.add(newMeasure);
					return true;
				}else {
					return false;
				}
			}else {
				if (measures.size()==0){
					newMeasure.setShowClef(true);
					newMeasure.alignment();
				}else {
					newMeasure.setShowClef(false);
				}
				measures.add(newMeasure);
				gapCount += newMeasure.getGapCount();
				W += newMeasure.getW();
				group.getChildren().add(newMeasure.getShapeGroups());
				return true;
			}

	}
	public void alignment(){
		double idealLengthDiff = PageW-MarginX-W;
		double ideaGapDiff = idealLengthDiff/gapCount;
		//find the gap that fit into the page
		for (VMeasure measure:measures){
			measure.updateConfig("gapBetweenElement",measure.getConfigAbleList().get("gapBetweenElement")+ideaGapDiff);
		}
		System.out.println(W);
		W = 0;
		for (int i=1;i<measures.size();i++){
			measures.get(i-1).alignment();
			W = W+measures.get(i-1).getW();
			measures.get(i).getShapeGroups().setLayoutX(W);
		}
		measures.get(measures.size()-1).alignment();
		W += measures.get(measures.size()-1).getW();
		System.out.println(W);
	}

}
