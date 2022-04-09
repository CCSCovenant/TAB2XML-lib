package player;

import visualElements.VMeasure;
import visualElements.VNote;

import java.util.List;

public class PlayMonitor extends Thread{
	private Thread t;
	private String threadName;
	List<VMeasure> measures;
	List<List<Long>> durations;
	int measure = 0;
	int note = 0;
	boolean shouldRun;
	public PlayMonitor(List<VMeasure> measures,List<List<Long>> durations){
		this.measures = measures;
		this.durations = durations;
	}
	public void start(){
		if (t==null){
			t = new Thread(this,threadName);
			t.start();
		}
	}
	public void run(){
		while (shouldRun){
			long duration = durations.get(measure).get(note);
			if (playNextNote()){
				try {
					Thread.sleep(duration);
					System.out.println("played");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else {
				break;
			}
		}

	}
	public boolean playNextNote(){
		note++;
		if (note>=measures.get(measure).getNotes().size()){
			measure++;
			if (measure>=measures.size()){
				return false;
			}else {
				VNote vNote = measures.get(measure).getNotes().get(note);
				return true;
			}
		}else {
			VNote vNote = measures.get(measure).getNotes().get(note);
			return true;
		}
	}
}