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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Display {

	public JFrame f;
	public JPanel pnlTop, pnlTopE, pnlTopC, pnlTopW, pnlMinefield, pnlBottom, pnlBottomE, pnlBottomC, pnlBottomW;
	public JButton[][] mines;
	public JButton highScores, newGame, help;
	public JTextField tfTime, tfMine;
	public JLabel imgTime, imgMine, imgFlag, animationStatus;
	public ImageIcon iconMine, iconFlag;
	
	// REFERENCES TO OTHER CLASSES
	private GameLogic gl;
	private HighScoreTable hs;
	
	// USED FOR SETTING GRID SIZE
	public String gridSize;
	public int gridSizeH, gridSizeV, frameSizeH, frameSizeV;
	public int numberOfMines;
	
	// USED FOR SETTING ANIMATIONS OPTION
	public boolean animate;
	
	// GLOBAL FONTS
	private Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
	private Font fontBigger = new Font(Font.SANS_SERIF, Font.BOLD, 12);
	
	// COLOURS
	public String backgroundColour = "#151515";
	public String enabledGridColour = "#DDDDDD";
	public String disabledGridColour = "#2E2E2E";
	public String hoverOverGridColor = "#A6BBCC";
	
	// CONSTRUCTOR
	public Display(String gridSize, Boolean animate) {
		if(gridSize.equalsIgnoreCase("large"))
		{
			this.gridSize = "large";
			this.numberOfMines = 85;
			this.gridSizeH = 16;
			this.gridSizeV = 30;
			this.frameSizeH = 1200;
			this.frameSizeV = 675;
		}
		else if(gridSize.equalsIgnoreCase("medium"))
		{
			this.gridSize = "medium";
			this.numberOfMines = 40;
			this.gridSizeH = 16;
			this.gridSizeV = 16;
			this.frameSizeH = 600;
			this.frameSizeV = 675;
		}
		else
		{
			this.gridSize = "small";
			this.numberOfMines = 10;
			this.gridSizeH = 8;
			this.gridSizeV = 8;
			this.frameSizeH = 300;
			this.frameSizeV = 375;
		}
		
		// SPECIAL CASE. NEED tfTime TO BE INITIALIZED BEFORE NEWING GameLogic
		setUpTextFields();
		// --------------------------------------
		
		this.animate = animate;
		hs = new HighScoreTable(this);
		gl = new GameLogic(this, hs);

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
		
		// SET FRAME VISIBLE
		f.setVisible(true);
		f.addKeyListener(new FrameKeyListener(gl, this));
	}
	
	// INITIALIZATION METHODS
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
		highScores = new JButton("High Scores");
		newGame = new JButton("New Game");
		help = new JButton("      Help      ");
		
		highScores.setFont(font);
		help.setFont(font);
		newGame.setFont(fontBigger);
		
		highScores.setMargin(new Insets(1,2,1,2));
		help.setMargin(new Insets(1,2,1,2));
		newGame.setMargin(new Insets(1,5,1,5));
		
		highScores.setBackground(Color.decode(enabledGridColour));
		help.setBackground(Color.decode(enabledGridColour));
		newGame.setBackground(Color.decode(enabledGridColour));
		
		highScores.setFocusable(false);
		newGame.setFocusable(false);
		help.setFocusable(false);
				
		highScores.addActionListener(new ButtonListener(this, gl, hs));
		help.addActionListener(new ButtonListener(this, gl, hs));
		newGame.addActionListener(new ButtonListener(this, gl, hs));
	}
	private void setUpImages() {
		imgTime = new JLabel(new ImageIcon(this.getClass().getResource("/images/stopwatch.png")));
		imgMine = new JLabel(new ImageIcon(this.getClass().getResource("/images/Mine2_032x032_32.png")));
		imgFlag = new JLabel(new ImageIcon(this.getClass().getResource("/images/Flag_024x024_32.png")));
		
		animationStatus = new JLabel();
		animationStatus.setForeground(Color.decode(hoverOverGridColor));
		animationStatus.setHorizontalAlignment(SwingConstants.CENTER);
		animationStatus.setText(animate ? "Animations: On" : "Animations: Off");
		
		animationStatus.setFont(gridSize.equalsIgnoreCase("small") ? font : fontBigger);
	}
	private void setUpIcons() {
		iconMine = new ImageIcon(this.getClass().getResource("/images/Mine_024x024_32.png"));
		iconFlag = new ImageIcon(this.getClass().getResource("/images/Flag_024x024_32.png"));
	}
	private void setUpTextFields() {
		tfTime = new JTextField(5);
		tfMine = new JTextField(5);
		
		tfTime.setText("00:00:00");
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
		
		pnlBottomW.add(highScores);
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
	
	// OTHER METHODS
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
