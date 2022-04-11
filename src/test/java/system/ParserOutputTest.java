package system;

import converter.Score;
import custom_exceptions.TXMLException;
import javafx.util.Pair;
import models.Part;
import models.ScorePartwise;
import models.measure.Measure;
import models.measure.note.*;
import models.measure.note.notations.Notations;
import models.measure.note.notations.Tied;
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
			for (Pair s:parser.getMusicWithoutReapeat()){
				musicString.append(s.getKey()+":"+s.getValue()+"\n");
			}
			Path outFile = outDirPath.resolve(input.getName());
			Files.writeString(outFile,musicString);
		}
	}

	@Test
	void FirstPositionTest() throws TXMLException {
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
	@Test
	void InstrumentConverterTest() throws TXMLException {
		MXLParser parser = new MXLParser(new Score("").getModel());

		Assertions.assertEquals(parser.getInstrument("default"),"GUNSHOT");
		Assertions.assertEquals(parser.getInstrument("P1-I45"),"PEDAL_HI_HAT");
		Assertions.assertEquals(parser.getInstrument("P1-I36"),"BASS_DRUM");

	}
	@Test
	void ChordTestPercussion() throws TXMLException {
		MXLParser parser = new MXLParser(new Score("").getModel());
		List<Note> notes = new ArrayList<>();
		Note note1 = new Note();
		Note note2 = new Note();
		note2.setChord(new Chord());
		Note note3 = new Note();
		note3.setChord(new Chord());

		notes.add(note1);
		notes.add(note2);
		notes.add(note3);

		for (Note note:notes){
			note.setInstrument(new Instrument("P1-I36"));
			note.setType("quarter");
		}
		String expected = "V9 [BASS_DRUM]/0.25+[BASS_DRUM]/0.25+[BASS_DRUM]/0.25 ";
		Assertions.assertEquals(expected,parser.getNoteDetails(notes,"percussion").getKey());
	}
	@Test
	void ChordTestTab() throws TXMLException {
		MXLParser parser = new MXLParser(new Score("").getModel());
		List<Note> notes = new ArrayList<>();
		Note note1 = new Note();
		Note note2 = new Note();
		note2.setChord(new Chord());
		Note note3 = new Note();
		note3.setChord(new Chord());

		notes.add(note1);
		notes.add(note2);
		notes.add(note3);

		for (Note note:notes){
			note.setType("quarter");
			note.setPitch(new Pitch("C",0,4));
		}
		String expected = "V1 I25 C4/0.25+C4/0.25+C4/0.25 ";
		Assertions.assertEquals(expected,parser.getNoteDetails(notes,"TAB").getKey());
	}
	@Test
	void DotTest() throws TXMLException {
		MXLParser parser = new MXLParser(new Score("").getModel());
		List<Note> notes = new ArrayList<>();
		Note note1 = new Note();
		Note note2 = new Note();
		note2.setChord(new Chord());
		Note note3 = new Note();
		note3.setChord(new Chord());

		notes.add(note1);
		notes.add(note2);
		notes.add(note3);
		List<Dot> dots = new ArrayList<>();
		dots.add(new Dot());
		note3.setDots(dots);
		for (Note note:notes){
			note.setType("quarter");
			note.setPitch(new Pitch("C",0,4));
		}
		String expected = "V1 I25 C4/0.25+C4/0.25+C4/0.375 ";
		Assertions.assertEquals(expected,parser.getNoteDetails(notes,"TAB").getKey());
	}
	@Test
	void TiedTest() throws TXMLException {
		MXLParser parser = new MXLParser(new Score("").getModel());
		List<Note> notes = new ArrayList<>();
		Note note1 = new Note();
		Note note2 = new Note();
		note2.setChord(new Chord());
		Note note3 = new Note();
		note3.setChord(new Chord());

		notes.add(note1);
		notes.add(note2);
		notes.add(note3);

		Tied ties = new Tied("start");
		Tied tiee = new Tied("stop");

		List ties1 = new ArrayList();
		List ties2 = new ArrayList();
		List ties3 = new ArrayList();

		ties1.add(ties);
		ties2.add(ties);
		ties2.add(tiee);
		ties3.add(tiee);
		Notations notations1 = new Notations();
		Notations notations2 = new Notations();
		Notations notations3 = new Notations();

		notations1.setTieds(ties1);
		notations2.setTieds(ties2);
		notations3.setTieds(ties3);

		note1.setNotations(notations1);
		note2.setNotations(notations2);
		note3.setNotations(notations3);
		for (Note note:notes){
			note.setType("quarter");
			note.setPitch(new Pitch("C",0,4));
		}
		String expected = "V1 I25 C4/0.25-+C4/-0.25-+C4/-0.25 ";
		Assertions.assertEquals(expected,parser.getNoteDetails(notes,"TAB").getKey());
	}
	@Test
	void NoteTestPercussion() throws TXMLException {
		MXLParser parser = new MXLParser(new Score("").getModel());
		List<Note> notes = new ArrayList<>();
		Note note1 = new Note();
		note1.setInstrument(new Instrument("P1-I45"));
		note1.setType("eighth");
		notes.add(note1);
		String expected = "V9 [PEDAL_HI_HAT]/0.125 ";
		Assertions.assertEquals(expected,parser.getNoteDetails(notes,"percussion").getKey());
	}
	@Test
	void NoteTestTab() throws TXMLException {
		MXLParser parser = new MXLParser(new Score("").getModel());
		List<Note> notes = new ArrayList<>();
		Note note1 = new Note();
		note1.setType("quarter");
		note1.setPitch(new Pitch("C",0,4));
		notes.add(note1);
		String expected = "V1 I25 C4/0.25 ";
		Assertions.assertEquals(expected,parser.getNoteDetails(notes,"TAB").getKey());
	}

	@Test
	void NoteTestPercussionS() throws TXMLException {
		Note note1 = new Note();
		note1.setInstrument(new Instrument("P1-I45"));
		note1.setType("eighth");
		String expected = "V9 [PEDAL_HI_HAT]/0.125 ";
		Assertions.assertEquals(expected,MXLParser.getSingleNote(note1,"percussion"));
	}
	@Test
	void NoteTestTabS() throws TXMLException {
		Note note1 = new Note();
		note1.setType("quarter");
		note1.setPitch(new Pitch("C",0,4));
		String expected = "V1 I25 C4/0.25 ";
		Assertions.assertEquals(expected,MXLParser.getSingleNote(note1,"TAB"));
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
