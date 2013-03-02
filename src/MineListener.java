import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MineListener implements MouseListener {

	// REFERENCES TO OTHER CLASSES
	private Display d;
	private GameLogic gl;

	// MINELISTENER VARIABLES
	private String mouseFocus;
	private boolean leftMouseButtonPressed, rightMouseButtonPressed;
	
	// CONSTRUCTOR
	public MineListener(Display d, GameLogic gl) {
		this.d = d;
		this.gl = gl;
	}

	// MOUSE LISTENER METHODS
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
		for (int i = 0; i < d.mines.length; i++) {
			for (int k = 0; k < d.mines[i].length; k++) {
				if (e.getSource() == d.mines[i][k]) { // Gets the [i][k] index values for the button pressed.
					mouseFocus = d.mines[i][k].toString(); // update mouseFocus
					
					if(e.getButton() == 1)
						leftMouseButtonPressed=true;
					else if(e.getButton() == 3) 
						rightMouseButtonPressed=true;
					else if (e.getButton() == 2) {
						leftMouseButtonPressed=true;
						rightMouseButtonPressed=true;
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
							leftMouseButtonPressed=false;
							rightMouseButtonPressed=false;
						}
						
						else if(!(leftMouseButtonPressed && rightMouseButtonPressed))
						{
							if (e.getButton() == 3) { // Right click
								gl.flagLogic(i, k);
								rightMouseButtonPressed=false;
								gl.clickCount++;
							}
							
							else if (e.getButton() == 1) { // Left click
								gl.mineLogic(i, k);
								leftMouseButtonPressed=false;
								gl.clickCount++;
							}							
						}
						
						mouseFocus = d.mines[i][k].toString();
						
						// Skip to the end of the [i][k] array, since correct button already found and processed.
						i = d.mines.length-1;
						k = d.mines[i].length-1;
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
		
		leftMouseButtonPressed=false;
		rightMouseButtonPressed=false;
		
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