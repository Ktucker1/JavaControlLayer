
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class GoogleEarth extends JFrame{
	Process ge;
	Runtime runTime;
	Robot robot;

	
	public GoogleEarth(){
		setSize(new Dimension(1600, 900)); //change to aspect ratio of tv's
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu());
		setVisible(true);
	}
	
	private JMenu createFileMenu() {
		JMenu menu = new JMenu("File");
		menu.add(createNewGameItem());
		menu.add(createFileExitItem());
		return menu;
	}

	private JMenuItem createFileExitItem() {
		JMenuItem item = new JMenuItem("Exit");
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}
	
	private JMenuItem createNewGameItem() {
		JMenuItem item = new JMenuItem("New Game");
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				authenticate();
				openGE();
				switchFocus();
				flightMode();
				System.out.println("check 1");
			}
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}	
	// Open Google Earth from local machine
	public void openGE() {
		try {
			runTime = Runtime.getRuntime();
			ge = runTime.exec("C:\\Program Files (x86)\\Google\\Google Earth\\client\\googleearth.exe");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void switchFocus() {
		  try {
		    Robot r = new Robot();
		    r.delay(4000);
		    r.keyPress(KeyEvent.VK_ALT);
		    r.keyPress(KeyEvent.VK_TAB);
		    r.delay(150);
		    r.keyRelease(KeyEvent.VK_ALT);
		    r.keyRelease(KeyEvent.VK_TAB);
		    r.delay(1550);
		  } catch(AWTException e) {
		    // handle
		  }
		}
	//	switch to flight simulator mode
	public void flightMode(){
		try {
			robot = new Robot();
			robot.setAutoDelay(550);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_ALT);
			robot.keyPress(KeyEvent.VK_A);
			robot.delay(150);
			robot.keyRelease(KeyEvent.VK_A);
			robot.keyRelease(KeyEvent.VK_ALT);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.delay(150);
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
	
	//	implementation for login stuffs
	public void authenticate(){
		System.out.println("check11");
	}
	
	public Process getProcess(){
		return ge;
	}
	
	public static void main(String[] args) {
		// set up Google Earth
		GoogleEarth googleEarth = new GoogleEarth();
		// set up sound
		SoundThread soundThread = new SoundThread();
		soundThread.run();
		System.out.println("check 2");
		
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
		
	}

}
