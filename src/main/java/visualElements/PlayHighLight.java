package visualElements;

public class PlayHighLight {
	private static PlayHighLight playing = new PlayHighLight();
	private VElement vElement;
	private PlayHighLight(){

	}

	public static PlayHighLight getInstance() {
		return playing;
	}

	public void setPlayingElement(VElement sElement) {
		if (sElement!=vElement){
			if (vElement!=null){
				if (vElement==Selected.getInstance().getSElement()){
					vElement.setHighLight(HighLight.SELECTED);
				}else {
					vElement.setHighLight(HighLight.NULL);
				}
			}
			vElement = sElement;
			vElement.setHighLight(HighLight.PLAY);
		}else {
			sElement.setHighLight(HighLight.NULL);
			vElement = null;
		}
	}


	public VElement getSElement() {
		return vElement;
	}
}
