package minesweeper;

public class InvalidTimeException extends Exception {
    
    public InvalidTimeException() {
        super("The time entered is not a valid time.");
    }
    
}
