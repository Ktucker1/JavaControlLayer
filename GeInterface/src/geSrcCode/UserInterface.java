package geSrcCode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;


public class UserInterface{
	private JTextField username;
	private JTextArea lessonDesc, lessonDescEdit;
	private JPasswordField password; 
	private JButton loginButton;
	private Boolean descriptions = false;
	private Boolean validKML = false;
	private int status;
	private String fileName;
	public JFrame frame = new JFrame();
	public JFrame Disc = new JFrame();
	public JFrame edit = new JFrame();
	public JFrame sysAdmin = new JFrame();
	public JFrame helpFrame = new JFrame();
	
	private JFileChooser fc = new JFileChooser();
	private JCheckBox lesSelect, descRead;
	// private page component
	JLabel pageLabel = new JLabel();
	JButton geButton = new JButton("Explore GoogleEarth");
	JButton assignmentButton = new JButton("Select Assignment");
	JButton lessonDisButton = new JButton("Lesson Description");
	JButton logoutButton = new JButton("Log out");
	JButton exitButton = new JButton("Exit");
	JButton startButton = new JButton("Start Assignment");
	JButton editDescButton = new JButton("Edit Lesson Description");
	JButton enterDescButton = new JButton("Enter new Lesson Description");
	JButton helpButton = new JButton("Help");
	JButton sysAdminButton = new JButton("System Administration");
	File file = null;
	
	public UserInterface(){
		// default setting for interface
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("GeoPod Control System");
		frame.setSize(300, 500);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setLayout(null);
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		//fc.setCurrentDirectory(Location of the 4 folders);
	}

	public void loginLayout(){
		JLabel nameLabel = new JLabel("Username");
		nameLabel.setBounds(30,150,150,20);
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(30,190,150,20);
		username = new JTextField(40);
		username.setBounds(120, 150, 150, 20);
		password = new JPasswordField(20); 
		password.setBounds(120, 190, 150, 20);
		loginButton = new JButton("Login");
		loginButton.addActionListener(new ButtonListener());
		loginButton.setBounds(80,240, 150, 30);
		frame.add(nameLabel);
		frame.add(username);
		frame.add(passwordLabel);
		frame.add(password);
		frame.add(loginButton);
	}
	
	private class ButtonListener implements ActionListener{
		
		//Login function
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == loginButton){
				// check username/password combo
				String user = username.getText();
				String pass = password.getText();
				UserDatabase database = new UserDatabase();
				status = database.authorize(user, pass);
				if (status == 0){
					JOptionPane.showMessageDialog(null, "Invalid username.");
				} else if (status == 1){
					JOptionPane.showMessageDialog(null, "Invalid password.");
				} else if (status == 2){
					//JOptionPane.showMessageDialog(null, "You are a student.");
					eraseLogin();
					studentLayout();
					
				} else {
					//JOptionPane.showMessageDialog(null, "You are a teacher.");
					eraseLogin();
					teacherLayout();
				}
			}
			
			//Logout button
			if (e.getSource() == logoutButton){
				frame.dispose();
			}
			
			//Help button
			if(e.getSource() == helpButton){
				helpFrame.setTitle("Program Help");
				helpFrame.setVisible(true);
				helpFrame.setSize(300, 500);
				helpFrame.setLocationRelativeTo(null);
				helpFrame.setLayout(null);
				sysAdminButton.setSize(startButton.getHeight(),startButton.getWidth());
				sysAdminButton.setBounds(45,100,200,20);
				sysAdminButton.addActionListener(new ButtonListener());
				exitButton.setSize(startButton.getHeight(),startButton.getWidth());
				exitButton.setBounds(70,150,150,20);
				exitButton.addActionListener(new ButtonListener());
				helpFrame.add(sysAdminButton);
				helpFrame.add(exitButton);
			}
			
			//Exit button
			if (e.getSource() == exitButton){
				Disc.dispose();
				edit.dispose();
				sysAdmin.dispose();
				helpFrame.dispose();
			}
			
			//Start button
			if (e.getSource() == startButton && (lesSelect.isSelected() == true) && (descRead.isSelected() == true)){
				GoogleEarth googleEarth = new GoogleEarth();
				googleEarth.openGE(file);
				//googleEarth.flightMode();
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
					
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				googleEarth.closeGE();
			}else if (e.getSource() == startButton && ((lesSelect.isSelected() == true) && (descRead.isSelected() == false))){
				JOptionPane select = new JOptionPane();
				JOptionPane.showMessageDialog(null,"Please read the description of the lesson plan before starting the simultor", "Error incomplete pre-lesson plan", 0);
			}else if(e.getSource() == startButton && ((lesSelect.isSelected() == false) && (descRead.isSelected() == false))){
				JOptionPane select = new JOptionPane();
				JOptionPane.showMessageDialog(null,"Please select your current lesson by clicking on the Select Assignment button.", "Error incomplete pre-lesson plan", 0);
			}
			
			//Lesson Description button
			if(e.getSource() == lessonDisButton && (lesSelect.isSelected() == true)){
				Disc.setTitle("Lesson Description");
				Disc.setVisible(true);
				Disc.setSize(300, 500);
				Disc.setLocationRelativeTo(null);
				Disc.setLayout(null);
				
				try {
					descRead.setSelected(true);
					lessonReader();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else if (e.getSource() == lessonDisButton && (lesSelect.isSelected() == false)){
				JOptionPane select = new JOptionPane();
				JOptionPane.showMessageDialog(null,"Please select your current lesson before reading it's description by clicking on the Select Assignment button.", "Error incomplete pre-lesson plan", 0);
			}
			
			
			//Assignment Button
			if (e.getSource() == assignmentButton){
				int returnVal = fc.showOpenDialog(assignmentButton);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
	                file = fc.getSelectedFile();
	                setFile(file);
	                /*
	                try {
						Desktop.getDesktop().open(file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}*/
	               // try {
	                	lesSelect.setSelected(true);
	                	//System.out.println(file);
	                	//Runtime.getRuntime().exec( String.format("cmd /c start %s", file) );
					//} catch (IOException e1) {
						// TODO Auto-generated catch block
					//	e1.printStackTrace();
					//}
				}
			}
			
			//Explore Google Earth button
			if (e.getSource() == geButton){
				GoogleEarth googleEarth = new GoogleEarth();
				googleEarth.openGE();
				//googleEarth.flightMode();
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
					
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				googleEarth.closeGE();				
			}
			
			//Edit Description Button
			if(e.getSource() == editDescButton  && (lesSelect.isSelected() == true)){
				edit.setTitle("Edit Lesson Description");
				edit.setVisible(true);
				edit.setSize(300, 500);
				edit.setLocationRelativeTo(null);
				edit.setLayout(null);
				lessonDescEdit = new JTextArea(300,0);
				lessonDescEdit.setBounds(40, 50, 200, 300);
				lessonDescEdit.setLineWrap(true);
				lessonDescEdit.setWrapStyleWord(true);
				JLabel lessLabel = new JLabel("Please enter the lesson description");
				lessLabel.setBounds(40, 0, 240, 80);
				enterDescButton.setSize(startButton.getHeight(),startButton.getWidth());
				enterDescButton.setBounds(70,375,150,20);
				enterDescButton.addActionListener(new ButtonListener());
				exitButton.setSize(startButton.getHeight(),startButton.getWidth());
				exitButton.setBounds(70,400,150,20);
				exitButton.addActionListener(new ButtonListener());
				edit.add(lessonDescEdit);
				edit.add(exitButton);
				edit.add(lessLabel);
				edit.add(enterDescButton);
				checkFileType();
			}
			//NEW
			if(e.getSource() == editDescButton  && (lesSelect.isSelected() == false)){
				JOptionPane select = new JOptionPane();
				JOptionPane.showMessageDialog(null,"Please select your current lesson before editing it's description by clicking on the Select Assignment button.", "Error incomplete lesson selection", 0);
			}
				
			//Enter Description button
			if(e.getSource() == enterDescButton && (validKML == true)){
				String description =lessonDescEdit.getText();
				System.out.println(description);
				FileWriter fstream;
				try {
					fstream = new FileWriter(file, true);
				    BufferedWriter out = new BufferedWriter(fstream);
					out.write("<!--" + description + "-->");
					out.newLine();
					out.close();
					validKML = false;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("ERROR");
				}
			}
			
			//System Admin button
			if(e.getSource() == sysAdminButton) {
				Border  blackline, raisedetched, loweredetched, raisedbevel, loweredbevel, empty;
				blackline = BorderFactory.createLineBorder(Color.black);
				raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
				loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
				raisedbevel = BorderFactory.createRaisedBevelBorder();
				loweredbevel = BorderFactory.createLoweredBevelBorder();
				empty = BorderFactory.createEmptyBorder();
				
				int winX = 555, winY = 500,displayX = 100, displayY = 175, startY = 10;

				sysAdmin.setTitle("System Administration");
				sysAdmin.setVisible(true);
				sysAdmin.setSize(winX, winY);  
				sysAdmin.setLocationRelativeTo(null);
				sysAdmin.setLayout(null);
				
			
				// Master display
				JTextArea display0 = new JTextArea();
				display0.setBounds(220, startY, displayX, displayY);
				display0.setBorder(blackline);
				display0.setEditable(false);

				display0.append(" Master Computer \n");
				display0.append(" Forward \n=================\n");
				display0.append(" send = true \n");
				display0.append(" receive = false \n\n");
				display0.append(" yawOffset = 0.0 \n");
				display0.append(" pitchOffset = 0.0 \n");
				display0.append(" rollOffset = 0.0 \n");
				display0.append(" horizFov = 36.6 \n");
				
				//Far left display
				JTextArea displayL2 = new JTextArea();
				displayL2.setBounds(10, startY + 50, displayX, displayY);
				displayL2.setBorder(blackline);
				displayL2.setEditable(false);

				displayL2.append(" Client Computer \n");
				displayL2.append(" Far Left \n===============\n");
				displayL2.append(" send = false \n");
				displayL2.append(" receive = true \n\n");
				displayL2.append(" yawOffset = -73.0 \n");
				displayL2.append(" pitchOffset = 0.0 \n");
				displayL2.append(" rollOffset = 0.0 \n");
				displayL2.append(" horizFov = 36.6 \n");
				
				//Near left display
				JTextArea displayL1 = new JTextArea();
				displayL1.setBounds(115, startY + 25, displayX, displayY);
				displayL1.setBorder(blackline);
				displayL1.setEditable(false);

				displayL1.append(" Client Computer \n");
				displayL1.append(" Near Left \n==============\n");
				displayL1.append(" send = false \n");
				displayL1.append(" receive = true \n\n");
				displayL1.append(" yawOffset = -36.5 \n");
				displayL1.append(" pitchOffset = 0.0 \n");
				displayL1.append(" rollOffset = 0.0 \n");
				displayL1.append(" horizFov = 36.6 \n");
				
				//Near Right display
				JTextArea displayR1 = new JTextArea();
				displayR1.setBounds(325, startY + 25, displayX, displayY);
				displayR1.setBorder(blackline);
				displayR1.setEditable(false);

				displayR1.append(" Client Computer \n");
				displayR1.append(" Near Right \n=============\n");
				displayR1.append(" send = false \n");
				displayR1.append(" receive = true \n\n");
				displayR1.append(" yawOffset = 36.5 \n");
				displayR1.append(" pitchOffset = 0.0 \n");
				displayR1.append(" rollOffset = 0.0 \n");
				displayR1.append(" horizFov = 36.6 \n");
				
				//Far Right display
				JTextArea displayR2 = new JTextArea();
				displayR2.setBounds(430, startY + 50, displayX, displayY);
				displayR2.setBorder(blackline);
				displayR2.setEditable(false);

				displayR2.append(" Client Computer \n");
				displayR2.append(" Far Right \n==============\n");
				displayR2.append(" send = false \n");
				displayR2.append(" receive = true \n\n");
				displayR2.append(" yawOffset = 73 \n");
				displayR2.append(" pitchOffset = 0.0 \n");
				displayR2.append(" rollOffset = 0.0 \n");
				displayR2.append(" horizFov = 36.6 \n");
				
				JTextArea genInfo = new JTextArea();
				JScrollPane scrollPane = new JScrollPane(genInfo);
				scrollPane.setBounds(10, 250, winX - 35, winY - 330);
				genInfo.setBorder(blackline);
				genInfo.setEditable(false);
				// End each line at column 120
				genInfo.append("Google Earth Liquid Galaxy uses one computer per display, with two configuration files\n ");
				genInfo.append("per computer. The two files are located at:\n");
				genInfo.append("C:\\Program Files\\Google\\Google Earth\\client\\drivers.ini, and\n");
				genInfo.append("C:\\Program Files\\Google\\Google Earth\\plugins\\drivers.ini.\n\n");
				genInfo.append("Both files require elevated rights to modify them. A single user can also be given\n");
				genInfo.append("explicit rights to modify the files. The individual angles are in the above boxes.\n\n");
				genInfo.append("If Google Earth is re-installed, the ViewSync settings must be re-applied.\n\n");
				genInfo.append("Inside the SETTINGS section of each file there should be\n");
				genInfo.append(" several ViewSync entries that follow this format, where ';' is a comment line:\n");
				genInfo.append("SETTINGS {\n");
				genInfo.append("     ;ViewSync settings\n");
				genInfo.append("     ViewSync/send = true\n");
				genInfo.append("     ViewSync/receive = false\n\n");
				genInfo.append("     ; If send == true, sets the IP where the datagrams are sent, should be a\n");
				genInfo.append("     ; broadcast address at Brady High School.");
				genInfo.append("     ViewSync/hostname = a.b.c.255  ;where a.b.c is the local subnet.\n");
				genInfo.append("     ViewSync/port = 21567          ; this the the UDP protocol.\n\n");
				genInfo.append("     ViewSync/yawOffset = n.n       ; where n.n is an angle in degrees from the above boxes\n");
				genInfo.append("     ViewSync/pitchOffset = n.n\n");
				genInfo.append("     ViewSync/rollOffset = n.n\n");
				genInfo.append("     ViewSync/horizFov = 36.5\n");
				
				exitButton.setSize(startButton.getHeight(),startButton.getWidth());
				exitButton.setBounds(375,430,150,20);
				exitButton.addActionListener(new ButtonListener());
				
				sysAdmin.add(display0);
				sysAdmin.add(displayL2);
				sysAdmin.add(displayL1);
				sysAdmin.add(displayR1);
				sysAdmin.add(displayR2);
				//sysAdmin.add(genInfo);
				sysAdmin.add(scrollPane, BorderLayout.CENTER);
				sysAdmin.add(exitButton);
			}
			
		}
	}
	
	
	public void eraseLogin(){
		frame.getContentPane().removeAll();
		frame.repaint();
	}
	
	public void checkFileType() {
		if (lesSelect.isSelected()) {
			String delims = "[.]+";
			String[] fileType = fileName.split(delims);
			if (fileType[1].equals("kml")) {
				validKML = true;
			}
		}
		if(validKML == false){
			JOptionPane select = new JOptionPane();
			JOptionPane.showMessageDialog(null,"Please select a valid KML file before editing it's description by clicking on the Select Assignment button.","No File Selected", 0);
			edit.dispose();
		}
	}

	public void lessonReader() throws IOException{
		//Creating  the lesson descriptions
		lessonDesc = new JTextArea();
		lessonDesc.setLineWrap(true);
		lessonDesc.setWrapStyleWord(true);
		lessonDesc.setBounds(20, 0, 240, 400);
		lessonDesc.setEditable(false);
		JLabel lessLabel = new JLabel("Lesson Description");
		lessLabel.setBounds(85, 400, 150, 20);
		exitButton.setSize(startButton.getHeight(),startButton.getWidth());
		exitButton.setBounds(70,430,150,20);
		exitButton.addActionListener(new ButtonListener());
		Disc.add(exitButton);
		Disc.add(lessLabel);
		Disc.add(lessonDesc);
		//Creating file reader
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String line = null;
		while ((line = reader.readLine()) != null) {
		    if (line.startsWith("<!--")) {
		    	System.out.println(line);
		    	line=line.replace("<!--","");
		    	line=line.replace("-->","");
		    	String description=line.trim();
		    	lessonDesc.setText(description);
		    	descriptions = true;
		    }
		    
		}
		if(descriptions == false){
			String description = ("There is currently no discription for this lesson");
			lessonDesc.setText(description);
		}
	}
	
	public void studentLayout(){
		pageLabel = new JLabel("Welcome, student");
		lesSelect = new JCheckBox();
		descRead = new JCheckBox();
		assignmentButton.setSize(startButton.getHeight(),startButton.getWidth());
		logoutButton.setSize(startButton.getHeight(),startButton.getWidth());
		lessonDisButton.setSize(startButton.getHeight(),startButton.getWidth());
		startButton.setBounds(70,250,150,20);
		assignmentButton.setBounds(70,150,150,20);
		//lesSelect.setSize(20,20);
		lesSelect.setBounds(250,150,20,20);
		lesSelect.setEnabled(false);
		lessonDisButton.setBounds(70, 200, 150, 20);
		descRead.setBounds(250,200,20,20);
		descRead.setEnabled(false);
		geButton.setBounds(70, 300, 150, 20);
		logoutButton.setBounds(70,350,150,20);
		pageLabel.setBounds(90, 70, 150, 20);	
		
		logoutButton.addActionListener(new ButtonListener());
		startButton.addActionListener(new ButtonListener());
		geButton.addActionListener(new ButtonListener());
		assignmentButton.addActionListener(new ButtonListener());
		lessonDisButton.addActionListener(new ButtonListener());
		
		frame.add(descRead);
		frame.add(lesSelect);
		frame.add(pageLabel);
		frame.add(startButton);
		frame.add(assignmentButton);
		frame.add(lessonDisButton);
		frame.add(logoutButton);
		frame.add(geButton);
	}
	
	public void teacherLayout(){
		pageLabel = new JLabel("Welcome, Teacher");
		lesSelect = new JCheckBox();
		descRead = new JCheckBox();
		helpButton.setSize(startButton.getHeight(), startButton.getWidth());
		assignmentButton.setSize(startButton.getHeight(),startButton.getWidth());
		logoutButton.setSize(startButton.getHeight(),startButton.getWidth());
		lessonDisButton.setSize(startButton.getHeight(),startButton.getWidth());
		editDescButton.setSize(startButton.getHeight(),startButton.getWidth());
		helpButton.setBounds(70,350,150,20);
		
		editDescButton.setBounds(45,300,200,20);
		startButton.setBounds(70,200,150,20);
		assignmentButton.setBounds(70,100,150,20);
		//lesSelect.setSize(20,20);
		lesSelect.setBounds(250,100,20,20);
		lesSelect.setEnabled(false);
		lessonDisButton.setBounds(70, 150, 150, 20);
		descRead.setBounds(250,150,20,20);
		descRead.setEnabled(false);
		geButton.setBounds(70, 250, 150, 20);
		logoutButton.setBounds(70,400,150,20);
		pageLabel.setBounds(90, 70, 150, 20);	
		helpButton.addActionListener(new ButtonListener());
		logoutButton.addActionListener(new ButtonListener());
		startButton.addActionListener(new ButtonListener());
		geButton.addActionListener(new ButtonListener());
		assignmentButton.addActionListener(new ButtonListener());
		lessonDisButton.addActionListener(new ButtonListener());
		editDescButton.addActionListener(new ButtonListener());
		frame.add(helpButton);
		frame.add(editDescButton);
		frame.add(descRead);
		frame.add(lesSelect);
		frame.add(pageLabel);
		frame.add(startButton);
		frame.add(assignmentButton);
		frame.add(lessonDisButton);
		frame.add(logoutButton);
		frame.add(geButton);
	}

	
	public static void main(String[] args) {
		// Set up user interface
		UserInterface userInterface = new UserInterface();
		userInterface.loginLayout();
		//userInterface.studentLayout();
		//userInterface.teacherLayout();
		userInterface.frame.setVisible(true);

	}
	public void setFile(File file){
		fileName = file.toString();
	}

}
