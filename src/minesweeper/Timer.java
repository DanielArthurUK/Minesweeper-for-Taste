package minesweeper;

import java.util.logging.Level;
import java.util.logging.Logger;
import uk.danielarthur.tasteapi.Taste;
import uk.danielarthur.tasteapi.TasteDevice;

public class Timer implements Runnable {

	// REFERENCES TO OTHER CLASSES
	private Display d;
	private GameLogic gl;
        private TasteDevice td;
        private Time t;

	// CLASS VARIABLES
	public boolean stopped;

	// CONSTRUCTOR
	public Timer(Display d, GameLogic gl, TasteDevice td, Time t) {
		this.d = d;
		this.gl = gl;
                this.t = t;
                this.td = td;
		
		stopped = false;
                
                d.tfTime.setText(t.toString());
	}

	@Override
	public void run() {
		while (!stopped && !t.expired()) {
			try 
			{	
				// Sysout used only to refresh the thread.
				System.out.print("");
				
				if (gl.clickCount > 0) {
                                    
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
			
			
			catch (InterruptedException e) 
                        {
				t.reset();
				stopped = true;
			}
			
		}
                if(t.expired() && !gl.gameFinished) {
                    gl.gameLost("You ran out of time and lost the game!");
                }
	}

}
