package visualizer;

public class TieElement {
	double x1;
	double x2;
	double y1;
	double y2;
	String placement;
	boolean half;
	boolean first;
	public TieElement(double x1, double y1){
		this.x1 = x1;
		this.y1 = y1;
		this.half = false;
		this.first = true;
		this.placement = "below";
	}
}
