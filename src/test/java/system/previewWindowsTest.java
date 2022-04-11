package system;

import GUI.PreviewViewController;
import converter.Score;
import custom_exceptions.TXMLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(ApplicationExtension.class)
public class previewWindowsTest extends ApplicationTest {
	static List<String> inputString = new ArrayList<>();
	static PreviewViewController previewViewController;
	@Override
	public void start(Stage stage) throws IOException, URISyntaxException, TXMLException {
		URL inputDirURL = ParserOutputTest.class.getClassLoader().getResource("../../resources/test/system/");
		assert inputDirURL != null;
		Path inputDirPath = Path.of(inputDirURL.toURI());
		File inputDir = inputDirPath.toFile();
		File[] inputFiles = inputDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
		for (File input : inputFiles) {
			String inputText = Files.readString(input.toPath());
			StringBuilder inputEdit = new StringBuilder(inputText);
			for (int i=0;i<inputEdit.length();i++){
				if (inputEdit.charAt(i)=='\r'){
					inputEdit.deleteCharAt(i);
				}
			}
			inputString.add(inputEdit.toString());
		}
		int testCase = (int)(Math.random()*inputString.size());
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GUI/previewMXL.fxml"));
		Parent root = loader.load();
		stage.setTitle("preview musicXML");
		stage.setResizable(true);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		stage.setMinWidth(1000);
		stage.setMinHeight(700);
		previewViewController = loader.getController();
		previewViewController.updateScore(new Score(inputString.get(testCase)));
		previewViewController.setSceneAndStage(scene,stage);
		previewViewController.update();

	}
	@Test
	void playMusic(FxRobot robot) throws InterruptedException {
		robot.clickOn("#playButton");
		Thread.sleep(1000);
		while (previewViewController.isPlaying){
			Thread.sleep(1000);
		}
	}
	@Test
	void RepeatTest(FxRobot robot) throws InterruptedException {
		robot.clickOn("#repeatButton");
		robot.clickOn("#playButton");
		Thread.sleep(1000);
		while (previewViewController.isPlaying){
			Thread.sleep(1000);
		}
	}
}
