package visualElements;

import models.measure.note.Note;
/**
 * this class contain help methods that help visualizer determine position/type/resource
 * @author Kuimou Yu
 * */
public class VUtility {
	/**
	 * This method get relative position of given step and octave.
	 * relate to E5
	 *  for example. F5-E5 = -1
	 *
	 * @param octave input octave
	 * @param step input step
	 * @return relative position
	 */
	public static int getRelative(String step,int octave){
		//center C is the baseline.
		int centerOctave = 5;
		char stepAdjusted = step.charAt(0);
		char centerStep = 'E';
		if (stepAdjusted<'C'){
			if (stepAdjusted=='A'){
				stepAdjusted = 'H';
			}else if (stepAdjusted=='B'){
				stepAdjusted = 'I';
			}
		}
		return (centerStep-stepAdjusted)-(7*(octave-centerOctave));
	}
	// C4 A B3 C D2 E F1 G A0 B C D E F G

	/**
	 * This method get Asset name from given note.
	 * asset name is a string that program can get image from imageResourceHandler
	 *
	 * @param note Note
	 * @return asset id
	 */
	public static String getDrumAssetName(Note note){
		if (note.getRest()!=null){
			return note.getType()+"_rest";
		}else if (note.getGrace()!=null){
			switch (NoteType2Integer(note.getType())){
				case 1:
					return "whole_normal";
				case 2:
					return "half_full";
				case 4:
					return "quarter_full";
				case 8:
					return "eighth_full";
				case 16:
					return "16th_full";
				case 32:
					return "32nd_full";
				default:
					return "64th_full";
			}
		}else {
			if (note.getNotehead()!=null){
				switch (NoteType2Integer(note.getType())){
					case 1:
						return "whole_"+note.getNotehead().getType();
					case 2:
						return "half_"+note.getNotehead().getType();
					default:
						return note.getNotehead().getType();
				}
			}else {
				switch (NoteType2Integer(note.getType())){
					case 1:
						return "whole_normal";
					case 2:
						return "half_normal";
					default:
						return "normal";
				}
			}
		}
	}
	/**
	 * this method will covert note in string into int duration value.
	 * only return denominator
	 * @param type note type (e.g whole,half,quarter)
	 * @return denominator of duration value
	 * */
	public static int NoteType2Integer(String type){
		switch (type){
			case "whole":
				return 1;
			case "half":
				return 2;
			case "quarter":
				return 4;
			case "eighth":
				return 8;
			case "16th":
				return 16;
			case "32nd":
				return 32;
			case "64th":
				return 64;
			case "128th":
				return 128;
			case "256th":
				return 256;
			case "512th":
				return 512;
			case "1024th":
				return 1024;
			default:
				return -1;
		}
	}
	/**
	 * this method will covert internal string id into display name
	 *
	 * @param id string id
	 * @return display name
	 * */
	public static String getDisplayName(String id){
		if(id.equalsIgnoreCase("gapBetweenGrace")) {
			return "Grace Spacing";
		}
		else if(id.equalsIgnoreCase("gapBeforeMeasure")) {
			return "Measure Spacing (Before)";
		}
		else if(id.equalsIgnoreCase("MinNoteDistance")) {
			return "Minimum Note Spacing";
		}
		else if (id.equalsIgnoreCase("offsetX")) {
			return "Note Head Horizontal Offset";
		}
		else if (id.equalsIgnoreCase("scale")) {
			return "Note Size";
		}
		else if (id.equalsIgnoreCase("dotOffset")) {
			return "Dot Offset";
		}
		else if (id.equalsIgnoreCase("DrumNotationHeight")) {
			return "Drum Notation Height";
		}
		else if (id.equalsIgnoreCase("notationGap")) {
			return "Notation Spacing";
		}
		else if (id.equalsIgnoreCase("thickness")) {
			return "Guitar Notation Thickness";
		}
		else if (id.equalsIgnoreCase("GuitarNotationEndHeight")) {
			return "Guitar Notation Height (End)";
		}
		else if (id.equalsIgnoreCase("GuitarNotationStartHeight")) {
			return "Guitar Notation Height (Start)";
		}
		else if (id.equalsIgnoreCase("dotSize")) {
			return "Dot Size";
		}
		else if(id.equalsIgnoreCase("PageX")) {
			return "Page Width";
		}
		else if(id.equalsIgnoreCase("PageY")) {
			return "Page Height";
		}
		else if(id.equalsIgnoreCase("MarginX")) {
			return "Width Compression";
		}
		else if(id.equalsIgnoreCase("MarginY")) {
			return "Offset from Top";
		}
		else if(id.equalsIgnoreCase("MeasureDistance")) {
			return "Distance b/w Measures";
		}
		return id;
	}
	public static String getHelpDocUrl(){
		return "https://github.com/CCSCovenant/TAB2XML/blob/master/Documents/EECS2311_%20USER%20MANUAL.pdf";
	}


}

