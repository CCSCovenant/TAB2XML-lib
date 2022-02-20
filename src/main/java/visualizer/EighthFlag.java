package visualizer;

public class EighthFlag {
	int type;
	int x;
	int maxy;
	int miny;

	public EighthFlag(int x){
		this.x = x;
		this.maxy = Integer.MIN_VALUE;
		this.miny = Integer.MAX_VALUE;
		this.type =0;
	}
}
