package visualElements;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

public class VElement {
	public Group group = new Group();
	public VElement(){
		group.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (Selected.getInstance().getSElement()!=getCurrentElement()){
					Selected.getInstance().getSElement().setHighLight(false);
					Selected.getInstance().setSElement(getCurrentElement());
					setHighLight(true);
					event.consume();
				}else {
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
		return 0;
	}
	public double getW(){
		return 0;
	}
	public VElement getCurrentElement(){
		return this;
	}
}
