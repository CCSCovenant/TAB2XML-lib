package visualElements;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class VPage extends VElement{
	List<VLine> lines = new ArrayList<>();
	Rectangle background = new Rectangle();
	double MarginY = VConfig.getInstance().getGlobalConfig("MarginY");
	double MarginX = VConfig.getInstance().getGlobalConfig("MarginX");

	double PageX = VConfig.getInstance().getGlobalConfig("PageX");
	double PageY = VConfig.getInstance().getGlobalConfig("PageY");
	double measureDistance = VConfig.getInstance().getGlobalConfig("MeasureDistance");
	public VPage(){
		H = MarginY+measureDistance;
		background.setFill(VConfig.getInstance().backGroundColor);
		background.setWidth(PageX);
		background.setHeight(PageY);
		group.getChildren().add(background);

	}

	@Override
	public void setHighLight(HighLight states) {

	}

	@Override
	public Group getShapeGroups() {
		return group;
	}


	public boolean addNewLine(VLine line){
		line.alignment();
		double lineH = line.getH();
		if (H+lineH>PageY-MarginY){
			if (lines.size()==0){
				//TODO give user error while can't fit a single line into the page
				//which should not happen :(  WHY SOME ONE CONFIG LIKE THIS
				line.getShapeGroups().setLayoutY(H);
				line.getShapeGroups().setLayoutX(MarginX);
				lines.add(line);
				group.getChildren().add(line.getShapeGroups());
				H += line.getH()+measureDistance;
				return true;
			}
			return false;
		}else {
			line.getShapeGroups().setLayoutY(H);
			line.getShapeGroups().setLayoutX(MarginX);
			lines.add(line);
			group.getChildren().add(line.getShapeGroups());
			H += line.getH()+measureDistance;
			return true;
		}
	}


	public List<VLine> getLines() {
		return lines;
	}
}
