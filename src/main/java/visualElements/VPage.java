package visualElements;

import javafx.scene.Group;

import java.util.ArrayList;
import java.util.List;

public class VPage extends VElement{
	VTitle vTitle;
	List<VLine> lines = new ArrayList<>();
	double MarginY = VConfig.getInstance().getGlobalConfig().get("MarginY");
	double MarginX = VConfig.getInstance().getGlobalConfig().get("MarginX");

	double PageY = VConfig.getInstance().getGlobalConfig().get("PageY");
	double measureDistance = VConfig.getInstance().getGlobalConfig().get("MeasureDistance");
	double H = MarginY+measureDistance;
	public VPage(){

	}

	@Override
	public void setHighLight(boolean states) {
		for (VLine line:lines){
			line.setHighLight(states);
		}
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


	public boolean addNewLine(VLine line){
		if (H+measureDistance>PageY-MarginY){
			return false;
		}else {
			line.getShapeGroups().setLayoutY(H);
			line.getShapeGroups().setLayoutX(MarginX);
			lines.add(line);
			line.alignment();
			group.getChildren().add(line.getShapeGroups());
			H += line.getH()+measureDistance;
			return true;
		}
	}
	// must be called before add line
	public void addVTitle(VTitle vTitle){
		this.vTitle = vTitle;
		H += vTitle.getH();
	}
}
