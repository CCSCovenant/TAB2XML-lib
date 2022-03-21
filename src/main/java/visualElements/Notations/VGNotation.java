package visualElements.Notations;

import javafx.scene.Group;
import visualElements.VConfigAble;
import visualElements.VElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VGNotation extends VElement implements VConfigAble {
	List<Integer> notes = new ArrayList<>(); // how much note in current notation
	List<String> types = new ArrayList<>(); // the type of note in current notation
	public VGNotation(){

	}
	public void addNote(int noteID, String type){
		notes.add(noteID);
		types.add(type);
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


	@Override
	public HashMap<String, Double> getConfigAbleList() {
		return null;
	}

	@Override
	public void updateConfig(String id, double value) {

	}
	public void alignment(List<Double> position){

	}
}
