package visualElements;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import visualizer.ImageResourceHandler;

import java.util.List;

public class VClef extends VSign {
	ImageView imageView = new ImageView();
	ImageResourceHandler imageResourceHandler = ImageResourceHandler.getInstance();

	public VClef(String instrument){
		group.getChildren().add(imageView);
		imageView.setImage(new Image(imageResourceHandler.getImage(instrument)));
	}
	public void alignment(){
		List<Integer> staffDetail = VConfig.getInstance().getStaffDetail();
		double step = VConfig.getInstance().getGlobalConfig().get("Step");
		double start = staffDetail.get(0)*step;
		double end = staffDetail.get(staffDetail.size()-1)*step;
		imageView.setFitHeight(end-start);
		imageView.setFitWidth((end-start)/2);
		W = imageView.getFitWidth();
	}
	public void setVisible(boolean states){
		imageView.setVisible(states);
	}
}
