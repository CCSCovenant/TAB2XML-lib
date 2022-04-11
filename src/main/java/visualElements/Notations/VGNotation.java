package visualElements.Notations;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import visualElements.HighLight;
import visualElements.VConfig;
import visualElements.VElement;
import visualElements.VUtility;
import visualizer.ImageResourceHandler;

import java.util.ArrayList;
import java.util.List;
/**
 *
 *
 * */
public class VGNotation extends VElement {
	List<Integer> notes = new ArrayList<>(); // how much note in current notation
	List<String> types = new ArrayList<>(); // the type of note in current notation
	List<Integer> dots = new ArrayList<>();
	List<Line> Vlines = new ArrayList<>();
	List<Line> Hlines = new ArrayList<>();
	List<Circle> circles = new ArrayList<>();
	List<ImageView> imageView = new ArrayList<>();
	public VGNotation(){
	}

	public int getSize(){
		return notes.size();
	}
	public void addNote(int noteID, String type, int dotC){
		notes.add(noteID);
		types.add(type);
		dots.add(dotC);
	}
	public List<Integer> getNotes(){
		return notes;
	}
	@Override
	public void setHighLight(HighLight states) {
		Color color = null;
		highLight = states;
		switch (states){
			case NULL -> color = VConfig.getInstance().getDefaultColor();
			case PLAY -> color = VConfig.getInstance().getPlayColor();
			case SELECTED -> color = VConfig.getInstance().getHighLightColor();
		}
		for (Line line:Vlines){
			line.setStroke(color);
		}
		for (Line line:Hlines){
			line.setStroke(color);
		}
		for (ImageView imageView:this.imageView){
			Blend blend = new Blend();
			Bounds bounds = imageView.getBoundsInLocal();
			ColorInput colorinput = new ColorInput(bounds.getMinX(),bounds.getMinY(),bounds.getWidth(),bounds.getHeight(),color);
			blend.setTopInput(colorinput);
			blend.setMode(BlendMode.SRC_ATOP);
			imageView.setEffect(blend);
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



	public void initElements(){
		int globalHLineNum = 0;
		for (int i =0;i<notes.size();i++){
			if (dots.get(i)>0){
				for (int k=0;k<dots.get(i);k++){
					circles.add(new Circle());
				}
			}
			Vlines.add(new Line());
			int localHline = (int)(Math.log(VUtility.NoteType2Integer(types.get(i)))/Math.log(2))-2;
			if (localHline>globalHLineNum){
				int diff =  localHline - globalHLineNum;
				globalHLineNum = localHline;
				if (notes.size()==1){
					for (int j=0;j<diff;j++){
						ImageView imageView = new ImageView();
						Image image = ImageResourceHandler.getInstance().getImage("eighthFlag");
						imageView.setImage(image);
						this.imageView.add(imageView);
						group.getChildren().add(imageView);
					}
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
		for (Circle circle:circles){
			group.getChildren().add(circle);
		}
		for (Line line:Vlines){
			group.getChildren().add(line);
		}
		for (Line line:Hlines){
			line.setStrokeLineCap(StrokeLineCap.BUTT);
			group.getChildren().add(line);
		}
	}

	public void alignment(List<Double> HPosition,List<Double> VPosition){

	}
}
