package visualElements;

import javafx.scene.Group;
import visualElements.Notations.VGNotation;
import visualElements.Signs.VSign;

import java.util.HashMap;
import java.util.List;

public class VMeasure extends VElement implements VConfigAble {
	private int number;
	private List<VNote> Notes;
	private List<VGNotation> Notations;
	private List<VSign> Signs;
	private List<VStaffLine> staffLines;
	private List<VBarline> barlines;
	private HashMap<String,Double> config = VConfig.getInstance().getDefaultConfigMap("VMeasure");
	double W = 0;
	double H = 0;
	double gapCount = 0;
	public VMeasure(){

	}

	@Override
	public HashMap<String, Double> getConfigAbleList() {
		return null;
	}

	@Override
	public void updateConfig(String id, double value) {

	}

	@Override
	public void setHighLight(boolean states) {

	}

	@Override
	public Group getShapeGroups() {
		return null;
	}

	@Override
	public double getH() {
		return H;
	}

	@Override
	public double getW() {
		return W;
	}

	public void setNotes(List<VNote> notes) {
		Notes = notes;
	}

	public void setBarlines(List<VBarline> barlines) {
		this.barlines = barlines;
	}

	public void setNotations(List<VGNotation> notations) {
		Notations = notations;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public double getGapCount(){
		return gapCount;
	}
	public void setStaffLines(List<VStaffLine> staffLines) {
		this.staffLines = staffLines;
	}
	public void alignment(){
		W = 0;
		W += config.get("gapBeforeMeasure");
		gapCount = 0;
		double gapBetweenElement = config.get("gapBetweenElement");
		for (VSign sign:Signs){
			sign.alignment();
			sign.getShapeGroups().setLayoutX(W);
			W += sign.getW();
			W += gapBetweenElement;
			gapCount++;
		}
		for (VNote note:Notes){
			note.alignment();
			note.getShapeGroups().setLayoutX(W);
			W += note.getW();
			W += gapBetweenElement;
			gapCount++;
		}
	}
}
