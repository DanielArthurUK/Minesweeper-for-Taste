package minesweeper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 * This class is part of the original Minesweeper game by Osama Alqasim and has not been
 * edited in any way. Refer to the report for full references of the original author.
 */
public class ButtonListener implements ActionListener {

	// REFERENCES TO OTHER CLASSES
	private GameLogic gl;
	private Display d;
	
	// CONSTRUCTOR
	public ButtonListener(Display d, GameLogic gl)
	{
		this.gl = gl;
		this.d = d;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().contains("Help"))
			JOptionPane.showMessageDialog(d.f, "Right click to place a flag.\nLeft click to sweep!" +
					"\n\nKeyboard Shortcuts:\nCtrl+s - Change to small grid\n" +
					"Ctrl+m - Change to medium grid\nCtrl+l - Change to large grid", "Help", JOptionPane.INFORMATION_MESSAGE);
			
		else if(e.getActionCommand().equals("New Game"))
		{
			if(gl.gameFinished) // only resets the game if the current game is finished
				gl.resetGame();

			d.f.requestFocus();
		}
		
	}

}
