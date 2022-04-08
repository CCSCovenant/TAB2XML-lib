package visualElements;
/**
 * this class is a singleton class
 * that will be call during playing music.
 * when some note is playing.
 * player will call
 * setPlayingElement(note) to highlight that note.
 * @author Kuimou Yu
 * */
public class PlayingSelector {
	private static PlayingSelector playing = new PlayingSelector();
	private VElement vElement;
	private PlayingSelector(){

	}

	public static PlayingSelector getInstance() {
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
				if (vElement== GUISelector.getInstance().getSElement()){
					vElement.setHighLight(HighLight.SELECTED);
				}else {
					vElement.setHighLight(HighLight.NULL);
				}
			}
			vElement = sElement;
			if (vElement!=null){
				vElement.setHighLight(HighLight.PLAY);
			}
		}else {
			if (sElement!=null){
				if (sElement== GUISelector.getInstance().getSElement()){
					sElement.setHighLight(HighLight.SELECTED);
				}else {
					sElement.setHighLight(HighLight.NULL);
				}
			}
			vElement = null;
		}
	}


	public VElement getSElement() {
		return vElement;
	}
}
