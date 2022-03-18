package visualizer;


import converter.Score;
import custom_exceptions.TXMLException;
import javafx.scene.Group;
import visualElements.VConfigAble;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class is use for visualize musicXML file.
 *
 * @author Kuimou
 * */
public class Visualizer implements VConfigAble {
	Score score;
	ArrayList<Group> groups;
	HashMap<String, Double> configMap;
 	public Visualizer(Score score) throws TXMLException {
		this.score = score;
		alignment();
	}

	public ArrayList<Group> getElementGroups(){
		 return groups;
	}
	public void alignment(){
		configMap = this.getConfigAbleList();


	}

	@Override
	public void updateConfigList(HashMap<String, Double> configs) {

	}

	@Override
	public HashMap<String, Double> getConfigAbleList() {
		return null;
	}

	@Override
	public void updateConfig(String id, double value) {

	}
}
