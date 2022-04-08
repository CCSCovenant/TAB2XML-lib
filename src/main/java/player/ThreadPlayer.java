package player;

import org.jfugue.player.Player;

import models.measure.Measure;

import java.util.List;

import org.jfugue.player.ManagedPlayer;

public class ThreadPlayer extends Thread {
	private String musicString;private MXLPlayer p; 
	private boolean isPlaying = false; private DetailedPlayer player;
	private Thread t;

	private String threadName;
	public ThreadPlayer(String threadName){
		this.threadName = threadName;
	}
	public void run(){
		Player player = new Player(); //System.out.println(musicString);
		isPlaying = true;
		/*	player.play(musicString);*/ 
		ManagedPlayer mplayer = player.getManagedPlayer();			
		this.player = new DetailedPlayer(musicString,p.getNotes(),mplayer,p.getMeasure());
	
		this.player.play();
//		while(!mplayer.isFinished()){
//		    if(mplayer.isPaused()){
//		    }else{
//		      int time = (int) ( ( ((double)60000) / ((double)(128 * 120)) ) * (double)mplayer.getTickPosition()); //converts ticks to milliseconds			
//			  int index = 0;
//				for(List<Double> timing : this.player.getTime()){
//						if(timing.get(0)<time && timing.get(1) > time){
//								List<Integer> element = this.player.getElementID(index);
//								/*element.get(0) is measureId ,element.get(1) is noteid */
//								updateVisualElement(element.get(0),element.get(1));
//							}
//						index++;
//					if(time > timing.get(1){ break; }
//		
//		    }
//		   }
		isPlaying = false;
	}
	public void start(String musicString){
		this.musicString = musicString;
		if (t==null){
			t=new Thread(this,threadName);
			t.start();
		}
	}
	
	public void addXMLPlayer(MXLPlayer p) {
		this.p = p;
	}
}
