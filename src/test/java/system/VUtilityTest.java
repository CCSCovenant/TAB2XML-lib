package system;

import models.measure.note.Grace;
import models.measure.note.Note;
import models.measure.note.Notehead;
import models.measure.note.Rest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import visualElements.VUtility;
import visualizer.ImageResourceHandler;

import java.util.ArrayList;
import java.util.List;

public class VUtilityTest {
	static List<Note> restNotes;
	static List<Note> graceNotes;
	static List<Note> normalNotes;
	static List<Note> xNotes;
	static String[] type2 = {"whole","half","quarter","eighth","16th","32nd","64th"};
	@BeforeAll
	static void initDrumNotes(){
		restNotes = new ArrayList<>();
		graceNotes = new ArrayList<>();
		normalNotes = new ArrayList<>();
		xNotes = new ArrayList<>();

		for (int i=0;i<type2.length;i++){
			Note rest = new Note();
			rest.setRest(new Rest());
			rest.setType(type2[i]);
			Note grace = new Note();
			grace.setType(type2[i]);
			grace.setGrace(new Grace());
			Note normal = new Note();
			Notehead normalHead = new Notehead("normal");
			normal.setNotehead(normalHead);
			normal.setType(type2[i]);
			Note x = new Note();
			Notehead xHead = new Notehead("x");
			x.setNotehead(xHead);
			x.setType(type2[i]);
			restNotes.add(rest);
			graceNotes.add(grace);
			normalNotes.add(normal);
			xNotes.add(x);
		}
	}
	@Test
	void DrumAssetTest_Rest(){
		for (int i=0;i<4;i++){
			Note rest = restNotes.get(i);
			String assetName = VUtility.getDrumAssetName(rest);
			//check if we can get right asset from resource handler
			Assertions.assertNotNull(ImageResourceHandler.getInstance().getImage(assetName));
			//check if work as expect
			Assertions.assertEquals(type2[i]+"_rest",assetName);
		}
	}
	@Test
	void DrumAssetTest_Grace(){
		for (int i=0;i<graceNotes.size();i++){
			Note rest = graceNotes.get(i);
			String assetName = VUtility.getDrumAssetName(rest);
			//check if we can get right asset from resource handler
			Assertions.assertNotNull(ImageResourceHandler.getInstance().getImage(assetName));
			//check if work as expect
			if (i==0){
				Assertions.assertEquals("whole_normal",assetName);
			}else {
				Assertions.assertEquals(type2[i]+"_full",assetName);
			}
		}
	}

	@Test
	void DrumAssetTest_Normal(){
		for (int i=0;i<normalNotes.size();i++){
			Note rest = normalNotes.get(i);
			String assetName = VUtility.getDrumAssetName(rest);
			//check if we can get right asset from resource handler
			Assertions.assertNotNull(ImageResourceHandler.getInstance().getImage(assetName));
			//check if work as expect
			if (i==0){
				Assertions.assertEquals("whole_normal",assetName);
			}else if (i==1){
				Assertions.assertEquals("half_normal",assetName);
			}else {
				Assertions.assertEquals("normal",assetName);
			}
		}
	}
	@Test
	void DrumAssetTest_x(){
		for (int i=0;i<xNotes.size();i++){
			Note rest = xNotes.get(i);
			String assetName = VUtility.getDrumAssetName(rest);
			//check if we can get right asset from resource handler
			Assertions.assertNotNull(ImageResourceHandler.getInstance().getImage(assetName));
			//check if work as expect
			if (i==0){
				Assertions.assertEquals("whole_x",assetName);
			}else if (i==1){
				Assertions.assertEquals("half_x",assetName);
			}else {
				Assertions.assertEquals("x",assetName);
			}
		}
	}
	@Test
	void getRelativeTest() {
		String[] steps = {"A","B","C","D","E","F","G","A","B","C","D","E","F","G","A","B"};
		int[] oct = {3,3,4,4,4,4,4,4,4,5,5,5,5,5,5,5};
		int[] relative = {11,10,9,8,7,6,5,4,3,2,1,0,-1,-2,-3,-4};
		for (int i=0;i<steps.length;i++){
			Assertions.assertEquals(relative[i],VUtility.getRelative(steps[i],oct[i]));
		}

	}

}
