package visualElements;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import visualizer.ImageResourceHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VNoteHead extends VElement implements VConfigAble{
	ImageView imageView = new ImageView();
	ImageResourceHandler imageResourceHandler = ImageResourceHandler.getInstance();
	List<VDot> dots = new ArrayList<>();
	HashMap<String,Double> config = VConfig.getInstance().getDefaultConfigMap("VNoteHead");
	double H = 0;
	double W = 0;
	public VNoteHead(String type,int dots){
		imageView.setImage(new Image(imageResourceHandler.getImage(type)));
		imageView.setFitHeight(config.get("StepSize"));
		imageView.setFitWidth(config.get("StepSize"));
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
		W = imageView.getFitWidth();
		H = imageView.getFitHeight();

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
