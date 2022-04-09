package player;

import converter.Score;
import custom_exceptions.TXMLException;
import javafx.util.Pair;
import models.Part;
import models.ScorePartwise;
import models.measure.Measure;
import models.measure.attributes.Time;
import models.measure.barline.BarLine;
import models.measure.note.Dot;
import models.measure.note.Note;
import models.measure.note.notations.Tied;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MXLParser {
	private ScorePartwise score;
	private String clef;
	List<List<Double>> Durations = new ArrayList<>();
	List<List<Double>> FullDurationsWithRepeat = new ArrayList<>();

	List<Pair<Integer,String>> measureMapping = new ArrayList<>();
	List<Pair<Integer,String>> fullMusicWithRepeat = new ArrayList<>();

	List<Integer> firstPosition = new ArrayList<>();
	public MXLParser(Score score) throws TXMLException {
		this.score = score.getModel();
		initStrings();
	}
	public List<Pair<Integer,String>> getMeasureMapping() {
		return measureMapping;
	}
	public void initStrings(){
		measureMapping = new ArrayList<>();
		fullMusicWithRepeat = new ArrayList<>();
		for (Part part:score.getParts()){
			initPart(part,measureMapping,fullMusicWithRepeat);
		}
	}
	public void initPart(Part part,List<Pair<Integer,String>> measureMapping,List<Pair<Integer,String>> fullMusicWithRepeat){
			for (Measure measure:part.getMeasures()){
				StringBuilder musicString = new StringBuilder();
				musicString.append(getMeasure(measure));
				measureMapping.add(new Pair<>(measure.getNumber(),musicString.toString()));
			}
			processWithRepeat(part,fullMusicWithRepeat);
	}

	public void processWithRepeat(Part part,List<Pair<Integer,String>> fullMusicWithRepeat){
		int repeatStart = 0;
		HashMap<Integer,Integer> repeatTime = new HashMap<>();
		for (int i=0;i<part.getMeasures().size();i++){
			Measure measure = part.getMeasures().get(i);
			fullMusicWithRepeat.add(measureMapping.get(i));
			FullDurationsWithRepeat.add(Durations.get(i));

			if (!repeatTime.containsKey(measure.getNumber())){
				firstPosition.add(fullMusicWithRepeat.size()-1);
			}

			if (measure.getBarlines()!=null){
				for (BarLine barLine:measure.getBarlines()){
					if (barLine.getRepeat()!=null){
						if (barLine.getRepeat().getDirection().equals("forward")){
							repeatStart = i-1;
						}else if (barLine.getRepeat().getDirection().equals("backward")){
							int time2repeat = 1;
							if (barLine.getRepeat().getTimes()!=null){
								time2repeat = Integer.parseInt(barLine.getRepeat().getTimes());
							}

							if (repeatTime.containsKey(measure.getNumber())){
								if (repeatTime.get(measure.getNumber())>=time2repeat-1){
									continue;
								}else {
									repeatTime.put(measure.getNumber(),repeatTime.get(measure.getNumber())+1);
								}
							}else {
								repeatTime.put(measure.getNumber(),1);
							}
							i = repeatStart;
						}
					}
				}
			}
		}
	}

	public List<Pair<Integer,String>> getFullMusicWithRepeat() {
		return fullMusicWithRepeat;
	}
	Time time = new Time(4,4);
	public String getMeasure(Measure measure){
		if (measure.getAttributes()!=null&&measure.getAttributes().getClef()!=null){
			clef = measure.getAttributes().getClef().getSign();
		}
		if (measure.getAttributes()!=null&&measure.getAttributes().getTime()!=null){
			time = measure.getAttributes().getTime();
		}
		StringBuilder musicString = new StringBuilder();
		List<List<Note>> Notes = new ArrayList<>();
		if (measure.getNotesBeforeBackup()!=null){
			List<Note> tmp = new ArrayList<>();
			for(Note note: measure.getNotesBeforeBackup()) {
				if (note.getChord()==null){
					if (tmp.size()>0){
						Notes.add(tmp);
					}
					tmp = new ArrayList<>();
				}

				tmp.add(note);
			}
			if (tmp.size()>0){
				Notes.add(tmp);
			}
		}
		List<Double> durationM = new ArrayList<>();
		for (List<Note> noteGroup:Notes){
			Pair<String,Double> notePair = getNoteDetails(noteGroup,clef);
			musicString.append(notePair.getKey());
			durationM.add(notePair.getValue());
		}
		Durations.add(durationM);
		return musicString.toString();
	}
	
	public static Pair<String,Double> getNoteDetails(List<Note> notes, String clef) {
		Time time = new Time(4,4);
		StringBuilder musicString = new StringBuilder();
		String voice = "";
		String chord = "";
		String instrument = "";
		String startTie = "";
		String Duration = "";
		String Alter = "";
		String Dots = "";
		String endTie = "";
		Double Duration_Total = 0d;
		
		//unpitched notes are generally used in music that contain a percussion clef
		//We need to use the appropriate voice for percussive notes (V9)
		if (clef.equals("TAB")){
			voice = "V1 I25";
		}else {
			voice = "V9";
		}
		musicString.append(voice+" ");

		for (Note note:notes){
			if (note.getRest()!=null){
				Duration = "R"+getNoteDurationType(note)+"";
			}else {
				if (note.getChord()!=null){
					chord = "+";
				}
				if (clef.equals("TAB")){
					Duration = note.getPitch().getStep();
					if(note.getPitch().getAlter() != null) {
						if(note.getPitch().getAlter() == 1){ Duration += "#";}
						else if(note.getPitch().getAlter() == -1) {Duration += "#";}
					}
					Duration += note.getPitch().getOctave();
					Duration += getNoteDurationType(note)+"";
				}else {
					Duration = getNoteDurationType(note)+"";
				}
				if (note.getNotations()!=null&&note.getNotations().getTieds()!=null){
					for (Tied tied:note.getNotations().getTieds()){
						if (tied.getType().equals("start")){
							endTie = "-";
						}
						if (tied.getType().equals("stop")){
							startTie = "-";
						}
					}
				}
				if(note.getInstrument() == null || note.getInstrument().getId().equals("")) {

				}//instruments for percussive notes are in the form '[name_of_instrument]'
				else { instrument = "[" + getInstrument(note.getInstrument().getId()) + "]";
				}
			}
			//add dots;
			if (note.getDots()!=null){
				for (Dot dot:note.getDots()){
					Dots +=".";
				}
			}
			musicString.append(chord+""+instrument+""+startTie+""+Duration+Alter+Dots+""+endTie);
			Duration_Total = Math.max(getNoteDuration(note)*time.getBeatType(),Duration_Total);
		}
		musicString.append(" ");
		return new Pair<String,Double>(musicString.toString(),Duration_Total);
	}

	public static String getSingleNote(Note note, String clef) {
		StringBuilder musicString = new StringBuilder();
		String voice = "";
		String chord = "";
		String instrument = "";
		String startTie = "";
		String Duration = "";
		String Alter = "";
		String Dots = "";
		String endTie = "";

		//unpitched notes are generally used in music that contain a percussion clef
		//We need to use the appropriate voice for percussive notes (V9)
		if (clef.equals("TAB")){
			voice = "V1 I25";
		}else {
			voice = "V9";
		}
		musicString.append(voice+" ");

			if (note.getRest()!=null){
				Duration = "R"+getNoteDurationType(note)+"";
			}else {
				if (clef.equals("TAB")){
					Duration = note.getPitch().getStep();
					if(note.getPitch().getAlter() != null) {
						if(note.getPitch().getAlter() == 1){ Duration += "#";}
						else if(note.getPitch().getAlter() == -1) {Duration += "#";}
					}
					Duration += note.getPitch().getOctave();
					Duration += getNoteDurationType(note)+"";
				}else {
					Duration = getNoteDurationType(note)+"";
				}
				if(note.getInstrument() == null || note.getInstrument().getId().equals("")) {

				}//instruments for percussive notes are in the form '[name_of_instrument]'
				else { instrument = "[" + getInstrument(note.getInstrument().getId()) + "]";
				}
			}
			//add dots;
			if (note.getDots()!=null){
				for (Dot dot:note.getDots()){
					Dots +=".";
				}
			}
			musicString.append(chord+""+instrument+""+startTie+""+Duration+Alter+Dots+""+endTie);
		musicString.append(" ");
		return musicString.toString();
	}

	public static char getNoteDurationType(Note note) {
		if (note.getType()!=null){
			if(note.getType().equals("whole")) { return 'w'; }
			else if(note.getType().equals("half")) { return 'h'; }
			else if(note.getType().equals("quarter")) { return 'q'; }
			else if(note.getType().equals("eighth")) { return 'i'; }
			else if(note.getType().equals("16th")) { return 's'; }
			else if(note.getType().equals("32nd")) { return 't'; }
			else if(note.getType().equals("64th")) { return 'x'; }
			else if(note.getType().equals("128th")) { return 'o'; }
			else { return 'q'; }
		} else { return 'q'; }
	}
	public static double getNoteDuration(Note note){
		double duration = 1;
		if (note.getType()!=null){
			if(note.getType().equals("whole")) { duration = 1; }
			else if(note.getType().equals("half")) { duration = 0.5; }
			else if(note.getType().equals("quarter")) { duration = 0.25; }
			else if(note.getType().equals("eighth")) { duration = 0.125; }
			else if(note.getType().equals("16th")) { duration = 0.0625;}
			else if(note.getType().equals("32nd")) { duration = 0.03125; }
			else if(note.getType().equals("64th")) { duration = 0.015625;; }
			else if(note.getType().equals("128th")) { duration = 0.0078125; }
			else { duration = 0.25; }
		} else { duration = 0.25; }
		if (note.getDots()!=null){
			double k = 2;
			for (int i=0;i<note.getDots().size();i++){
				duration += duration * 1/k;
				k*=2;
			}
		}
		return duration;
	}

	public List<List<Double>> getDurations() {
		return Durations;
	}

	public List<List<Double>> getFullDurationsWithRepeat() {
		return FullDurationsWithRepeat;
	}

	public List<Integer> getFirstPosition() {
		return firstPosition;
	}

	public static String getInstrument(String InstrumentId) {
		if(InstrumentId.equals("P1-I47")) { return "OPEN_HI_HAT"; }
		else if(InstrumentId.equals("P1-I52")) { return "RIDE_CYMBAL_1"; }
		else if(InstrumentId.equals("P1-I53")) { return "CHINESE_CYMBAL"; }
		else if(InstrumentId.equals("P1-I43")) { return "CLOSED_HI_HAT"; }
		else if(InstrumentId.equals("P1-I46")) { return "LO_TOM"; }
		else if(InstrumentId.equals("P1-I44")) { return "HIGH_FLOOR_TOM"; }
		else if(InstrumentId.equals("P1-I54")) { return "RIDE_BELL"; }
		else if(InstrumentId.equals("P1-I36")) { return "BASS_DRUM"; }
		else if(InstrumentId.equals("P1-I50")) { return "CRASH_CYMBAL_1"; }
		else if(InstrumentId.equals("P1-I39")) { return "ACOUSTIC_SNARE"; }
		else if(InstrumentId.equals("P1-I42")) { return "LO_FLOOR_TOM"; }
		else if(InstrumentId.equals("P1-I48")) { return "LO_MID_TOM"; }
		else if(InstrumentId.equals("P1-I45")) { return "PEDAL_HI_HAT"; }
		/*More could be added later on*/
		else { return "GUNSHOT"; }//default for now
	}

}
