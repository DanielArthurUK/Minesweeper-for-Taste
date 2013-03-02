import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class ButtonListener implements ActionListener {

	// REFERENCES TO OTHER CLASSES
	private GameLogic gl;
	private Display d;
	private HighScoreTable hs;
	
	// CONSTRUCTOR
	public ButtonListener(Display d, GameLogic gl, HighScoreTable hs)
	{
		this.gl = gl;
		this.d = d;
		this.hs = hs;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().contains("Help"))
			JOptionPane.showMessageDialog(d.f, "Right click to place a flag.\nLeft click to sweep!" +
					"\n\nKeyboard Shortcuts:\nCtrl+s - Change to small grid\n" +
					"Ctrl+m - Change to medium grid\nCtrl+l - Change to large grid\n\n" +
					"Ctrl+t - Toggle animations\nCtrl+n - Start new game", "Help", JOptionPane.INFORMATION_MESSAGE);
		
		else if(e.getActionCommand().equals("High Scores"))
			hs.showHighScoreWindow();
			
		else if(e.getActionCommand().equals("New Game"))
		{
			if(gl.gameFinished) // only resets the game if the current game is finished
				gl.resetGame();

			d.f.requestFocus();
		}
		
	}

}
