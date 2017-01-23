package minesweeper;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;
import uk.danielarthur.tasteapi.Taste;

import uk.danielarthur.tasteapi.TasteDevice;

public class GameLogic {

	// REFERENCES TO OTHER CLASSES
	private Display d;
	private Timer timer;
	public Thread timerThread;
	private Thread shakeTheScreenThread;
	private HighScoreTable hs;
        private TasteDevice td;

	// VARIABLES
	public int clickCount = 0;
	public boolean gameFinished;
	public int minesLeftToFind, flagAllowance;
	private Point[] mineLocations;
	private List<Point> emptyAdjPointsArrayList;
	private boolean[][] isChecked;
	

	// CONSTRUCTOR
	public GameLogic(Display d, HighScoreTable hs, TasteDevice td) {
		this.d = d;
		this.hs = hs;
                this.td = td;
		
		gameFinished = false;

		minesLeftToFind = d.numberOfMines;
		flagAllowance = minesLeftToFind;
		mineLocations = new Point[d.numberOfMines];
		assignRandomMines();
		
		isChecked = new boolean[d.gridSizeH][d.gridSizeV];

		timer = new Timer(d, this);
		timerThread = new Thread(timer);
		timerThread.start();
	}

	// GAMELOGIC METHODS
	private void assignRandomMines() {
		Random r = new Random();
		boolean safeToAdd = true;

		for (int i = 0; i < mineLocations.length; i++) {
			mineLocations[i] = new Point();
		}

		for (int i = 0; i < mineLocations.length; i++) {
			safeToAdd = true;
			int x = r.nextInt(d.gridSizeV);
			int y = r.nextInt(d.gridSizeH);

			for(int j = 0; j < mineLocations.length; j++)
			{
				if (i!=j)
				{
					if (mineLocations[j].x == x && mineLocations[j].y == y) // if the pair of random numbers already exist
					{
						i--; // reduce value of i by 1 to give it another try. i will be incremented by 1 in the next iteration.
						safeToAdd = false; 
						break;
					} 
				}
			}
			
			if (safeToAdd)
			{
				mineLocations[i].x = x;
				mineLocations[i].y = y;				
			}
		}
			
	}
	private boolean checkForMines(int i, int k) {
		for(int m = 0; m < mineLocations.length; m++)
		{
			if(mineLocations[m].y == i && mineLocations[m].x == k)
				return true;
		}
		return false;
	}

	private boolean findAdjacentFlags(int i, int k) {
		try
		{
				if(d.mines[i][k] != null && d.mines[i][k].getIcon() == d.iconFlag)
					return true;		
		}
		
		catch (ArrayIndexOutOfBoundsException e) {
			
		}
		
		return false;
	}
	private void findAdjacentMines(int i, int k) {
		int adjacentMines = 0;
				
		if(checkForMines(i+1,k-1))
			adjacentMines++;
		if(checkForMines(i+1,k))
			adjacentMines++;
		if(checkForMines(i+1,k+1))
			adjacentMines++;
		if(checkForMines(i,k-1))
			adjacentMines++;
		if(checkForMines(i,k+1))
			adjacentMines++;
		if(checkForMines(i-1,k-1))
			adjacentMines++;
		if(checkForMines(i-1,k))
			adjacentMines++;
		if(checkForMines(i-1,k+1))
			adjacentMines++;
		
		isChecked[i][k] = true;
		
		if(adjacentMines > 0)
		{
			d.mines[i][k].setText(Integer.toString(adjacentMines));
			d.mines[i][k].setBackground(Color.decode(d.disabledGridColour));
			d.mines[i][k].setEnabled(false);
		}
		else
		{
			d.mines[i][k].setBackground(Color.decode(d.disabledGridColour));
			d.mines[i][k].setEnabled(false);
			
			if(i > 0)
			{
				if (isChecked[i-1][k] == false)
				findAdjacentMines(i-1,k); // check button above.
			}
			
			if(k < d.mines[i].length-1)
			{
				if (isChecked[i][k+1] == false)
				findAdjacentMines(i,k+1); // check button to the right.
			}
			
			if(i < d.mines.length-1)
			{
				if (isChecked[i+1][k] == false)
				findAdjacentMines(i+1,k); // check button below.
			}
			
			if(k > 0)
			{
				if (isChecked[i][k-1] == false)
				findAdjacentMines(i,k-1); // check button to the left.
			}
			
			
			// DIAGONALS
			if(i > 0 && k < d.mines[i].length-1)
			{
				if (isChecked[i-1][k+1] == false)
					findAdjacentMines(i-1,k+1); // check button above right.
			}
			
			if(i < d.mines.length-1 && k < d.mines[i].length-1)
			{
				if (isChecked[i+1][k+1] == false)
					findAdjacentMines(i+1,k+1); // check button below right.
			}
			
			if(i < d.mines.length-1 && k > 0)
			{
				if (isChecked[i+1][k-1] == false)
					findAdjacentMines(i+1,k-1); // check button below left.
			}
			
			if(i > 0 && k > 0)
			{
				if (isChecked[i-1][k-1] == false)
					findAdjacentMines(i-1,k-1); // check button above left.
			}

		}
	}
	private boolean findAdjacentEmpty(int i, int k) {
		try
		{
				if(d.mines[i][k] != null && d.mines[i][k].getIcon() == null && d.mines[i][k].isEnabled()) {
					emptyAdjPointsArrayList.add(new Point(i, k));
					return true;
				}
		}
		
		catch (ArrayIndexOutOfBoundsException e) {
			
		}
		
		return false;
	}
	
	private void showMines() {
		if(d.animate)
		{
			try 
			{
				for(int m = 0; m < mineLocations.length; m++)
				{
					d.mines[mineLocations[m].y][mineLocations[m].x].setIcon(d.iconMine);
					Thread.sleep(450 / d.numberOfMines);
					d.pnlMinefield.paintImmediately(0, 0, d.pnlMinefield.getWidth(), d.pnlMinefield.getHeight());
				}
			}
			
			catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
		
		else
		{
			for(int m = 0; m < mineLocations.length; m++)
			{
				d.mines[mineLocations[m].y][mineLocations[m].x].setIcon(d.iconMine);
			}
			
			d.pnlMinefield.paintImmediately(0, 0, d.pnlMinefield.getWidth(), d.pnlMinefield.getHeight());
		}
	}
	private void shakeScreen() {
		if(d.animate)
		{
			shakeTheScreenThread = null;
			shakeTheScreenThread = new Thread(new ShakeTheScreen(d, this));
			shakeTheScreenThread.start();			
		}
	}
	private void simpleSleep(int i) {
		try 
		{
			Thread.sleep(i);
		}
		
		catch (InterruptedException e) {
			System.out.println("Sleep interrupted");
		}
	}
	
	public void flagLogic(int i, int k) {
		if (!gameFinished)
		{
			if (d.mines[i][k].getIcon() == null)
			{
				if(flagAllowance > 0)
				{
					if(d.mines[i][k].isEnabled()) // only place flag if button is enabled
					{
						flagAllowance--;
						d.mines[i][k].setIcon(d.iconFlag);
						d.tfMine.setText(flagAllowance + "F / " + d.numberOfMines);
						
						if (checkForMines(i, k))
							minesLeftToFind--;
						
						checkWin();
					}
				}
				else
				{
					JOptionPane.showMessageDialog(d.f, "Flag allowance exceeded!");
				}
			}
			else 
			{
				flagAllowance++;
				d.mines[i][k].setIcon(null);
				d.tfMine.setText(flagAllowance + "F / " + d.numberOfMines);
				
				if (checkForMines(i, k))
					minesLeftToFind++;
			}
		}
	}
	public void mineLogic(int i, int k) {
		if (!gameFinished)
		{
			if (d.mines[i][k].getIcon() == null) // If there is no flag on that piece
			{
				if (checkForMines(i, k)) // if there is a mine under that piece
				{
					gameLost();
				}
				else // this is the place where you examine adjacent mines
				{
					findAdjacentMines(i, k);
					checkWin();
				}
			}			
		}
	}
	public void middleClickLogic(int i, int k) {
		// Check if its a valid button for this type of action.
		if (d.mines[i][k].getText() != null && d.mines[i][k].getText().matches("[1-9]")){
			
			// Check to see if right number of flags are placed.
			if(getAdjacentFlagCounter(i, k) == Integer.parseInt(d.mines[i][k].getText())) {
				
				// If d.mines[i][k] has no empty buttons, surrounding it, then do nothing
				if(getEmptySurroundingButtons(i, k) == 0) {
					System.out.println("No surrounding empty.");
				}
				
				// Find mines for all empty buttons
				else {					
					for (int z = 0; z < emptyAdjPointsArrayList.size(); z++){
						mineLogic(emptyAdjPointsArrayList.get(z).x, emptyAdjPointsArrayList.get(z).y);						
					}
				}
			}
		}
	}

	private int getEmptySurroundingButtons(int i, int k) {
		emptyAdjPointsArrayList = new ArrayList<Point>();
		
		int emptyCounter = 0;
		
		if(findAdjacentEmpty(i+1,k-1))
			emptyCounter++;
		if(findAdjacentEmpty(i+1,k))
			emptyCounter++;
		if(findAdjacentEmpty(i+1,k+1))
			emptyCounter++;
		if(findAdjacentEmpty(i,k-1))
			emptyCounter++;
		if(findAdjacentEmpty(i,k+1))
			emptyCounter++;
		if(findAdjacentEmpty(i-1,k-1))
			emptyCounter++;
		if(findAdjacentEmpty(i-1,k))
			emptyCounter++;
		if(findAdjacentEmpty(i-1,k+1))
			emptyCounter++;
		
		return emptyCounter;
	}
	private int getAdjacentFlagCounter(int i, int k) {
		int adjacentFlagCounter = 0;
		
		if(findAdjacentFlags(i+1,k-1))
			adjacentFlagCounter++;
		if(findAdjacentFlags(i+1,k))
			adjacentFlagCounter++;
		if(findAdjacentFlags(i+1,k+1))
			adjacentFlagCounter++;
		if(findAdjacentFlags(i,k-1))
			adjacentFlagCounter++;
		if(findAdjacentFlags(i,k+1))
			adjacentFlagCounter++;
		if(findAdjacentFlags(i-1,k-1))
			adjacentFlagCounter++;
		if(findAdjacentFlags(i-1,k))
			adjacentFlagCounter++;
		if(findAdjacentFlags(i-1,k+1))
			adjacentFlagCounter++;
		
		return adjacentFlagCounter;
	}
	
	private void checkWin() {
		if(minesLeftToFind == 0)
		{
			boolean allowGameWin = true;
			
			for(int x = 0; x < d.mines.length; x++)
			{
				for(int y = 0; y < d.mines[x].length; y++)
				{
					if(d.mines[x][y].isEnabled())
					{
						if(d.mines[x][y].getIcon() == null)
						{
							allowGameWin = false;
							break;											
						}
					}									
				}
				
				if(!allowGameWin)
					break;
			}
			
			if(allowGameWin)
				gameWon();
		}
	}
	private void gameWon() {
		// interrupt to stop the clock
		timerThread.interrupt();
		
                // Deliver the taste to the user
                td.deliverTaste(Taste.SWEET, 2000);
                
		// sets boolean true, preventing further user interaction with the grid
		gameFinished = true;
		
		// congratulations
		JOptionPane.showMessageDialog(d.f, "Winner!");
		String name = JOptionPane.showInputDialog(d.f, "What is your name?", "High Score Entry", JOptionPane.QUESTION_MESSAGE);
		String time = d.tfTime.getText();
		
		if (name == null)
			name = "";
		
		if(name.length() > 14) // if name is too long, truncate it
			name = name.substring(0,14);
		
		hs.addToHighScores(name, time);
		
		// enable new game button
		d.showNewGame();
	}
	private void gameLost() {
		// stops the clock
		timerThread.interrupt();
		
                // Deliver the taste to the user
                td.deliverTaste(Taste.BITTER, 2000);
                
		// sets boolean true, preventing further user interaction with the grid
		gameFinished = true;
		
		// show location of all mines
		shakeScreen();
		showMines();
		
		// needs a short pause before moving on
		simpleSleep(650);
		
		// pop up message
		//JOptionPane.showMessageDialog(d.f, "Loser!");
					
		// enable new game button
		d.showNewGame();
	}
	public void resetGame() {
		// Reset boolean
		gameFinished = false;
		
		// Reset isChecked 2D boolean array
		for(int i = 0; i < isChecked.length; i++)
			for(int j = 0; j < isChecked[i].length; j++)
				isChecked[i][j] = false;
		
		// Reset counters
		clickCount = 0;
		minesLeftToFind = d.numberOfMines;
		flagAllowance = minesLeftToFind;
		d.tfMine.setText(flagAllowance + "F / " + d.numberOfMines);
		
		// Clear state of mine buttons
		for(int i = 0; i < d.gridSizeH; i++)
			for(int j = 0; j < d.gridSizeV; j++)
			{				
				d.mines[i][j].setIcon(null);
				d.mines[i][j].setText(null);
				d.mines[i][j].setBackground(Color.decode(d.enabledGridColour));
				d.mines[i][j].setEnabled(true);
			}
		
		// Get new random mine locations
		mineLocations = null;
		mineLocations = new Point[d.numberOfMines];
		assignRandomMines();

		// Reset Timer
		d.tfTime.setText("00:00:00");
		timerThread.interrupt();
		timerThread = new Thread(new Timer(d, this));
		timerThread.start();
		
		// Hide 'New Game' Button
		d.hideNewGame();
	}

}