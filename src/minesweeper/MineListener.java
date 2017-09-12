package minesweeper;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * An implements MouseListener for the Minesweeper game.
 */
public class MineListener implements MouseListener {

	private Display d;
	private GameLogic gl;
	private Timer timer;

	private String mouseFocus;
	private boolean leftMouseButtonPressed, rightMouseButtonPressed;

	/**
	 * Create a new MineListener attached to the specified Display and Game Logic.
	 * @param d The Display object for the current Minesweeper game.
	 * @param gl The GameLogic object for the current Minesweeper game.
	 */
	public MineListener(Display d, GameLogic gl) {
		this.d = d;
		this.gl = gl;
		this.timer = gl.getTimer();
	}

	/**
	 * This method is not implemented.
	 * @param e The MouseEvent that is passed to the Listener.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * This method is called when the user presses the mouse.
	 * @param e The MouseEvent passed to the Listener.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		for (int i = 0; i < d.mines.length; i++) {
			for (int k = 0; k < d.mines[i].length; k++) {
				if (e.getSource() == d.mines[i][k]) { // Gets the [i][k] index values for the button pressed.
					mouseFocus = d.mines[i][k].toString(); // update mouseFocus


					if(e.getButton() == 1)
						leftMouseButtonPressed = true;
					else if(e.getButton() == 3) 
						rightMouseButtonPressed = true;
					else if (e.getButton() == 2) {
						leftMouseButtonPressed = true;
						rightMouseButtonPressed = true;
					}
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (int i = 0; i < d.mines.length; i++) {
			for (int k = 0; k < d.mines[i].length; k++) {
				if (e.getSource() == d.mines[i][k]) { // Gets the [i][k] index values for the button clicked.
					if (d.mines[i][k].toString().equals(mouseFocus)) // if the mouse cursor is still on that button.
					{
						if (d.mines[i][k].isEnabled())
							d.mines[i][k].setBackground(Color.decode(d.enabledGridColour));
						
						if(leftMouseButtonPressed && rightMouseButtonPressed) {
							gl.middleClickLogic(i, k);
							leftMouseButtonPressed = false;
							rightMouseButtonPressed = false;
						}
						
						else if(!(leftMouseButtonPressed && rightMouseButtonPressed))
						{
							if (e.getButton() == 3) { // Right click
								d.getLogger().log(timer.getTime(), "Cell flagged at coordinates ("+i+", "+k+")");
								gl.flagLogic(i, k);
								rightMouseButtonPressed = false;
								gl.clickCount++;
							}
							
							else if (e.getButton() == 1) { // Left click
								d.getLogger().log(timer.getTime(), "Cell clicked at coordinates ("+i+", "+k+")");
								gl.mineLogic(i, k);
								leftMouseButtonPressed = false;
								gl.clickCount++;
							}							
						}
						
						mouseFocus = d.mines[i][k].toString();
						
						// Skip to the end of the [i][k] array, since correct button already found and processed.
						i = d.mines.length - 1;
						k = d.mines[i].length - 1;
						break;
					}
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		for (int i = 0; i < d.mines.length; i++) {
			for (int k = 0; k < d.mines[i].length; k++) {
				if (e.getSource() == d.mines[i][k]) { // Gets the [i][k] index values for the button entered.
					mouseFocus = d.mines[i][k].toString();
					if (d.mines[i][k].isEnabled() && d.mines[i][k].getIcon() == null && gl.gameFinished != true)
						d.mines[i][k].setBackground(Color.decode(d.hoverOverGridColor));
				}
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseFocus = null;
		leftMouseButtonPressed = false;
		rightMouseButtonPressed = false;
		
		for (int i = 0; i < d.mines.length; i++) {
			for (int k = 0; k < d.mines[i].length; k++) {
				if (e.getSource() == d.mines[i][k]) { // Gets the [i][k] index values for the button exited.
					if (d.mines[i][k].isEnabled() && d.mines[i][k].getIcon() == null && gl.gameFinished != true) {
						d.mines[i][k].setBackground(Color.decode(d.enabledGridColour));
					}
				}
			}
		}
	}
}