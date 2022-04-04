package player;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import org.jfugue.midi.MidiFileManager;
import org.jfugue.midi.MidiParserListener;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.ManagedPlayer;
import org.jfugue.player.Player;
import org.jfugue.player.SequencerManager;
import org.staccato.StaccatoParser;

import models.measure.note.Dot;
import models.measure.note.Note;
import models.measure.note.notations.Slur;
import models.measure.note.notations.Tied;
import models.measure.Measure;
import models.measure.attributes.Attributes;
import models.measure.attributes.Clef;

public class DetailedPlayer {
	private List<Note> musicNotes;
	private String musicString;
	private ManagedPlayer player;
	private Sequence sequence;
	private List<List<Double>> time;
	private int BPM = 120;
	
	public DetailedPlayer(String musicString,List<Note> notes, ManagedPlayer p) {
		this.musicString = musicString;
		setTempo();	
		player = p;
		musicNotes = notes;
		initialize();
	}
	
	public void initialize() {
		sequence = getSequence(musicString);
		time = new ArrayList<>();
		try {
		player.start(sequence);
		player.pause();
		}catch(MidiUnavailableException e) {
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		//player.seek(player.getTickPosition());float t = javax.sound.midi.Sequence.PPQ; System.out.println(t);
		long current = player.getTickPosition();
		long total = player.getTickLength();
		double ratio = current/total;
		
		int end = 0;		//end of slider(will be determined when slider is implemented)
		int beginning = 0;  //start of slider(will be determined when slider is implemented)
		
		long position = (long)(beginning + (end-beginning)*ratio);
//		p.seek(position);
		
		//BPM=120
		//PPQ=128
//		int totaltime = (int) ( ( ((double)60000) / ((double)(128 * 120)) ) * (double)total);
		int totaltime = (int) (sequence.getMicrosecondLength() / 1000);
		double elapsedtime = 0;
		
		int i = 0;
		ListIterator<Note> iterator = musicNotes.listIterator();
	
		while(iterator.hasNext() && i < musicNotes.size()) {
			Note note = musicNotes.get(i);
			double start = elapsedtime;
			elapsedtime += getNoteDuration(note,i);
			double stop = elapsedtime;
			
			time.add(new ArrayList<>());
			time.get(i).add(start);
			time.get(i).add(stop);
			i++;
		}
//		System.out.println("\n" +totaltime);
//		System.out.println(elapsedtime);
//		for(int j = 0; j<time.size(); j++) {
//			System.out.println(time.get(j).get(0));
//			System.out.println(time.get(j).get(1)+"\n");
//		}
	} 
	
	public void play() {
		player.resume();
	}
	private List<Note> tiednotes = new ArrayList<Note>(); private int ch=0;private int tie=0;
	
	public double getNoteDuration(Note note, int position) {
		int BPM = 120;
		double beat = 60000/BPM; // duration of quarter note in milliseconds
		double duration = 0;
		boolean hasChord = note.getChord() != null;
		boolean hasTie =note.getNotations() != null && note.getNotations().getTieds() != null;
		boolean hasSlur = note.getNotations() != null && note.getNotations().getSlurs() != null;
		List<Note> notes = new ArrayList<>();
		
		if(hasTie) {
			tiednotes.add(note);tie++;
			boolean isLastNote = position == musicNotes.size()-1;
			for(Tied tie : note.getNotations().getTieds()) {
				boolean stop = tie != null && tie.getType().equals("stop");
				if(stop && !isLastNote && (musicNotes.get(position+1).getNotations() != null && musicNotes.get(position+1).getNotations().getTieds() == null)) {
					this.tie--;
					duration = timeOf(tiednotes);
					while(this.tie>0) {
						time.get(position-this.tie).set(1, time.get(position-this.tie).get(0)+duration);
						this.tie--;
					}
					tiednotes = new ArrayList<>();
					return duration;
				}
				else if(stop && !isLastNote && musicNotes.get(position+1).getNotations() == null) { 
					this.tie--;
					duration = timeOf(tiednotes);
					while(this.tie>0) {
						time.get(position-this.tie).set(1, time.get(position-this.tie).get(0)+duration);
						this.tie--;
					}
					tiednotes = new ArrayList<>();
					return duration;
				}
				else if(stop && isLastNote) {  
					this.tie--;
					duration = timeOf(tiednotes);
					while(this.tie>0) {
						time.get(position-this.tie).set(1, time.get(position-this.tie).get(0)+duration);
						this.tie--;
					}
					tiednotes = new ArrayList<>();
					return duration;
				}
			}
			return duration;
		}
		else if(hasChord) {duration = getChordDuration(note,position); return duration;}
		else if(position != musicNotes.size()-1 && musicNotes.get(position+1).getChord() != null) {ch++;return 0;}
		else if(hasSlur) {
			boolean sameNote = false;
			for(Slur slur : note.getNotations().getSlurs()) {
				if(slur != null && (slur.getType().equals("start") && !sameNote)) {
					duration = beat * 0.03125;
				}
				if(slur != null && (slur.getType().equals("stop"))) {
					sameNote = true;
					if(note.getNotations().getSlurs().size() <= 1) {
						notes.add(note); 
						duration = timeOf(notes);
					}
				}
				if(slur != null && slur.getType().equals("start") && sameNote) {
					duration = beat * 0.03125;
				}
			}
			return duration;
		}
		else {notes.add(note); duration = timeOf(notes);
//		if(note.getGrace() != null) {
//			duration = beat * 0.25;
//		}else {
//			if(note.getType().equals("whole")) { duration = beat * 4; }
//			else if(note.getType().equals("half")) { duration = beat * 2; }
//			else if(note.getType().equals("quarter")) { duration = beat; }
//			else if(note.getType().equals("eighth")) { duration = beat * 0.5; }
//			else if(note.getType().equals("16th")) { duration = beat * 0.25; }
//			else if(note.getType().equals("32nd")) { duration = beat * 0.125; }
//			else if(note.getType().equals("64th")) { duration = beat * 0.0625; }
//			else if(note.getType().equals("128th")) { duration = beat * 0.03125; }
//			else { duration = beat; }
//		}
//		
//		if(note.getDots() != null) {
//			for(Dot dot : note.getDots()) {
//				if(dot != null) {
//					duration = duration + (duration/2);
//				}
//			}
//		}
//		if(note.getChord() != null) {
//			duration = 0;
//		}
		return duration;
		}
	}

	private List<Note> chordnotes = new ArrayList<Note>();
	
	private double getChordDuration(Note note,int index) {
		
		Note prev = musicNotes.get(index-1);
		if( prev.getChord() == null) { chordnotes.add(prev); }
		
		chordnotes.add(note);ch++;
		
		if(index == musicNotes.size()-1 || musicNotes.get(index+1).getChord() == null) {
			double dur;ch--;
			if(note.getNotations() != null && note.getNotations().getSlurs() != null
					&& note.getNotations().getSlurs().size() > 1 && note.getNotations().getSlurs().get(1).getType().equals("start")) {
				dur = (double)500 * 0.03125;
			}else {
				dur = timeOf(chordnotes);
				while(ch>0) {
					time.get(index-ch).set(1, time.get(index-ch).get(0)+dur);
					ch--;
				}
			}
			chordnotes = new ArrayList<Note>();
			return dur;
		}
		return 0;
	}
	private double timeOf(List<Note> notes) {
		Measure measure = new Measure();
		Attributes a = new Attributes();
		Clef clef;
		if(notes.get(0).getRest() == null && notes.get(0).getUnpitched() != null) {clef = new Clef("percussion",0); }
		else {clef = new Clef("TAB",0); }
		a.setClef(clef); measure.setAttributes(a);
		measure.setNotesBeforeBackup(notes);
		MXLPlayer compose = new MXLPlayer();
		String musicString = compose.getMeasure(measure, "-1", -1);
		Sequence Notes = getSequence(musicString);
		double time = ((double)Notes.getMicrosecondLength())/((double)1000);

		return time;
	}
	
	private Sequence getSequence(String musicString) {
		StaccatoParser staccatoParser = new StaccatoParser();
		MidiParserListener midiParserListener = new MidiParserListener();
		staccatoParser.addParserListener(midiParserListener);
		staccatoParser.parse(musicString);
		Sequence sequence = midiParserListener.getSequence();
		
		return sequence;
	}
	private void setTempo() {
		
//		if(/*BPM is set by user*/) {
//			
//		}else { BPM = 120;}
		
		try {
			Field field = ManagedPlayer.class.getDeclaredField("common");
			field.setAccessible(true);
			
			SequencerManager manager = (SequencerManager)field.get(player);
			Sequencer sequencer = manager.getSequencer();
			sequencer.setTempoInBPM(BPM);
			
		}catch(NoSuchFieldException e) {
			System.out.println("NOT FOUND");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
//	try {
//	File file = new File("C:\\Users\\kidim\\Desktop\\m\\musik.mid");
//	Pattern p = new Pattern(); p.add(musicString); 
//	MidiFileManager.savePatternToMidi(p, file);
//	}catch(IOException e) {
//		
//	}
	
}
