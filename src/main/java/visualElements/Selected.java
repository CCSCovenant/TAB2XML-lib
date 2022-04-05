package visualElements;

public class Selected {
	private static Selected selected = new Selected();
	private VElement vElement;
	private Selected(){

	}

	public static Selected getInstance() {
		return selected;
	}

	public void setSElement(VElement vElement) {
		this.vElement = vElement;

	}

	public VElement getSElement() {
		return vElement;
	}
}
