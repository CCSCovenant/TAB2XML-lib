package visualElements;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import visualizer.ImageResourceHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VNoteHead extends VElement implements VConfigAble{
	ImageView imageView = new ImageView();
	Label label = new Label();
	ImageResourceHandler imageResourceHandler = ImageResourceHandler.getInstance();
	List<VDot> dots = new ArrayList<>();
	HashMap<String,Double> config = VConfig.getInstance().getDefaultConfigMap("noteHead");
	int relative = 0;
	double step = VConfig.getInstance().getGlobalConfig().get("Step");
	double H = 0;
	double W = 0;
	public VNoteHead(String AssetName,int dots,int relative){
		this.relative = relative;
		imageView.setImage(new Image(imageResourceHandler.getImage(AssetName)));
		imageView.setFitHeight(config.get("defaultSize")*config.get("scale"));
		imageView.setFitWidth(config.get("defaultSize")*config.get("scale"));
		group.getChildren().add(imageView);
		initDots(dots);
	}
	public VNoteHead(int fret,int dots,int relative){
		this.relative = relative-2; // double-spaced for tab
 		label.setText(fret+"");
		label.setFont(new Font(config.get("defaultSize")*config.get("scale")));
		group.getChildren().add(label);
		initDots(dots);
	}

	@Override
	public void setHighLight(boolean states) {

		if (states){
			// add color fiter into image view
			
		}
		for (VDot dot:dots){
			dot.setHighLight(states);
		}
	}

	@Override
	public Group getShapeGroups() {
		return group;
	}

	@Override
	public double getH() {
		return H;
	}

	@Override
	public double getW() {
		return W;
	}

	public void initDots(int number){
		for (int i = 0; i < number; i++) {
			dots.add(new VDot());
		}
	}
	public void alignment(){
		step = VConfig.getInstance().getGlobalConfig().get("Step");
		group.setLayoutY(relative*step);
		W = config.get("defaultSize")*config.get("scale");
		if (dots.size()>0){
			for (int i = 0; i < dots.size(); i++) {
				dots.get(i).getShapeGroups().setLayoutX(W);
				W += dots.get(i).getW();
			}
		}

	}
	@Override
	public HashMap<String, Double> getConfigAbleList() {
		return null;
	}

	@Override
	public void updateConfig(String id, double value) {
		switch (id){
			case "offsetX":
				break;
			case "offsetY":
				break;
			case "scale":
				break;
			case "color":
				break;
		}
	}
}
