package player;

import converter.Score;
import custom_exceptions.TXMLException;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import visualElements.VConfig;

import java.util.List;

public class LinkedPlayer {
	List<List<Long>> Durations;
	List<List<Long>> FullDurationsWithRepeat;

	List<String> measureMapping;
	List<String> fullMusicWithRepeat;
	public LinkedPlayer(Score score) throws TXMLException {
		MXLParser parser = new MXLParser(score);
		measureMapping = parser.getMeasureMapping();
		fullMusicWithRepeat = parser.getFullMusicWithRepeat();

	//	Durations = parser.getDurations();
	//	FullDurationsWithRepeat =parser.getFullDurationsWithRepeat();
	}
	public void play(int measureNumber){
		int tempo = (int)(double)VConfig.getInstance().getGlobalConfig("tempo");
		Pattern pattern = new Pattern();
		for (int i=measureNumber;i<measureMapping.size();i++){
			pattern.add(measureMapping.get(i));
			System.out.println(measureMapping.get(i));
		}
		pattern.setTempo(tempo);
		Player player = new Player();
		//PlayMonitor PlayMonitor = new PlayMonitor(measureNumber,Durations);
		player.delayPlay(0,pattern);

	}
}
