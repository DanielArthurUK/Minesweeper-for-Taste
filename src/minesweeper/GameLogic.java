package minesweeper;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;
import uk.danielarthur.tasteapi.Taste;

import uk.danielarthur.tasteapi.TasteDevice;

/**
 * A class representing the logic of each Minesweeper game.
 */
public class GameLogic {

	private Display d;
	private Timer timer;
	public Thread timerThread;
	private Thread shakeTheScreenThread;
	private TasteDevice td;
	private Time time;

	/**
	 * The number of clicks in the current game.
	 */
	public int clickCount = 0;

	/**
	 * True if game is finished, false otherwise.
	 */
	public boolean gameFinished;

	/**
	 * The number of mines left to find, and the number of flags allowed to be placed in the game.
	 */
	public int minesLeftToFind, flagAllowance;

	private Point[] mineLocations;
	private List<Point> emptyAdjPointsArrayList;
	private boolean[][] isChecked;
	private int tempFreeSpaces;

	/**
	 * Create a new GameLogic class attached to a Display, TasteDevice and Time
	 * @param d The Display that the game will be shown in
	 * @param td The TasteDevice to deliver tastes through, or null if the game is being played without taste.
	 * @param time The Time limit that this game starts with.
	 */
	public GameLogic(Display d, TasteDevice td, Time time) {
		this.d = d;
		this.td = td;
		
		gameFinished = false;

		minesLeftToFind = d.numberOfMines;
		flagAllowance = minesLeftToFind;
		mineLocations = new Point[d.numberOfMines];
		assignRandomMines();
		
		isChecked = new boolean[d.gridSizeH][d.gridSizeV];

		this.time = time;
                
		timer = new Timer(d, this, td, time);
		timerThread = new Thread(timer);
		timerThread.start();

		tempFreeSpaces = 0;

		d.getLogger().log(timer.getTime(), "Game started!");
	}

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

			tempFreeSpaces++;

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

            for(int m = 0; m < mineLocations.length; m++)
            {
                    d.mines[mineLocations[m].y][mineLocations[m].x].setIcon(d.iconMine);
            }

            d.pnlMinefield.paintImmediately(0, 0, d.pnlMinefield.getWidth(), d.pnlMinefield.getHeight());
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

	/**
	 * Applies the logic of placing a flag down at a specific cell.
	 * @param i The row of the flagged cell.
	 * @param k The column of the flagged cell.
	 */
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

	/**
	 * Applies the logic when a cell is clicked.
	 * @param i The row of the clicked cell.
	 * @param k The column of the clicked cell.
	 */
	public void mineLogic(int i, int k) {
		if (!gameFinished)
		{
			if (d.mines[i][k].getIcon() == null) // If there is no flag on that piece
			{
				if (checkForMines(i, k)) // if there is a mine under that piece
				{
					d.getLogger().log(timer.getTime(), "Cell ("+i+", "+k+") contains a mine!");
					gameLost();
				}
				else // this is the place where you examine adjacent mines
				{
					d.getLogger().log(timer.getTime(), "Cell ("+i+", "+k+") is safe!");

					findAdjacentMines(i, k);

					if(tempFreeSpaces > 5) {
						d.getLogger().log(timer.getTime(), "Clicking on ("+i+", "+k+") revealed a safe space of size "+tempFreeSpaces);
						// Deliver sweet taste to the user
						if(td != null) {
							td.deliverTaste(Taste.SWEET, 2000);
							d.getLogger().log(timer.getTime(), "Sweet taste delivered.");
						}
					}
					tempFreeSpaces = 0;

					checkWin();
				}
			}
		}
	}

	/**
	 * Applies "middle click" logic at a specific cell.
	 * @param i The row of the clicked cell.
	 * @param k The column of the clicked cell.
	 */
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
				
				if(!allowGameWin) {
					break;
				}
			}
			
			if(allowGameWin) {
				gameWon();
			}
		}
	}
	private void gameWon() {
		// interrupt to stop the clock
		timerThread.interrupt();

		d.getLogger().log(timer.getTime(), "Game won!");

		// Deliver the taste to the user
		if(td != null) {
			td.deliverTaste(Taste.SWEET, 2000);
			d.getLogger().log(timer.getTime(), "Sweet taste delivered.");
		}
                
		// sets boolean true, preventing further user interaction with the grid
		gameFinished = true;
		
		// congratulations
		JOptionPane.showMessageDialog(d.f, "Winner!");
		
		// enable new game button
		d.showNewGame();
	}

	/**
	 * A method called when the game is lost.
	 * @param msg The message given to the loser when the game is lost.
	 */
	public void gameLost(String msg) {
		// stops the clock
		timerThread.interrupt();

		d.getLogger().log(timer.getTime(), "Game lost!");

		// Deliver the taste to the user
		if(td != null) {
			td.deliverTaste(Taste.BITTER, 2000);
			d.getLogger().log(timer.getTime(), "Bitter taste delivered!");
		}

		if(msg != null) {
			d.errorDialog(msg);
		}
                
		// sets boolean true, preventing further user interaction with the grid
		gameFinished = true;
		
		// show location of all mines
		showMines();
		
		// needs a short pause before moving on
		simpleSleep(650);

		// enable new game button
		d.showNewGame();
	}

	/**
	 * Returns the timer used in the game.
	 * @return The timer used in the game.
	 */
	public Timer getTimer() {
		return timer;
	}

	/**
	 * The method called when the game is lost.
	 */
	public void gameLost() {
            gameLost(null);
        }

	/**
	 * Resets the current game.
 	 */
	public void resetGame() {

		d.getLogger().log(timer.getTime(), "The game has been reset. Start new game:");

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
		timerThread.interrupt();
		timerThread = new Thread(new Timer(d, this, td, time));
		timerThread.start();
		
		// Hide 'New Game' Button
		d.hideNewGame();
	}

}