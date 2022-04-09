package player;

import converter.Score;
import custom_exceptions.TXMLException;
import javafx.util.Pair;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import visualElements.VConfig;
import visualElements.VMeasure;

import java.util.List;

public class LinkedPlayer {
	List<List<Double>> durations;
	List<List<Double>> fullDurationsWithRepeat;
	PlayMonitor playMonitor;
	List<Pair<Integer,String>> partMusicWithOutRepeat;
	List<Pair<Integer,String>> fullMusicWithRepeat;
	List<Integer> firstPosition;
	List<VMeasure> measures;
	public LinkedPlayer(Score score) throws TXMLException {
		MXLParser parser = new MXLParser(score);
		partMusicWithOutRepeat = parser.getMeasureMapping();
		fullMusicWithRepeat = parser.getFullMusicWithRepeat();
		durations = parser.getDurations();
		fullDurationsWithRepeat =parser.getFullDurationsWithRepeat();
		firstPosition = parser.getFirstPosition();
	}
	public void setVMeasures(List<VMeasure> measures){
		this.measures = measures;
	}
	public void play(int measureNumber){
		int tempo = (int)(double)VConfig.getInstance().getGlobalConfig("tempo");
		boolean shouldRepeat = VConfig.getInstance().getEnableRepeat();
		List<Pair<Integer,String>> musicStringData;
		List<List<Double>> durationData;
		if (shouldRepeat){
			musicStringData = fullMusicWithRepeat;
			durationData = fullDurationsWithRepeat;
		}else {
			musicStringData = partMusicWithOutRepeat;
			durationData = durations;
		}
		playMonitor = new PlayMonitor("Monitor");
		playMonitor.setMeasures(measures);
		playMonitor.setTempo(tempo);
		playMonitor.setDurations(durationData);
		playMonitor.setMusicStrings(musicStringData);

		int startPos = firstPosition.get(measureNumber);

		Pattern pattern = new Pattern();
		for (int i=startPos;i<musicStringData.size();i++){
			pattern.add(musicStringData.get(i).getValue());
		}
		pattern.setTempo(tempo);

		playMonitor.setMeasure(startPos);

		Player player = new Player();

		playMonitor.start();
		player.delayPlay(0,pattern);

	}
}
