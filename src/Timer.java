public class Timer implements Runnable {

	// REFERENCES TO OTHER CLASSES
	private Display d;
	private GameLogic gl;

	// CLASS VARIABLES
	public int seconds, minutes, hours;
	public String strSeconds, strMinutes, strHours;
	public boolean stopped;

	// CONSTRUCTOR
	public Timer(Display d, GameLogic gl) {
		this.d = d;
		this.gl = gl;
		
		seconds = 0;
		minutes = 0;
		hours = 0;
		
		strSeconds = "00";
		strMinutes = "00";
		strHours = "00";
		
		stopped = false;
	}

	@Override
	public void run() {
		while (!stopped) {
			try 
			{	
				// Sysout used only to refresh the thread.
				System.out.print("");
				
				if (gl.clickCount > 0) {
					
					Thread.sleep(1000);
					if (seconds < 59) 
					{
						seconds++;
						strSeconds = seconds < 10 ? "0"+Integer.toString(seconds) : Integer.toString(seconds);
					}
					else 
					{
						seconds = 0;
						strSeconds = "00";
						
						if (minutes < 59) 
						{
							minutes++;
							strMinutes = minutes < 10 ? "0"+Integer.toString(minutes) : Integer.toString(minutes);
						}
						else 
						{
							minutes = 0;
							strMinutes = "00";
							
							hours++;
							strHours = hours < 10 ? "0"+Integer.toString(hours) : Integer.toString(hours);
						}
						
					}
					d.tfTime.setText(strHours+":"+strMinutes+":"+strSeconds);
					
				}
			}
			
			
			catch (InterruptedException e) 
			{	
				seconds = 0;
				minutes = 0;
				hours = 0;
				
				strSeconds = "00";
				strMinutes = "00";
				strHours = "00";
				
				stopped = true;
			}
			
		}
	}

}
