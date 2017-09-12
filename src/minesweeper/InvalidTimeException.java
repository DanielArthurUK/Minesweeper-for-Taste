package minesweeper;

/**
 * Thrown when a Time object is instantiated with invalid parameters.
 */
public class InvalidTimeException extends Exception {

    /**
     * Constructs a new InvalidTimeException with a predetermined message.
     */
    public InvalidTimeException() {
        super("The time entered is not a valid time.");
    }
    
}
