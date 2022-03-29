package visualElements.Signs;

import javafx.scene.text.Text;

public class VTime extends VSign{
	public Text upper;
	public Text lower;
	public VTime(int beats,int value){
		upper = new Text(beats+"");
		lower = new Text(value+"");
	}
}
