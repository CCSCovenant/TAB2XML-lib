package system;

import converter.Score;
import custom_exceptions.TXMLException;
import org.junit.jupiter.api.Test;
import visualizer.Visualizer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

class VisualizerOutputTest {
	@Test
	void getSampleOutput() throws IOException, URISyntaxException, TXMLException {
		URL outDirURL = this.getClass().getClassLoader().getResource("../../resources/test/outputs/");
		Path outDirPath = Path.of(outDirURL.toURI());
		File outDir = outDirPath.toFile();
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
			Path outPath = outDirPath.resolve(input.getName()+".pdf");
			File outFile = outPath.toFile();
			Visualizer visualizer = new Visualizer(score);

		}

	}
}
