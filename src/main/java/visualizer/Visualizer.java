package visualizer;


import converter.Score;
import custom_exceptions.TXMLException;
import javafx.scene.Group;
import models.Part;
import models.ScorePartwise;
import models.measure.Measure;
import visualElements.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class is use for visualize musicXML file.
 *
 * @author Kuimou
 * */
public class Visualizer implements VConfigAble {
	Score score;
	ArrayList<Group> groups = new ArrayList<>();
	ArrayList<VPage> pages = new ArrayList<>();
	HashMap<String, Double> configMap;

	double currentX = 0;
	double currentY = 0;
 	public Visualizer(Score score) throws TXMLException {
		this.score = score;
		configMap = VConfig.getInstance().getDefaultConfigMap("global");
		alignment();
	}

	public ArrayList<Group> getElementGroups(){
		 return groups;
	}
	public void alignment() throws TXMLException {
		configMap = this.getConfigAbleList();
		ScorePartwise scorePartwise = score.getModel();
		VPage tmpPage = new VPage();
		VLine tmpLine = new VLine();
		for (Part part:scorePartwise.getParts()){
			for (Measure measure:part.getMeasures()){

				if (tmpLine.addNewMeasure(getVMeasure(measure))){
						
				}else {
					if (tmpPage.addNewLine(tmpLine)){
					}else {
						pages.add(tmpPage);
						tmpPage = new VPage();
						tmpPage.addNewLine(tmpLine);
					}
					tmpLine.alignment();
					tmpLine = new VLine();
					tmpLine.addNewMeasure(getVMeasure(measure));
				}
			}
		}

	}
	public VMeasure getVMeasure(Measure measure){
		 return null;
	}

	@Override
	public HashMap<String, Double> getConfigAbleList() {
		return null;
	}

	@Override
	public void updateConfig(String id, double value) {

	}
}
