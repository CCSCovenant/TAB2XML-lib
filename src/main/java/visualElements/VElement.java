package visualElements;

import javafx.scene.Group;

public interface VElement {
	abstract void setHighLight(boolean states);
	abstract Group getShapeGroups();
}
