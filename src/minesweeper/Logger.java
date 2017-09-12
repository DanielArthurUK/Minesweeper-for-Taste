package minesweeper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;

/**
 * Class used for creating log files when a user plays a game.
 */
public class Logger {
    
    private BufferedWriter bw;
    private String participantId, loc;

    /**
     * Creates a new Logger object given a participant's ID and a directory to output the log file.
     * @param participantId The Participant's unique ID number.
     * @param loc The directory to output the log file.
     */
    public Logger(String participantId, String loc) {
        this.participantId = participantId;
        this.loc = loc;
        try {
            String fileLoc = loc + "\\participant-" + participantId + ".log";
            File f = new File(fileLoc);
            FileWriter fw;
            if (f.exists()) {
                fw = new FileWriter(f, true);
            } else {
                fw = new FileWriter(f);
            }
            bw = new BufferedWriter(fw);
            bw.write("********** New Log File **********");
            bw.newLine();
            bw.write("Participant ID: " + participantId);
            bw.newLine();
            bw.newLine();
            bw.flush();
        } catch(Exception e) {
            System.err.println("An error occurred whilst initialising the Logger:");
            e.printStackTrace();
        }
    }

    /**
     * Outputs a message to the log file at a given time.
     * @param t The time of the message.
     * @param msg The string content of the message.
     */
    public void log(Time t, String msg) {
        try {
            bw.write("Time: "+t.toString()+" Action: "+msg);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            System.err.println("An error occurred whilst printing to the error log: "+e.getMessage());
        }
    }

    /**
     * Outputs a message to the log file.
     * @param msg The String content of the message.
     */
    public void log(String msg) {
        try {
            bw.write("Action: "+msg);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            System.err.println("An error occurred whilst printing to the error log: "+e.getMessage());
        }
    }

    /**
     * Returns a new instance of the current Logger object with the same setup.
     * @return A new instance of the current Logger object with the same setup.
     */
    public Logger newInstance() {
        return new Logger(participantId, loc);
    }

}
