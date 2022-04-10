package system;

import converter.Score;
import custom_exceptions.TXMLException;
import javafx.util.Pair;
import models.Part;
import models.ScorePartwise;
import models.measure.Measure;
import models.measure.note.Note;
import org.jfugue.pattern.Pattern;
import org.jfugue.temporal.TemporalPLP;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.staccato.StaccatoParser;
import player.MXLParser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParserOutputTest {
	static List<String> inputString = new ArrayList<>();

	@BeforeAll
	static void init() throws URISyntaxException, IOException {
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
	}
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
			MXLParser parser = new MXLParser(score.getModel());
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
		for (String s:inputString){
			Score score = new Score(s);
			MXLParser parser = new MXLParser(score.getModel());
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

	@Test
	void DurationTest() throws URISyntaxException, IOException, TXMLException {
		for (String s:inputString){
			Score score = new Score(s);
			ScorePartwise scorePartwise= score.getModel();
			removeChord(scorePartwise);
			MXLParser MXLparser = new MXLParser(scorePartwise);
			Pattern pattern = new Pattern();
			for (Pair<Integer,String> pair: MXLparser.getFullMusicWithRepeat()){
				pattern.add(pair.getValue());
			}
			StaccatoParser parser = new StaccatoParser();
			TemporalPLP plp = new TemporalPLP();
			parser.addParserListener(plp);
			parser.parse(pattern);

			List<Double> FullDuration = new ArrayList<>();
			for (List<Double> duration:MXLparser.getFullDurationsWithRepeat()){
				for (Double d:duration){
					FullDuration.add(d);
				}
			}
			Double actualTiming = 0d;
			int i =0;
			Set<Long> timingSet = plp.getTimeToEventMap().keySet();
			for (Long ExpectedTiming:timingSet){
				Assertions.assertEquals(ExpectedTiming,(long)(actualTiming*500));
				actualTiming += FullDuration.get(i);
				i++;
			}
		}


	}
	public void removeChord(ScorePartwise scorePartwise){
		for (Part part: scorePartwise.getParts()){
			for (Measure measure: part.getMeasures()){
				if (measure.getNotesBeforeBackup()!=null){
					List<Note> notes = measure.getNotesBeforeBackup();
					List<Note> note2remove = new ArrayList<>();
					for (Note note:notes){
						if (note.getChord()!=null){
							note2remove.add(note);
						}
					}
					for (Note note:note2remove){
						notes.remove(note);
					}
				}
			}
		}
	}
}
