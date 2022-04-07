package visualElements;

public class Selected {
	private static Selected selected = new Selected();
	private VElement vElement;
	private Selected(){

	}

	public static Selected getInstance() {
		return selected;
	}

	public void setSElement(VElement sElement) {
		if (sElement!=vElement){
			if (vElement!=null){
				vElement.setHighLight(false);
			}
			vElement = sElement;
			vElement.setHighLight(true);
		}else {
			sElement.setHighLight(false);
			vElement = null;
		}
	}

	public VElement getSElement() {
		return vElement;
	}
}
