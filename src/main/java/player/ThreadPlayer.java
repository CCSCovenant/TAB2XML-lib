package player;

import org.jfugue.player.Player;

public class ThreadPlayer extends Thread {
	private String musicString;
	private Thread t;

	private String threadName;
	public ThreadPlayer(String threadName){
		this.threadName = threadName;
	}
	public void run(){
		Player player = new Player();
		player.play(musicString);
	}
	public void start(String musicString){
		this.musicString = musicString;
		if (t==null){
			t=new Thread(this,threadName);
			t.start();
		}
	}

}
