import java.applet.*;
import java.net.*;

public class SoundThread extends Thread{
	AudioClip helicopterSound;
	boolean stop = false;
	
	public void load(){
		try {
			stop = false;
			helicopterSound = Applet.newAudioClip(new URL("file://C:/Users/Kyle/Music/chopper.wav"));
			helicopterSound.play();
		} catch (MalformedURLException e) {
			System.out.println(e);
		}
	}
	
	public void stopSignal(){
		stop = true;
		this.interrupt();
	}

	public void run() {
		load();
		while(!stop){
			helicopterSound.play();
		}
	}

}
