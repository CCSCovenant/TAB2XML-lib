package visualElements;

public class VUtility {
	public static int getRelative(String step,int octave){
		//center C is the baseline.
		int centerOctave = 3;
		char stepAdjusted = step.charAt(0);
		char centerStep = 'C';
		if (stepAdjusted<'C'){
			if (stepAdjusted=='A'){
				stepAdjusted = 'H';
			}else if (stepAdjusted=='B'){
				stepAdjusted = 'I';
			}
		}
		return (centerStep-stepAdjusted)-(7*(octave-centerOctave));
	}



}

