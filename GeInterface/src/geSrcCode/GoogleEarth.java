package geSrcCode;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class GoogleEarth {
	Process ge;
	Runtime runTime;
	Robot robot;

	// Open Google Earth from local machine
	public void openGE() {
		String x86File = new String("C:\\Program Files (x86)\\Google\\Google Earth\\client\\googleearth.exe");
	    String no86File = new String("C:\\Program Files\\Google\\Google Earth\\client\\googleearth.exe");
	    try {
	        runTime = Runtime.getRuntime();
	            
	        File f = new File(x86File);  // test existence of x86 or not
	        if(f.exists()) {
	             ge = runTime.exec(x86File);
	        } else {
	             ge = runTime.exec(no86File);
	        }
	            
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void openGE(File file){
		try {
			//System.out.println(file);
			if (file == null)
				openGE();
			else
				ge = Runtime.getRuntime().exec( String.format("cmd /c start %s", file) );
		} catch (IOException e1) {
		// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	//	switch to flight simulator mode
	public void flightMode(){
		try {
			robot = new Robot();
			robot.setAutoDelay(1000);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_ALT);
			robot.keyPress(KeyEvent.VK_A);
			robot.keyRelease(KeyEvent.VK_A);
			robot.keyRelease(KeyEvent.VK_ALT);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	// close the currently opened Google Earth
	public void closeGE(){
		ge.destroy();
	}
	
	public Process getProcess(){
		return ge;
	}
	
	/*public static void main(String[] args) {
		// set up Google Earth
		GoogleEarth googleEarth = new GoogleEarth();
		googleEarth.openGE();
		googleEarth.flightMode();
		//System.out.println("check 1");
		
		// set up sound
		SoundThread soundThread = new SoundThread();
		soundThread.start();
		//System.out.println("check 2");
		
		//	wait for Google Earth to be done
		try {
			googleEarth.getProcess().waitFor();
			//System.out.println("check 3");
			soundThread.stopSignal();		
			//System.out.println("check 4");
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		googleEarth.closeGE();
	}*/

}
