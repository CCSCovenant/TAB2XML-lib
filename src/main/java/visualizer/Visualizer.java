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
import java.util.List;

/**
 * This Class is use for visualize musicXML file.
 *
 * @author Kuimou
 * */
public class Visualizer implements VConfigAble {
	Score score;
	ArrayList<Group> groups;
	ArrayList<VPage> pages;
	HashMap<String, Double> configMap;
	List<VMeasure> VMeasures;
	String staffType;
 	public Visualizer(Score score) throws TXMLException {
		this.score = score;
		VMeasures = new ArrayList<>();
		configMap = VConfig.getInstance().getDefaultConfigMap("global");
		setUpInitStaffLine(5,2);
		initMeasures();
		alignment();
		initGroups();
	}

	public ArrayList<Group> getElementGroups(){
		 return groups;
	}

	public void initMeasures() throws TXMLException{
		ScorePartwise scorePartwise = score.getModel();
		staffType = "percussion";
		for (Part part:scorePartwise.getParts()){
			for (Measure measure:part.getMeasures()){
				if (measure.getAttributes()!=null&&measure.getAttributes().getStaffDetails()!=null){
					if (measure.getAttributes().getClef()!=null){
						if (measure.getAttributes().getClef().getSign().equals("TAB")){
							setUpInitStaffLine(measure.getAttributes().getStaffDetails().getStaffLines(),3);
							staffType = "TAB";
						}else {
							setUpInitStaffLine(measure.getAttributes().getStaffDetails().getStaffLines(),2);
							staffType = "percussion";
						}
					}
				}
				VMeasures.add(getVMeasure(measure));
			}
		}
	}
	public void alignment() throws TXMLException {
		pages = new ArrayList<>();
		VPage tmpPage = new VPage();
		VLine tmpLine = new VLine();
		for (VMeasure measure:VMeasures){
			if (tmpLine.addNewMeasure(measure)){ // if this measure can fit into this line, do nothing

			}else {    //if this measure fail to fit into this line
				if (tmpPage.addNewLine(tmpLine)){ //try to add this line into the page. if success, create a new line(in line 79)

				}else { //else create a new page to add current line.
					pages.add(tmpPage);
					tmpPage = new VPage();
					tmpPage.addNewLine(tmpLine);
				}
				tmpLine = new VLine();
				tmpLine.addNewMeasure(measure);
			}
		}
		if (tmpPage.addNewLine(tmpLine)){
			pages.add(tmpPage);
		}else {
			pages.add(tmpPage);
			tmpPage = new VPage();
			tmpPage.addNewLine(tmpLine);
			pages.add(tmpPage);
		}
	}
	public void initGroups(){
		 groups = new ArrayList<>();
		 for (VPage page:pages){
			 groups.add(page.getShapeGroups());
		 }
	}
	public VMeasure getVMeasure(Measure measure){
		VMeasure vMeasure = new VMeasure(measure,staffType,VConfig.getInstance().getStaffDetail());
		return vMeasure;
	}



	public void setUpInitStaffLine(int lines,int gap){
		List<Integer> list= new ArrayList<>();
		for (int i=0;i<lines*gap;i+=gap){
			 list.add(i);
		 }
		VConfig.getInstance().setStaffDetail(list);
	}
	@Override
	public HashMap<String, Double> getConfigAbleList() {
		return null;
	}

	@Override
	public void updateConfig(String id, double value) {

	}
}
