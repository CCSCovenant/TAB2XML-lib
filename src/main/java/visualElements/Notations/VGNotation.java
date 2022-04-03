package visualElements.Notations;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import visualElements.VConfig;
import visualElements.VConfigAble;
import visualElements.VElement;
import visualElements.VUtility;

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
		Color color;
		if (states){
			color	= VConfig.getInstance().getHighLightColor();
		}else {
			color = VConfig.getInstance().getDefaultColor();
		}
		for (Line line:Vlines){
			line.setStroke(color);
		}
		for (Line line:Hlines){
			line.setStroke(color);
		}

		System.out.println("selected Lines");
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
	public void initElements(){
		int globalHLineNum = 0;
		for (int i =0;i<notes.size();i++){
			Vlines.add(new Line());
			int localHline = (int)(Math.log(VUtility.NoteType2Integer(types.get(i)))/Math.log(2))-2;
			if (localHline>globalHLineNum){
				int diff =  localHline - globalHLineNum;
				globalHLineNum = localHline;
				if (notes.size()==1){
					//TODO add flag for only one note.
				}else {
					for (int j=0;j<diff;j++){
						Line line = new Line();
						line.setStrokeWidth(configMap.get("thickness"));
						Hlines.add(line);
					}
				}
			}else if (localHline<globalHLineNum){
				globalHLineNum = localHline;
			}
		}
		for (Line line:Vlines){
			group.getChildren().add(line);
		}
		for (Line line:Hlines){
			group.getChildren().add(line);
		}
	}

	public void alignment(List<Double> HPosition,List<Double> VPosition){

	}
}
