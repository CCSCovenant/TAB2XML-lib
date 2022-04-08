package visualElements;

import GUI.Sidebar;

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

	public void setSElement(VElement sElement) {
		if (sElement!=vElement){
			if (vElement!=null){
				vElement.setHighLight(HighLight.NULL);
			}
			vElement = sElement;
			sidebar.update(vElement);
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
