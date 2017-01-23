package minesweeper;

import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class HighScoreTable implements Serializable {
	
	private static final long serialVersionUID = 1L;

	// REFERENCES TO OTHER CLASSES
	private Display d;
	
	// HIGH SCORE TABLE VARIABLES
	private List<String> highScores;
	private String pathToScores = "";
	private String pwd = "";
	private String os = "";
	
	// CONSTRUCTOR
	public HighScoreTable(Display d) {
		this.d = d;
		highScores = new ArrayList<String>();
		
		establishPathToScores();
		readSerializable();
		sortArrayList();
	}
	
	// OTHER METHODS
	private void establishPathToScores() {
		os = System.getProperty("os.name");
		pwd = System.getProperty("user.home");
		
		if(os.contains("Windows")) {
			if(d.gridSize.equalsIgnoreCase("small"))
				pathToScores = pwd + "\\HighScoresGridSmall.ser";
				
			else if(d.gridSize.equalsIgnoreCase("medium"))
				pathToScores = pwd + "\\HighScoresGridMedium.ser";
			
			else if(d.gridSize.equalsIgnoreCase("large"))
				pathToScores = pwd + "\\HighScoresGridLarge.ser";
		}
		
		else {
			if(d.gridSize.equalsIgnoreCase("small"))
				pathToScores = pwd + "/.HighScoresGridSmall.ser";
				
			else if(d.gridSize.equalsIgnoreCase("medium"))
				pathToScores = pwd + "/.HighScoresGridMedium.ser";
			
			else if(d.gridSize.equalsIgnoreCase("large"))
				pathToScores = pwd + "/.HighScoresGridLarge.ser";
		}

	}
	
	@SuppressWarnings("unchecked")
	private void readSerializable() {
		try {
			
			File file = new File(pathToScores);
			if (file.exists()) {
				FileInputStream fis = new FileInputStream(pathToScores);
				ObjectInputStream ois = new ObjectInputStream(fis);
				
				Object temp = ois.readObject();
				ois.close();
				
				if (temp instanceof ArrayList)
					highScores = (ArrayList<String>)temp;				
			}
			
			else
				System.out.println("That file doesn't exist! Can't read it.");
			
		}
		
		catch (Exception e) {
			System.out.println("An exception occured while reading from the scores file.\nPlease manually delete the " +
					"high scores file and try again.");
		}
	}
	
	private void writeToFiles() {
		try {

			FileOutputStream fos = new FileOutputStream(pathToScores);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(highScores);
			oos.flush();
			oos.close();

		}
		
		catch (Exception e) {
			System.out.println("An exception occured while writing to the scores file.");
		}
	}

	public void addToHighScores(String name, String time) {
		highScores.add(name + ":" + time);
		sortArrayList();
		writeToFiles();
	}
	public void printHighScores() {	
		int maxScoresToReturn = 10;
		if (highScores.size() < 10)
			maxScoresToReturn = highScores.size();
		
		for(int i = 0; i < maxScoresToReturn; i++)
			if(i == 9)
				System.out.println((i+1) + ". " + highScores.get(i));
			else
				System.out.println(" " + (i+1) + ". " + highScores.get(i));
		
		System.out.println();
	}
	public void showHighScoreWindow() {
		// PRE-INITIALIZATION CALCULATIONS
		int maxScoresToReturn = 10;
		if (highScores.size() < 10)
			maxScoresToReturn = highScores.size();
		
		// SET UP FRAME
		JFrame scoresFrame = new JFrame("High Scores");
		scoresFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		scoresFrame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {}
			
			@Override
			public void windowIconified(WindowEvent arg0) {}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {	}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				d.f.setVisible(true);
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {	}
			
			@Override
			public void windowActivated(WindowEvent arg0) {}
		});
		
		scoresFrame.setBounds(0,0,275,60 + maxScoresToReturn*15);
		scoresFrame.setLocationRelativeTo(d.f);
		scoresFrame.setResizable(false);
		
		// SET UP PANEL
		JPanel scoresPanel = new JPanel(new FlowLayout());
		
		// SET UP COMPONENTS
		JTextArea namesTextArea = new JTextArea(maxScoresToReturn,15);
		JTextArea timesTextArea = new JTextArea(maxScoresToReturn,5);
		namesTextArea.setFocusable(false);
		timesTextArea.setFocusable(false);
		
		// ADD COMPONENTS TO PANEL
		scoresPanel.add(namesTextArea);
		scoresPanel.add(timesTextArea);
		
		// ADD PANEL TO FRAME
		scoresFrame.add(scoresPanel);
		
		// SET FRAME VISIBLE
		scoresFrame.setVisible(true);
	
		// DISPLAY IT ON THE SCREEN
		for(int i = 0; i < maxScoresToReturn; i++)
		{
			String name = highScores.get(i).substring(0, highScores.get(i).indexOf(":"));
			String time = highScores.get(i).substring(highScores.get(i).indexOf(":")+1);
			
			if(i == maxScoresToReturn - 1)
			{
				namesTextArea.append(name);
				timesTextArea.append(time);
			}
			
			else
			{
				namesTextArea.append(name+"\n");
				timesTextArea.append(time+"\n");				
			}
		}
	}
	private void sortArrayList() {
		Collections.sort(highScores, new SortArrayListByShortestTime());
	}
}


class SortArrayListByShortestTime implements Comparator<String> {
	
	@Override
	public int compare(String o1, String o2) {
		o1 = o1.substring(o1.indexOf(":")+1);
		o2 = o2.substring(o2.indexOf(":")+1);
		
//		System.out.println("o1 is " + o1);
//		System.out.println("o2 is " + o2);
//		System.out.println("compareTo returned " + o1.compareTo(o2));
//		System.out.println();
		
		if (o1.compareTo(o2) < 0) // if o1 time is less than than o2
			return -1;
		else if (o1.compareTo(o2) == 0) // if o1 is equal to o2
			return 0; // do nothing
		return 1;
	}
	
}