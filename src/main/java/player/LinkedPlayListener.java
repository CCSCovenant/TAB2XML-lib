package player;

import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.theory.Note;

public class LinkedPlayListener extends ParserListenerAdapter {
	@Override
	public void onNoteParsed(Note note){
		System.out.println("playing");
	}
}
