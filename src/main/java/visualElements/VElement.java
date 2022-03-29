package visualElements;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

public class VElement {
	public Group group = new Group();
	double W = 0;
	double H = 0;
	public VElement(){
		group.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (Selected.getInstance().getSElement()!=getCurrentElement()){
					if (Selected.getInstance().getSElement()!=null){
						Selected.getInstance().getSElement().setHighLight(false);
					}
					Selected.getInstance().setSElement(getCurrentElement());
					System.out.println("Selected new element");
					setHighLight(true);
					event.consume();
				}else {
					System.out.println("Unselected a element");
					Selected.getInstance().setSElement(null);
					setHighLight(false);
					event.consume();
				}
			}
		});
	}

	public void setHighLight(boolean states){

	}
	public Group getShapeGroups(){
		return null;
	}
	public double getH(){
		return W;
	}
	public double getW(){
		return H;
	}
	public VElement getCurrentElement(){
		return this;
	}
}
