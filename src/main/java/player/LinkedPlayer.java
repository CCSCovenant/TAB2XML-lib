package player;

import converter.Score;
import custom_exceptions.TXMLException;
import org.jfugue.devtools.DiagnosticParserListener;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.jfugue.temporal.TemporalPLP;
import org.staccato.StaccatoParser;
import visualElements.VConfig;

import java.util.List;

public class LinkedPlayer {
	List<String> measureMapping;
	public LinkedPlayer(Score score) throws TXMLException {
		MXLParser parser = new MXLParser(score);
		measureMapping = parser.getMeasureMapping();
	}
	public void play(int measureNumber){
		int tempo = (int)(double)VConfig.getInstance().getGlobalConfig("tempo");
		Pattern pattern = new Pattern();
		for (int i=measureNumber;i<measureMapping.size();i++){
			pattern.add(measureMapping.get(i));
		}
		pattern.setTempo(tempo);
		StaccatoParser parser = new StaccatoParser();
		TemporalPLP plp = new TemporalPLP();
		parser.addParserListener(plp);
		parser.parse(pattern);

		DiagnosticParserListener dpl = new DiagnosticParserListener();
		plp.addParserListener(dpl);
		Player player = new Player();
		player.delayPlay(0,pattern);
		plp.parse();

	}
}
