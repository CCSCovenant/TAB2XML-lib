package system;

import converter.Score;
import custom_exceptions.TXMLException;
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import player.MXLParser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

public class ParserOutputTest {
	@Test
	void getSampleString() throws TXMLException, IOException, URISyntaxException {
		URL outDirURL = this.getClass().getClassLoader().getResource("../../resources/test/outputs/");
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
			MXLParser parser = new MXLParser(score);
			StringBuilder musicString = new StringBuilder();
			for (Pair s:parser.getFullMusicWithRepeat()){
				musicString.append(s.getKey()+":"+s.getValue()+"\n");
			}
			musicString.append("=================================");
			for (Pair s:parser.getMeasureMapping()){
				musicString.append(s.getKey()+":"+s.getValue()+"\n");
			}
			Path outFile = outDirPath.resolve(input.getName());
			Files.writeString(outFile,musicString);
		}
	}

	@Test
	void FirstPositionTest() throws URISyntaxException, IOException, TXMLException {
		URL inputDirURL = this.getClass().getClassLoader().getResource("../../resources/test/system/");
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

			Score score = new Score(inputEdit.toString());
			MXLParser parser = new MXLParser(score);
			List<Integer> firstPosition = parser.getFirstPosition();
			List<Pair<Integer,String>> fullMusic = parser.getFullMusicWithRepeat();
			HashSet<Integer> numberSet = new HashSet<>();
			for (int i=0;i<fullMusic.size();i++){
				Pair<Integer,String> p = fullMusic.get(i);
				if (numberSet.contains(p.getKey())){
					Assertions.assertNotEquals(i,firstPosition.get(p.getKey()-1));
				}else {
					Assertions.assertEquals(i,firstPosition.get(p.getKey()-1));
					numberSet.add(p.getKey());
				}
			}
		}
	}
}
