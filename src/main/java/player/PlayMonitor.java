package player;

import GUI.PreviewViewController;
import javafx.application.Platform;
import javafx.util.Pair;
import visualElements.PlayingSelector;
import visualElements.VMeasure;
import visualElements.VNote;

import java.util.List;

public class PlayMonitor extends Thread{
	private Thread t;
	private String threadName;
	int tempo = 60;//default
	List<VMeasure> measures;
	List<Pair<Integer,String>> MusicStrings;
	List<List<Double>> durations;
	int measure = 0;
	int note = -1;
	boolean shouldPlay = true;
	PreviewViewController controller;
	public PlayMonitor(String name) {
		this.threadName = name;
	}

	public void setController(PreviewViewController controller) {
		this.controller = controller;
	}

	public void setDurations(List<List<Double>> durations) {
		this.durations = durations;
	}
	public void stopPlaying(){
		shouldPlay = false;
	}
	public void setMeasure(int measure) {
		this.measure = measure;
	}

	public void setTempo(int tempo){
		this.tempo = tempo;
	}
	public void setMusicStrings(List<Pair<Integer, String>> musicStrings) {
		MusicStrings = musicStrings;
	}

	public void setMeasures(List<VMeasure> measures) {
		this.measures = measures;
	}

	public void start(){
		int priority = Thread.NORM_PRIORITY + ((Thread.MAX_PRIORITY - Thread.NORM_PRIORITY) * 3) / 4;
		if (t==null){
			t = new Thread(this,threadName);
			t.setPriority(priority);
			t.start();
		}
	}
	public void run(){
		long last = System.currentTimeMillis();
		long GDuration =0;
		while (shouldPlay){
			if (playNextNote()){
				try {
					double duration = durations.get(measure).get(note);
					GDuration += (long) (duration*(60000/tempo));
					long current = System.currentTimeMillis();
					long plan_to_sleep = last+GDuration-current;
					Thread.sleep(plan_to_sleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else {
				break;
			}
		}
		controller.RestPlayButton();
		PlayingSelector.getInstance().setPlayingElement(null);
	}
	public boolean playNextNote(){
		note++;
		if (note>=durations.get(measure).size()){
			measure++;
			if (measure>=durations.size()){
				return false;
			}else {
				note = 0;
				VNote vNote = measures.get(MusicStrings.get(measure).getKey()-1).getNotes().get(note);
				Platform.runLater(() -> {
					controller.goToMeasure(MusicStrings.get(measure).getKey());
				});
				PlayingSelector.getInstance().setPlayingElement(vNote);
				return true;
			}
		}else {
			VNote vNote = measures.get(MusicStrings.get(measure).getKey()-1).getNotes().get(note);

			Platform.runLater(() -> {
				controller.goToMeasure(MusicStrings.get(measure).getKey());
			});
			PlayingSelector.getInstance().setPlayingElement(vNote);
			return true;
		}
	}
}