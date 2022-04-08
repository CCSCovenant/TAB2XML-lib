package visualElements;

import GUI.Sidebar;
import player.MXLPlayer;
import player.ThreadPlayer;

/**
 * this class is a singleton class
 * that will be call during user select event.
 * when some vElement is selected.
 * GUI will call
 * setSElement(vElement) to highlight that vElement.
 * @author Kuimou Yu
 * */
public class Selected {
	private static Selected selected = new Selected();
	private VElement vElement;
	private Sidebar sidebar;
	private Selected(){

	}

	public static Selected getInstance() {
		return selected;
	}

	public void setSidebar(Sidebar sidebar) {
		this.sidebar = sidebar;
	}

	/**
	 * highlight notes for select states
	 * @param sElement note that you want to highlight during select states
	 *
	 * */
	public void setSElement(VElement sElement) {
		if (sElement!=vElement){
			if (vElement!=null){
				vElement.setHighLight(HighLight.NULL);
			}
			vElement = sElement;
			sidebar.update(vElement);
			if (vElement instanceof VNoteHead){
				ThreadPlayer player = new ThreadPlayer("singleNoteThread");
				String musicString = MXLPlayer.getNoteDetails(((VNoteHead) vElement).getNote());
				System.out.println(musicString);
				player.start(musicString);
			}
			vElement.setHighLight(HighLight.SELECTED);
		}else {
			sElement.setHighLight(HighLight.NULL);
			vElement = null;
		}
	}


	public VElement getSElement() {
		return vElement;
	}
}
