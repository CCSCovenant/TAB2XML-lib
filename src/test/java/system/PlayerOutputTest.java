package system;

import converter.Score;
import custom_exceptions.TXMLException;
import org.junit.jupiter.api.Test;
import player.MXLPlayer;
import player.ThreadPlayer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class PlayerOutputTest {
	@Test
	void getSampleString() throws TXMLException, IOException, URISyntaxException {
		URL outDirURL = this.getClass().getClassLoader().getResource("../../resources/test/outputs");
		Path outDirPath = Path.of(outDirURL.toURI());
		File outDir= outDirPath.toFile();
		File[] outputFiles = outDir.listFiles();
		for (File file : outputFiles) file.delete();

		URL inputDirURL = this.getClass().getClassLoader().getResource("../../resources/test/system/");
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

			Score score = new Score(inputEdit.toString());
			MXLPlayer player = new MXLPlayer(score);
			String musicString = player.getString(-1,-1,-1);
			Path outFile = outDirPath.resolve(input.getName());
			Files.writeString(outFile,musicString);
		}
	}
	@Test
	void getSampleOutput() throws IOException, URISyntaxException, TXMLException, InterruptedException {
		URL outDirURL = this.getClass().getClassLoader().getResource("../../resources/test/outputs");
		Path outDirPath = Path.of(outDirURL.toURI());
		File outDir= outDirPath.toFile();
		File[] outputFiles = outDir.listFiles();
		for (File file : outputFiles) file.delete();

		URL inputDirURL = this.getClass().getClassLoader().getResource("../../resources/test/system/");
		Path inputDirPath = Path.of(inputDirURL.toURI());
		File inputDir = inputDirPath.toFile();
		File[] inputFiles = inputDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
		int counter = 0;
		for (File input : inputFiles) {
			counter++;
			String inputText = Files.readString(input.toPath());
			StringBuilder inputEdit = new StringBuilder(inputText);
			for (int i=0;i<inputEdit.length();i++){
				if (inputEdit.charAt(i)=='\r'){
					inputEdit.deleteCharAt(i);
				}
			}

			Score score = new Score(inputEdit.toString());
			MXLPlayer player = new MXLPlayer(score);
			String musicString = player.getString(-1,-1,-1);
			ThreadPlayer player1 = new ThreadPlayer("Thread-"+counter);
			player1.start(musicString);
		}

	}
}
