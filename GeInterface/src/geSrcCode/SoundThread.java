package geSrcCode;
import java.applet.*;
import java.io.IOException;
import java.net.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundThread extends Thread{
	Clip clip;
	
	/*public void load(){
		try {
	         // Open an audio input stream.
	         URL url = this.getClass().getClassLoader().getResource("chopper.wav");
	         AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
	         // Get a sound clip resource.
	         clip = AudioSystem.getClip();
	         // Open audio clip and load samples from the audio input stream.
	         clip.open(audioIn);
	      } catch (UnsupportedAudioFileException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (LineUnavailableException e) {
	         e.printStackTrace();
	      }
	}*/
	
	 public int load(){
	        try {
	             // Open an audio input stream.
	             URL url = this.getClass().getClassLoader().getResource("chopper.wav");
	             AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
	             // Get a sound clip resource.
	             clip = AudioSystem.getClip();
	             // Open audio clip and load samples from the audio input stream.
	             clip.open(audioIn);
	          } catch (UnsupportedAudioFileException e) {
	             e.printStackTrace();
	             return -1;
	          } catch (IOException e) {
	             e.printStackTrace();
	             return -2;
	          } catch (LineUnavailableException e) {
	             e.printStackTrace();
	             return -3;
	          }
	        return 0;
	    }
	
	public void stopSignal(){
		if (clip != null && clip.isRunning()){
			clip.stop(); 
		}
		this.interrupt();
	}

	public void run() {
		load();
		clip.loop(clip.LOOP_CONTINUOUSLY);
	}

}
