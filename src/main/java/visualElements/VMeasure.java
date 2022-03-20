package visualElements;

import javafx.scene.Group;
import visualElements.Notations.VGNotation;

import java.util.HashMap;
import java.util.List;

public class VMeasure extends VElement implements VConfigAble {
	private int number;
	private List<VNote> Notes;
	private List<VGNotation> Notations;
	private List<VStaffLine> staffLines;
	private List<VBarline> barlines;
	private HashMap<String,Double> Configs = VConfig.getInstance().getDefaultConfigMap("VMeasure");


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
		return 0;
	}

	@Override
	public double getW() {
		return 0;
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

	public void setStaffLines(List<VStaffLine> staffLines) {
		this.staffLines = staffLines;
	}
	public void alignment(){

	}
}
