package system;

import converter.Score;
import custom_exceptions.TXMLException;
import models.measure.note.Instrument;
import models.measure.note.Note;
import models.measure.note.Pitch;
import models.measure.note.Unpitched;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import player.MXLPlayer;

public class PlayerInternalTest {
	@Test
	void NoteConverterTest() throws TXMLException {
		MXLPlayer player = new MXLPlayer(new Score(""));

		Note note = new Note();
		Assertions.assertEquals(player.getNoteDuration(note),'q');
		note.setType("whole");
		Assertions.assertEquals(player.getNoteDuration(note),'w');
		note.setType("half");
		Assertions.assertEquals(player.getNoteDuration(note),'h');
		note.setType("quarter");
		Assertions.assertEquals(player.getNoteDuration(note),'q');
	}
	@Test
	void InstrumentConverterTest() throws TXMLException {
		MXLPlayer player = new MXLPlayer(new Score(""));

		Assertions.assertEquals(player.getInstrument("default"),"GUNSHOT");
		Assertions.assertEquals(player.getInstrument("P1-I45"),"PEDAL_HI_HAT");
		Assertions.assertEquals(player.getInstrument("P1-I36"),"BASS_DRUM");

	}
	@Test
	void NoteDetailUnpitched() throws TXMLException {
		MXLPlayer player = new MXLPlayer(new Score(""));
		Note note = new Note();
		note.setUnpitched(new Unpitched("C",3));
		Assertions.assertEquals(player.getNoteDetails(note),"V9 I25 ");
		note.setUnpitched(new Unpitched("E",3));
		Assertions.assertEquals(player.getNoteDetails(note),"V9 I25 ");

	}
	@Test
	void NoteDetailpitched() throws TXMLException {
		MXLPlayer player = new MXLPlayer(new Score(""));
		Note note = new Note();
		note.setPitch(new Pitch("C",1,3));
		note.setVoice(5);
		Assertions.assertEquals(player.getNoteDetails(note),"V5 I25 ");
		note.setVoice(7);
		Assertions.assertEquals(player.getNoteDetails(note),"V7 I25 ");
	}
	@Test
	void NoteDetailInstrument() throws TXMLException{
		MXLPlayer player = new MXLPlayer(new Score(""));
		Note note = new Note();
		note.setUnpitched(new Unpitched("C",3));
		note.setInstrument(new Instrument("P1-I52"));
		Assertions.assertEquals(player.getNoteDetails(note),"V9 [RIDE_CYMBAL_1] ");
		note.setInstrument(null);
		Assertions.assertEquals(player.getNoteDetails(note),"V9 I25 ");
	}
}
