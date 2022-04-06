package visualizer;


import converter.Score;
import custom_exceptions.TXMLException;
import javafx.scene.Group;
import javafx.util.Pair;
import models.Part;
import models.ScorePartwise;
import models.measure.Measure;
import visualElements.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This Class is use for visualize musicXML file.
 * Visualizer will read score and output
 *
 * @author Kuimou
 * */
public class Visualizer implements VConfigAble {
	Score score;
	ArrayList<Group> groups;
	ArrayList<VPage> pages;
	List<VMeasure> VMeasures;
	String staffType;
	int measureCounter = 0;

	HashMap<Integer, Pair<Integer,Integer>> measureMapping = new HashMap<>();
 	public Visualizer(Score score) throws TXMLException {
		this.score = score;
		VMeasures = new ArrayList<>();
		setUpInitStaffLine(5,2);
		initMeasures();
		alignment();
	}

	public ArrayList<Group> getElementGroups(){
		 return groups;
	}

	public void initMeasures() throws TXMLException{
		ScorePartwise scorePartwise = score.getModel();
		staffType = "percussion";
		VConfig.getInstance().setInstrument(staffType);
		for (Part part:scorePartwise.getParts()){
			for (Measure measure:part.getMeasures()){
				if (measure.getAttributes()!=null&&measure.getAttributes().getStaffDetails()!=null){
					if (measure.getAttributes().getClef()!=null){
						if (measure.getAttributes().getClef().getSign().equals("TAB")){
							setUpInitStaffLine(measure.getAttributes().getStaffDetails().getStaffLines(),3);
							staffType = "TAB";
							VConfig.getInstance().setInstrument(staffType);
						}else {
							setUpInitStaffLine(measure.getAttributes().getStaffDetails().getStaffLines(),2);
							staffType = "percussion";
							VConfig.getInstance().setInstrument(staffType);
						}
					}
				}
				VMeasures.add(getVMeasure(measure));
			}
		}
		measureCounter = VMeasures.size();
	}
	public void alignment() throws TXMLException {
		pages = new ArrayList<>();
		VPage tmpPage = new VPage();
		VLine tmpLine = new VLine(staffType);

		for (VMeasure measure:VMeasures){
			if (tmpLine.addNewMeasure(measure)){ // if this measure can fit into this line, do nothing

			}else {    //if this measure fail to fit into this line
				tmpLine.initCurvedNotations();
				if (tmpPage.addNewLine(tmpLine)){ //try to add this line into the page. if success, create a new line(in line 79)

				}else { //else create a new page to add current line.
					pages.add(tmpPage);
					tmpPage = new VPage();
					tmpPage.addNewLine(tmpLine);
				}
				tmpLine = new VLine(staffType);
				tmpLine.addNewMeasure(measure);
			}
		}
		tmpLine.initCurvedNotations();
		if (tmpPage.addNewLine(tmpLine)){
			pages.add(tmpPage);
		}else {
			pages.add(tmpPage);
			tmpPage = new VPage();
			tmpPage.addNewLine(tmpLine);
			pages.add(tmpPage);
		}
		createMapping();
		initGroups();
	}
	public void createMapping(){
		measureMapping = new HashMap<>();
		for (int p=0;p<pages.size();p++){
			List<VLine> lines = pages.get(p).getLines();
			for (int l=0;l<lines.size();l++){
				for (VMeasure measure:lines.get(l).getMeasures()){
					int measureID = measure.getNumber();
					measureMapping.put(measureID,new Pair<>(p,l));
				}
			}
		}
	}

	public HashMap<Integer, Pair<Integer,Integer>> getMeasureMapping() {
		return measureMapping;
	}
	/**
	 * get VNote with measure ID and note ID
	 * @param measureID measure id
	 * @param noteID note id
	 * @return noteID th VNote in the given measure
	 * */
	public VNote getNote(int measureID,int noteID){
		 return VMeasures.get(measureID).getNotes().get(noteID);
	}
	public List<VMeasure> getVMeasures() {
		return VMeasures;
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

	public int getMeasureCounter() {
		return measureCounter;
	}
}
