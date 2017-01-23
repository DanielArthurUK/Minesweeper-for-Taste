package minesweeper;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import uk.danielarthur.tasteapi.TasteDevice;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class WelcomeDisplay {
    
    private JFrame f;
    
    private JLabel infoLabel;
    private JTextField comPortField;
    private JButton submitButton;
    
    
    public WelcomeDisplay() {
        setUpFrame();
        setUpComponents();
        addListeners();
        f.setVisible(true);
    }
    
    private void setUpFrame() {
        f = new JFrame("Minesweeper for Taste");
        f.setLayout(new BorderLayout());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void setUpComponents() {
        infoLabel = new JLabel("Please enter the COM Port of the connected taste device:");
        comPortField = new JTextField();
        submitButton = new JButton("Submit");
        
        f.getContentPane().add(infoLabel, BorderLayout.NORTH);
        f.getContentPane().add(comPortField, BorderLayout.CENTER);
        f.getContentPane().add(submitButton, BorderLayout.SOUTH);
        
        f.pack();
    }
    
    private void addListeners() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String comPort = comPortField.getText();
                try {
                    TasteDevice td = new TasteDevice("Minesweeper Taste", comPort);
                    new Display("small", false, td);
                } catch (NoSuchPortException ex) {
                    JOptionPane.showMessageDialog(f, "Error: This port does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (PortInUseException ex) {
                    JOptionPane.showMessageDialog(f, "Error: This port is already in use.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            }
        
        });
    }
    
}
