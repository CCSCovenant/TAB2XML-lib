package visualElements.Notations;

import javafx.scene.shape.Line;
import visualElements.VUtility;

import java.util.List;
import java.util.Stack;

public class VDrumGNotation extends VGNotation{
	@Override
	public void initElements(){
		int globalHLineNum = 0;
		for (int i =0;i<notes.size();i++){
			Vlines.add(new Line());
			int localHline = (int)(Math.log(VUtility.NoteType2Integer(types.get(i)))/Math.log(2))-2;
			if (localHline>globalHLineNum){
				int diff =  localHline - globalHLineNum;
				globalHLineNum = localHline;
				for (int j=0;j<diff;j++){
					Line line = new Line();
					line.setStrokeWidth(configMap.get("thickness"));
					Hlines.add(line);
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
	@Override
	public void alignment(List<Double> HPosition, List<Double> VPosition){
		double height = configMap.get("notationHeight");
		double gap = configMap.get("notationGap");
		Stack<Line> lineStack = new Stack<>();
		int globalHLineNum = 0;
		int HlinePointer = 0;
		for (int i =0;i<notes.size();i++){
			int localHline = (int)(Math.log(VUtility.NoteType2Integer(types.get(i)))/Math.log(2))-2;

			Vlines.get(i).setLayoutX(HPosition.get(i));
			Vlines.get(i).setStartY(height);
			Vlines.get(i).setEndY(VPosition.get(i));

			if (localHline>globalHLineNum){
				int diff = localHline - globalHLineNum;
				globalHLineNum = localHline;
				for (int j=0;j<diff;j++){
					lineStack.push(Hlines.get(HlinePointer));
					if (i>0){
						Hlines.get(HlinePointer).setStartX((HPosition.get(i)+HPosition.get(i-1))/2);
						Hlines.get(HlinePointer).setLayoutY(height+(lineStack.size()-1)*gap);
					}else {
						Hlines.get(HlinePointer).setStartX(HPosition.get(i)+configMap.get("thickness")/2);
						Hlines.get(HlinePointer).setLayoutY(height+(lineStack.size()-1)*gap);
					}
					HlinePointer++;
				}
			}else if (localHline<globalHLineNum){
				globalHLineNum = localHline;
				int diff = globalHLineNum - localHline;
				globalHLineNum = localHline;
				for (int j=0;j<diff;j++){
					Line line = lineStack.pop();
					line.setEndX(HPosition.get(i)-configMap.get("thickness"));
				}
			}
		}
		while (!lineStack.isEmpty()){
			Line line = lineStack.pop();
			line.setEndX(HPosition.get(HPosition.size()-1)-configMap.get("thickness")/2);
		}
	}
}
