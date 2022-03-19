package visualElements;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.List;

public class VLine extends VElement{
	List<VMeasure> measures;
	boolean dirtySign = false;
	public VLine(){

	}


	@Override
	public void setHighLight(boolean states) {

	}

	@Override
	public Group getShapeGroups() {
		updateGroup();
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

	public void updateGroup(){
		ObservableList<Node> children = group.getChildren();
		children.clear();
		for (VMeasure measure:measures){
			children.add(measure.getShapeGroups());
		}
	}
	public boolean addNewMeasure(VMeasure measure){
		return false;
	}
}
