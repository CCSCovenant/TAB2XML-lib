package visualElements.Notations;

import javafx.scene.Group;
import javafx.scene.shape.Line;
import visualElements.VConfig;
import visualElements.VConfigAble;
import visualElements.VElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VGNotation extends VElement implements VConfigAble {
	List<Integer> notes = new ArrayList<>(); // how much note in current notation
	List<String> types = new ArrayList<>(); // the type of note in current notation
	List<Line> Vlines = new ArrayList<>();
	List<Line> Hlines = new ArrayList<>();
	HashMap<String,Double> configMap = VConfig.getInstance().getDefaultConfigMap("gNotation");
	public VGNotation(){

	}
	public int getSize(){
		return notes.size();
	}
	public void addNote(int noteID, String type){
		notes.add(noteID);
		types.add(type);
	}
	public List<Integer> getNotes(){
		return notes;
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


	@Override
	public HashMap<String, Double> getConfigAbleList() {
		return null;
	}

	@Override
	public void updateConfig(String id, double value) {

	}
	public void initElements() {
	}

	public void alignment(List<Double> HPosition,List<Double> VPosition){

	}
}
