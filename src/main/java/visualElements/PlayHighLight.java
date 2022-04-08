package visualElements;
/**
 * this class is a singleton class
 * that will be call during playing music.
 * when some note is playing.
 * player will call
 * setPlayingElement(note) to highlight that note.
 * @author Kuimou Yu
 * */
public class PlayHighLight {
	private static PlayHighLight playing = new PlayHighLight();
	private VElement vElement;
	private PlayHighLight(){

	}

	public static PlayHighLight getInstance() {
		return playing;
	}

	/**
	 * highlight notes for playing states
	 * @param sElement note that you want to highlight during play states
	 *
	 * */
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
