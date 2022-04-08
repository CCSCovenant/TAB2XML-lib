package system;

import converter.Score;
import custom_exceptions.TXMLException;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import visualElements.VConfig;
import visualElements.VLine;
import visualElements.VMeasure;
import visualElements.VPage;
import visualizer.Visualizer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class VisualizerTest extends ApplicationTest {
	static List<Visualizer> visualizers = new ArrayList<>();
	@Override
	public void start(Stage stage)  throws URISyntaxException, IOException, TXMLException {
		URL inputDirURL = VisualizerTest.class.getClassLoader().getResource("../../resources/test/system/");
		Path inputDirPath = Path.of(inputDirURL.toURI());
		File inputDir = inputDirPath.toFile();
		File[] inputFiles = inputDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

		for (File input : inputFiles) {
			String inputText = Files.readString(input.toPath());
			StringBuilder inputEdit = new StringBuilder(inputText);
			for (int i = 0; i < inputEdit.length(); i++) {
				if (inputEdit.charAt(i) == '\r') {
					inputEdit.deleteCharAt(i);
				}
			}
			Score score = new Score(inputEdit.toString());
			Visualizer visualizer = new Visualizer(score);
			visualizers.add(visualizer);
		}
	}
	@Test
	void PageAlignmentCheck(){
		for (Visualizer visualizer:visualizers){
			for (VPage page: visualizer.pages){
				double pageY = VConfig.getInstance().getGlobalConfig("PageY");
				double MarginY = VConfig.getInstance().getGlobalConfig("MarginY");
				double measureDistance = VConfig.getInstance().getGlobalConfig("MeasureDistance");
				double actualY = page.H;
				Assertions.assertTrue(actualY<=(pageY-MarginY+measureDistance));
			}
		}
	}

	@Test
	void LineAlignmentCheck(){
		for (Visualizer visualizer:visualizers){
			for (VPage page: visualizer.pages){
				for (VLine line:page.getLines()){
					double pageX = VConfig.getInstance().getGlobalConfig("PageX");
					double MarginX = VConfig.getInstance().getGlobalConfig("MarginX");
					double actualX = line.getW();
					Assertions.assertTrue(Math.abs(pageX-2*MarginX-actualX)<=10);
				}
			}
		}
	}
	@Test
	void MeasureAlignmentTest(){
		for (Visualizer visualizer:visualizers){
			for (VPage page: visualizer.pages){
				for (VLine line:page.getLines()){
					double W = 0;
					 W += line.vClef.getW();
					for (VMeasure measure:line.getMeasures()){
						Assertions.assertEquals(W,measure.getShapeGroups().getLayoutX());
						W += measure.getW();
					}
				}
			}
		}
	}

}
