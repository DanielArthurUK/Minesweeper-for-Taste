package minesweeper;

import java.util.logging.Level;
import java.util.logging.Logger;
import uk.danielarthur.tasteapi.Taste;
import uk.danielarthur.tasteapi.TasteDevice;

/**
 * Class that represents a countdown timer.
 */
public class Timer implements Runnable {

	private Display d;
	private GameLogic gl;
	private TasteDevice td;
	private Time t;

	/**
	 * True if timer has stopped.
	 */
	public boolean stopped;

	/**
	 * True if timer has started.
	 */
	public boolean started;

	/**
	 * Create a new timer.
	 * @param d The display of the current Minesweeper game.
	 * @param gl The GameLogic of the current Minesweeper game.
	 * @param td The TasteDevice of the current Minesweeper game, or null if taste is not being used.
	 * @param t The start time of this Timer.
	 */
	public Timer(Display d, GameLogic gl, TasteDevice td, Time t) {
		this.d = d;
		this.gl = gl;
		this.t = t;
		this.td = td;
		
		stopped = false; // The timer is stopped by default
        started = false;

        d.tfTime.setText(t.toString()); // Refreshes the text on the timer
	}

	@Override
	public void run() {

		while (!stopped && !t.expired()) {
			try 
			{
				System.out.print(""); // Sysout used only to refresh the thread.
				
				if (gl.clickCount > 0) {

					if(td != null && !started) {
						td.deliverTaste(Taste.SOUR, 2000);
						d.getLogger().log(getTime(), "Sour taste delivered.");
						started = true;
					}
					if(td != null) {
						if(t.getHours() == 0
								&& t.getMinutes() == 0
								&& t.getSeconds() == 10) {
							td.deliverTaste(Taste.SOUR, 2000);
						}
					}

					Thread.sleep(1000);

					try {
						t.decrementSecond();
					} catch (InvalidTimeException ex) {
						d.errorDialog("A fatal error occurred when counting down the timer.");
					}

					d.tfTime.setText(t.toString());
					
				}
			}
			
			
			catch (InterruptedException e) {
				t.reset();
				stopped = true;
			}
			
		}

		if(t.expired() && !gl.gameFinished) {
			gl.gameLost("You ran out of time and lost the game!");
		}
	}

	/**
	 * Returns the current Time on the Timer.
	 * @return The current Time on the Timer.
	 */
	public Time getTime() {
		return t;
	}

}
