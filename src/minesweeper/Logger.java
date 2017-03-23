package minesweeper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;

public class Logger {
    
    private BufferedWriter bw;
    
    public Logger(String participantId) throws FileNotFoundException, IOException {
        String fileLoc = "participant-"+participantId+".log";
        File f = new File(fileLoc);
        FileWriter fw;
        if(f.exists()) {
            fw = new FileWriter(f, true);
        }
        else {
            fw = new FileWriter(f);
        }
        bw = new BufferedWriter(fw);
        bw.write("********** New Log File **********");
        bw.newLine();
        bw.write("Participant ID: "+participantId);
        bw.newLine();
        bw.newLine();
        bw.flush();
    }
    
    public void log(String msg, Time t) {
        
    }
    
    public static void main(String[] args) throws Exception {
        new Logger("12345");
        new Logger("12345");
    }
    
}
