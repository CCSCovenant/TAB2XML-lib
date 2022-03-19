package visualElements;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.List;

public class VPage extends VElement{
	List<VLine> lines;
	public VPage(){
		group = new Group();
	}

	@Override
	public void setHighLight(boolean states) {
		for (VLine line:lines){
			line.setHighLight(states);
		}
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
		for (VLine line:lines){
			children.add(line.getShapeGroups());
		}
	}
	public boolean addNewLine(VLine line){
		return false;
	}
}
