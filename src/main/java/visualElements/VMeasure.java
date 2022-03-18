package visualElements;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;

import java.util.List;

public class VMeasure {
	private List<VNote> Notes;
	private List<VNotations> Notations;
	private List<VStaffLine> staffLines;
	private static VConfig config = VConfig.getInstance();
	private double X;
	private double Y;
	private Canvas canvas;
	StackPane stackPane;
	public VMeasure(){

	}
	public void draw(){

	}
	public boolean sizeCheck(){


		return false;
	}
}
