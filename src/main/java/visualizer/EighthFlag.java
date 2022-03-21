package visualizer;

public class EighthFlag {
	int type;
	double x;
	double maxy;
	double miny;
	boolean isGrace;

	public EighthFlag(double x){
		this.x = x;
		this.maxy = Integer.MIN_VALUE;
		this.miny = Integer.MAX_VALUE;
		this.type =0;
		this.isGrace = false;
	}
}
