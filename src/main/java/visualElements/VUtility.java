package visualElements;

import models.measure.note.Note;

public class VUtility {
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


	public static String getDrumAssetName(Note note){
		if (note.getRest()!=null){
			return note.getType()+"_rest";
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


}

