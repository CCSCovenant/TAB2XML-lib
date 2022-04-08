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
				vElement.setHighLight(false);
			}
			vElement = sElement;
			sidebar.update(vElement);
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
