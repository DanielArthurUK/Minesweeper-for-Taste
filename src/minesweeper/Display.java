package minesweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import uk.danielarthur.tasteapi.TasteDevice;

/**
 * The Display window of the Minesweeper game.
 */
public class Display {

	/**
	 * The Frame itself
	 */
	public JFrame f;

	/**
	 * The panels used in the frame.
	 */
	public JPanel pnlTop, pnlTopE, pnlTopC, pnlTopW, pnlMinefield, pnlBottom, pnlBottomE, pnlBottomC, pnlBottomW;

	/**
	 * Two-dimensional array containing the mines
	 */
	public JButton[][] mines;

	/**
	 * New game and help buttons
	 */
	public JButton newGame, help;

	/**
	 * Text fields showing timer and the number of mines that have been placed.
	 */
	public JTextField tfTime, tfMine;

	/**
	 * Various labels used in the UI.
	 */
	public JLabel imgTime, imgMine, imgFlag, animationStatus;

	/**
	 * Image icons of a mine and a flag.
	 */
	public ImageIcon iconMine, iconFlag;
	
	private GameLogic gl;
	private Logger logger;
	
	/**
	 * String representing the grid size.
	 */
	public String gridSize;

	/**
	 * Integers representing more in depth grid sizes.
	 */
	public int gridSizeH, gridSizeV, frameSizeH, frameSizeV;

	/**
	 * The number of mines in the field.
	 */
	public int numberOfMines;

	/**
	 * The time limit for the current game.
	 */
	public Time timeLimit;
	
	private Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
	private Font fontBigger = new Font(Font.SANS_SERIF, Font.BOLD, 12);

	/**
	 * The different colours used in the UI.
	 */
	public String backgroundColour = "#151515";
	public String enabledGridColour = "#DDDDDD";
	public String disabledGridColour = "#2E2E2E";
	public String hoverOverGridColor = "#A6BBCC";
        
	private TasteDevice tasteDevice;

	/**
	 * Creates a new Display of the given size, with a TasteDevice and log file.
	 * @param gridSize The size of the grid: "small", "medium" and "large" accepted.
	 * @param td The TasteDevice to receive taste through, or null if the game is being played without one.
	 * @param logger The Logger object to produce log files.
	 */
	public Display(String gridSize, TasteDevice td, Logger logger) {
		this.logger = logger;
		tasteDevice = td;
            
		if(gridSize.equalsIgnoreCase("large"))
		{
			try {
				this.timeLimit = new Time(0,3,0);
			} catch (InvalidTimeException ex) {
				this.errorDialog("There was an error initiating the timer.");
			}
			this.gridSize = "large";
			this.numberOfMines = 85;
			this.gridSizeH = 16;
			this.gridSizeV = 30;
			this.frameSizeH = 1200;
			this.frameSizeV = 675;
		}
		else if(gridSize.equalsIgnoreCase("medium"))
		{
			try {
				this.timeLimit = new Time(0,2,0);
			} catch (InvalidTimeException ex) {
				this.errorDialog("There was an error initiating the timer.");
			}
			this.gridSize = "medium";
			this.numberOfMines = 40;
			this.gridSizeH = 16;
			this.gridSizeV = 16;
			this.frameSizeH = 600;
			this.frameSizeV = 675;
		}
		else
		{
			try {
				this.timeLimit = new Time(0,1,0);
			} catch (InvalidTimeException ex) {
				this.errorDialog("There was an error initiating the timer.");
			}
			this.gridSize = "small";
			this.numberOfMines = 10;
			this.gridSizeH = 8;
			this.gridSizeV = 8;
			this.frameSizeH = 300;
			this.frameSizeV = 375;
		}
		
		setUpTextFields();

		gl = new GameLogic(this, td, timeLimit);

		setUpFrame();
		setUpPanels();
		setPanelBackground();
		setUpButtons();
		hideNewGame();
		setUpImages();
		setUpIcons();
		
		addComponentsToPanels();
		addPanelsToPanels();
		addPanelsToFrame();
		
		f.setVisible(true);
		f.addKeyListener(new FrameKeyListener(gl, this, td));
	}
        
	protected void errorDialog(String errorMsg) {
		JOptionPane.showMessageDialog(f, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Returns the Logger object used to create log files.
	 * @return The logger object used to create log files.
	 */
	public Logger getLogger() {
		return logger;
	}

	private void setUpFrame() {
		f = new JFrame("Minesweeper");
		f.setLayout(new BorderLayout());
		f.setBackground(Color.LIGHT_GRAY);
		f.setBounds(0, 0, frameSizeH, frameSizeV);
		f.setLocationRelativeTo(null);
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void setUpPanels() {
		pnlTop = new JPanel(new BorderLayout());
		pnlTopE = new JPanel(new FlowLayout());
		pnlTopC = new JPanel(new BorderLayout());
		pnlTopW = new JPanel(new FlowLayout());
		pnlMinefield = new JPanel(new GridLayout(gridSizeH,gridSizeV));
		pnlBottom = new JPanel(new BorderLayout());
		pnlBottomE = new JPanel(new FlowLayout());
		pnlBottomC = new JPanel(new FlowLayout());
		pnlBottomW = new JPanel(new FlowLayout());
	}

	private void setPanelBackground() {
		pnlTop.setBackground(Color.decode(backgroundColour));
		pnlTopE.setBackground(Color.decode(backgroundColour));
		pnlTopC.setBackground(Color.decode(backgroundColour));
		pnlTopW.setBackground(Color.decode(backgroundColour));
		pnlMinefield.setBackground(Color.decode(backgroundColour));
		pnlBottom.setBackground(Color.decode(backgroundColour));
		pnlBottomE.setBackground(Color.decode(backgroundColour));
		pnlBottomC.setBackground(Color.decode(backgroundColour));
		pnlBottomW.setBackground(Color.decode(backgroundColour));	
	}

	private void setUpButtons() {
		newGame = new JButton("New Game");
		help = new JButton("      Help      ");
		
		help.setFont(font);
		newGame.setFont(fontBigger);
		
                help.setMargin(new Insets(1,2,1,2));
		newGame.setMargin(new Insets(1,5,1,5));
		
		help.setBackground(Color.decode(enabledGridColour));
		newGame.setBackground(Color.decode(enabledGridColour));
		
		newGame.setFocusable(false);
		help.setFocusable(false);
                
		help.addActionListener(new ButtonListener(this, gl));
		newGame.addActionListener(new ButtonListener(this, gl));
	}

	private void setUpImages() {
		imgTime = new JLabel(new ImageIcon(this.getClass().getResource("/images/stopwatch.png")));
		imgMine = new JLabel(new ImageIcon(this.getClass().getResource("/images/Mine2_032x032_32.png")));
		imgFlag = new JLabel(new ImageIcon(this.getClass().getResource("/images/Flag_024x024_32.png")));
		
		animationStatus = new JLabel();
		animationStatus.setForeground(Color.decode(hoverOverGridColor));
		animationStatus.setHorizontalAlignment(SwingConstants.CENTER);
		
		animationStatus.setFont(gridSize.equalsIgnoreCase("small") ? font : fontBigger);
	}

	private void setUpIcons() {
		iconMine = new ImageIcon(this.getClass().getResource("/images/Mine_024x024_32.png"));
		iconFlag = new ImageIcon(this.getClass().getResource("/images/Flag_024x024_32.png"));
	}

	private void setUpTextFields() {
		tfTime = new JTextField(5);
		tfMine = new JTextField(5);
		
		tfMine.setText(numberOfMines + "F / " + numberOfMines);

		tfTime.setFocusable(false);
		tfMine.setFocusable(false);
		
		tfTime.setFont(fontBigger);
		tfMine.setFont(fontBigger);
	}

	private void addComponentsToPanels() {
		pnlTopW.add(imgTime);
		pnlTopW.add(tfTime);
		pnlTopC.add(animationStatus, BorderLayout.CENTER);
		pnlTopE.add(tfMine);
		pnlTopE.add(imgMine);
		
		addButtonsToMinefield();
		
		pnlBottomC.add(newGame);
		pnlBottomE.add(help);
	}

	private void addButtonsToMinefield() {
		mines = new JButton[gridSizeH][gridSizeV];
		for(int i = 0; i < gridSizeH; i++)
			for(int j = 0; j < gridSizeV; j++)
			{
				mines[i][j] = new JButton("");
				mines[i][j].setFocusable(false);
				mines[i][j].setBackground(Color.decode(enabledGridColour));
				mines[i][j].setMargin(new Insets(1,1,1,1));
				mines[i][j].setFont(fontBigger);
				mines[i][j].addMouseListener(new MineListener(this, gl));
				pnlMinefield.add(mines[i][j]);
			}
	}

	private void addPanelsToPanels() {
		pnlTop.add(pnlTopE, BorderLayout.EAST);
		pnlTop.add(pnlTopC, BorderLayout.CENTER);
		pnlTop.add(pnlTopW, BorderLayout.WEST);
		
		pnlBottom.add(pnlBottomE, BorderLayout.EAST);
		pnlBottom.add(pnlBottomC, BorderLayout.CENTER);
		pnlBottom.add(pnlBottomW, BorderLayout.WEST);
	}

	private void addPanelsToFrame() {
		f.add(pnlTop, BorderLayout.NORTH);
		f.add(pnlMinefield, BorderLayout.CENTER);
		f.add(pnlBottom, BorderLayout.SOUTH);
	}

	public void hideNewGame() {
		newGame.setContentAreaFilled(false);
		newGame.setBorderPainted(false);
		newGame.setForeground(Color.decode(backgroundColour));
		newGame.setFocusable(false);
	}

	public void showNewGame() {
		newGame.setContentAreaFilled(true);
		newGame.setBorderPainted(true);
		newGame.setForeground(null);
		newGame.setFocusable(true);
	}

}
