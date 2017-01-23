package minesweeper;

public class ShakeTheScreen implements Runnable {

	// REFERENCES TO OTHER CLASSES
	Display d;
	GameLogic gl;
	
	// SHAKE THE SCREEN VARIABLES
	private int currentXOffset, currentYOffset;
	
	// CONSTRUCTOR
	public ShakeTheScreen(Display d, GameLogic gl) {
		this.d = d;
		this.gl = gl;
		this.currentXOffset = d.f.getX();
		this.currentYOffset = d.f.getY();
	}
	
	@Override
	public void run() {
		try
		{
			//Thread.sleep(100);
			
			if(d.gridSize.equalsIgnoreCase("small"))
			{
				for(int i = 1; i < d.numberOfMines; i++)
				{
					d.f.setBounds(currentXOffset+7, currentYOffset+4, d.frameSizeH, d.frameSizeV);
					Thread.sleep(50-d.numberOfMines);
					d.f.setBounds(currentXOffset, currentYOffset, d.frameSizeH, d.frameSizeV);
					Thread.sleep(50-d.numberOfMines);
				}
			}
			
			else if(d.gridSize.equalsIgnoreCase("medium"))
			{
				for(int i = 1; i < d.numberOfMines/3; i++)
				{
					d.f.setBounds(currentXOffset+10, currentYOffset+3, d.frameSizeH, d.frameSizeV);
					Thread.sleep(65-d.numberOfMines);
					d.f.setBounds(currentXOffset, currentYOffset, d.frameSizeH, d.frameSizeV);
					Thread.sleep(65-d.numberOfMines);
				}
			}
			
			else if(d.gridSize.equalsIgnoreCase("large"))
			{
				for(int i = 1; i < d.numberOfMines/3; i++)
				{
					d.f.setBounds(currentXOffset+11, currentYOffset+3, d.frameSizeH, d.frameSizeV);
					Thread.sleep(100-d.numberOfMines);
					d.f.setBounds(currentXOffset, currentYOffset, d.frameSizeH, d.frameSizeV);
					Thread.sleep(100-d.numberOfMines);
				}				
			}
		}
		
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
