package system;

import converter.Score;
import custom_exceptions.TXMLException;
import models.measure.Measure;
import models.measure.note.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import player.MXLPlayer;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayerInternalTest {
	@Test
	void NoteConverterTest() throws TXMLException {
		MXLPlayer player = new MXLPlayer(new Score(""));

		Note note = new Note();
		Assertions.assertEquals('q', player.getNoteDuration(note));
		note.setType("whole");
		Assertions.assertEquals('w', player.getNoteDuration(note));
		note.setType("half");
		Assertions.assertEquals('h', player.getNoteDuration(note));
		note.setType("quarter");
		Assertions.assertEquals('q', player.getNoteDuration(note));
		note.setType("eighth");
		Assertions.assertEquals('i', player.getNoteDuration(note));
		note.setType("16th");
		Assertions.assertEquals('s', player.getNoteDuration(note));
	}
	@Test
	void InstrumentConverterTest() throws TXMLException {
		MXLPlayer player = new MXLPlayer(new Score(""));

		Assertions.assertEquals("GUNSHOT", player.getInstrument("default"));
		Assertions.assertEquals("PEDAL_HI_HAT", player.getInstrument("P1-I45"));
		Assertions.assertEquals("BASS_DRUM", player.getInstrument("P1-I36"));

	}
	@Test
	void NoteDetailUnpitched() throws TXMLException {
		MXLPlayer player = new MXLPlayer(new Score(""));
		Note note = new Note();
		note.setUnpitched(new Unpitched("C",3));
		Assertions.assertEquals("V9 I25 ", player.getNoteDetails(note));
		note.setUnpitched(new Unpitched("E",3));
		Assertions.assertEquals("V9 I25 ", player.getNoteDetails(note));

	}
	@Test
	void NoteDetailPitched() throws TXMLException {
		MXLPlayer player = new MXLPlayer(new Score(""));
		Note note = new Note();
		note.setPitch(new Pitch("C",1,3));
		note.setVoice(5);
		Assertions.assertEquals("V5 I25 ", player.getNoteDetails(note));
		note.setVoice(7);
		Assertions.assertEquals("V7 I25 ", player.getNoteDetails(note));
	}
	@Test
	void NoteDetailInstrument() throws TXMLException{
		MXLPlayer player = new MXLPlayer(new Score(""));
		Note note = new Note();
		note.setUnpitched(new Unpitched("C",3));
		note.setInstrument(new Instrument("P1-I52"));
		Assertions.assertEquals("V9 [RIDE_CYMBAL_1] ", player.getNoteDetails(note));
		note.setInstrument(null);
		Assertions.assertEquals("V9 I25 ", player.getNoteDetails(note));
	}
	@Test
	void getDotsTest() throws TXMLException {
		MXLPlayer player = new MXLPlayer(new Score(""));
		Note note = new Note();
		List<Dot> dots = new ArrayList<Dot>();
		note.setUnpitched(new Unpitched("C",3));
		Assertions.assertEquals("", player.getDots(note));
		dots.add(new Dot());
		note.setDots(dots);
		Assertions.assertEquals(".", player.getDots(note));
	}

	@Test
	void getStringTest() throws IOException, TXMLException, URISyntaxException {
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
			MXLPlayer player = new MXLPlayer(score);
			String musicString = player.getString(-1,-1,-1);
			Assertions.assertEquals("V1 I25 A3h V1 I25 A3h V1 I25 D4q+A3q V1 I25 C4q V1 I25 F4i V1 I25 C4i V1 I25 A3q ", musicString);
			break;
		}
	}
}
