package minesweeper;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import uk.danielarthur.tasteapi.TasteDevice;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * The window shown to the user upon opening the Minesweeper Application
 */
public class WelcomeDisplay {
    
    private JFrame f;
    
    private JLabel infoLabel;
    private JTextField comPortField;
    private JButton submitButton;
    private JButton noTasteButton;
    private JPanel buttonsPanel, comPortPanel, pidPanel;
    private JTextField pid;
    private JCheckBox tasteCheck;

    /**
     * Creates and shows a new Welcome display window.
     */
    public WelcomeDisplay() {
        setUpFrame();
        setUpComponents();
        addListeners();
        checkEnabled();
        f.setVisible(true);
    }

    /**
     * Sets up some characteristics of the JFrame, such as the title and size of the window etc.
     */
    private void setUpFrame() {
        f = new JFrame("Minesweeper for Taste");
        f.setPreferredSize(new Dimension(300, 400));
        f.getContentPane().setLayout(new BorderLayout());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Sets up the components of the window, such as the text fields.
     */
    private void setUpComponents() {

        f.getContentPane().add(new JLabel("Welcome to a taste-enhanced game of Minesweeper!"), BorderLayout.NORTH);

        JPanel rest = new JPanel(new GridLayout(3,2));

        rest.add(new JLabel("Participant ID:"));
        pid = new JTextField();
        rest.add(pid);

        rest.add(new JLabel("Playing with Tastebud?"));
        tasteCheck = new JCheckBox("", true);
        rest.add(tasteCheck);

        rest.add(new JLabel("COM Port of Tastebud:"));
        comPortField = new JTextField();
        rest.add(comPortField);

        f.getContentPane().add(rest, BorderLayout.CENTER);

        submitButton = new JButton("Start");
        f.add(submitButton, BorderLayout.SOUTH);
        
        f.pack();
    }

    /**
     * Adds action listeners to each of the buttons in the window.
     */
    private void addListeners() {
        submitButton.addActionListener(ae -> {
            String comPort = comPortField.getText();
            try {
                if(tasteCheck.isSelected()) {
                    TasteDevice td = new TasteDevice("Minesweeper Taste", comPort);
                    Logger l = new Logger(pid.getText(), "C:/minesweeper-logs/");
                    l.log("Game started with Taste ENABLED.");
                    new Display("small", td, l);
                    f.setVisible(false);
                    f.dispose();
                } else {
                    Logger l = new Logger(pid.getText(), "C:/minesweeper-logs/");
                    l.log("Game started with Taste DISABLED.");
                    new Display("small", null, l);
                    f.setVisible(false);
                    f.dispose();
                }
            } catch (NoSuchPortException ex) {
                JOptionPane.showMessageDialog(f, "Error: This port does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (PortInUseException ex) {
                JOptionPane.showMessageDialog(f, "Error: This port is already in use.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        });
        tasteCheck.addActionListener(ae->{
            checkEnabled();
        });
    }

    /**
     * Checks whether the "Tastebud" checkbox is enabled and enables/disables the COM port tet field accordingly.
     */
    private void checkEnabled() {
        comPortField.setEnabled(tasteCheck.isSelected());
    }
}
