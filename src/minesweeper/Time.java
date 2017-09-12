package minesweeper;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing a specific Time.
 */
public class Time {
    
    private int seconds;
    private int initSecs;
    private int minutes;
    private int initMins;
    private int hours;
    private int initHrs;

    /**
     * Create a new Time object with the specified number of seconds, minutes and hours.
     * @param hours The hours portion of the current Time
     * @param minutes The minutes portion of the current Time
     * @param seconds The seconds portion of the current Time
     * @throws InvalidTimeException Thrown if an invalid time is specified.
     */
    public Time(int hours, int minutes, int seconds) throws InvalidTimeException {
        if(seconds > 59 || minutes > 59 || seconds < 0 || minutes < 0 || hours > 99) {
            throw new InvalidTimeException();
        }
        else {
            this.seconds = seconds;
            this.minutes = minutes;
            this.hours = hours;

            initSecs = seconds;
            initMins = minutes;
            initHrs = hours;
        }
    }
    
    @Override
    public String toString() {
        return getStrHours() + ":" + getStrMinutes() + ":" + getStrSeconds();
    }

    /**
     * Get the seconds portion of the Time in String format.
     * @return The seconds portion of the Time in String format.
     */
    public String getStrSeconds() {
        return (seconds >= 10) ? Integer.toString(seconds) : "0" + Integer.toString(seconds);
    }

    /**
     * Get the minutes portion of the Time in String format.
     * @return Get the minutes portion of the Time in String format.
     */
    public String getStrMinutes() {
        return (minutes >= 10) ? Integer.toString(minutes) : "0" + Integer.toString(minutes);
    }

    /**
     * Get the hours portion of the Time in String format.
     * @return Get the hours portion of the Time in String format.
     */
    public String getStrHours() {
        return (hours >= 10) ? Integer.toString(hours) : "0" + Integer.toString(hours);
    }

    /**
     * Get the seconds portion of the Time.
     * @return Get the seconds portion of the Time.
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * Get the minutes portion of the Time.
     * @return Get the minutes portion of the Time.
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Get the hours portion of the Time.
     * @return Get the hours portion of the Time.
     */
    public int getHours() {
        return hours;
    }

    /**
     * Increment the time by 1 second.
     * @throws InvalidTimeException Thrown if invalid time encountered.
     */
    public void incrementSecond() throws InvalidTimeException {
        if(seconds < 59) {
            seconds++;
        }
        else if(seconds == 59) {
            seconds = 0;
            incrementMinute();
        }
        else {
            throw new InvalidTimeException();
        }
    }

    /**
     * Increment the time by 1 minute
     * @throws InvalidTimeException Thrown if invalid time encountered.
     */
    public void incrementMinute() throws InvalidTimeException {
        if(minutes < 59) {
            minutes++;
        }
        else if(minutes == 59) {
            minutes = 0;
            incrementHour();
        }
        else {
            throw new InvalidTimeException();
        }
    }

    /**
     * Increment the time by 1 hour.
     * @throws InvalidTimeException Thrown if invalid time encountered.
     */
    public void incrementHour() throws InvalidTimeException {
        if(hours < 99) {
            hours++;
        }
        else {
            throw new InvalidTimeException();
        }
    }

    /**
     * Decrement the time by 1 second
     * @throws InvalidTimeException Thrown if invalid time encountered.
     */
    public void decrementSecond() throws InvalidTimeException {
        if(expired()) {
            // Do nothing
        }
        else if(seconds > 0) {
            seconds--;
        }
        else if(seconds == 0) {
            seconds = 59;
            decrementMinute();
        }
        else {
            throw new InvalidTimeException();
        }
    }

    /**
     * Decrement the time by 1 minute.
     * @throws InvalidTimeException Thrown if invalid time encountered.
     */
    public void decrementMinute() throws InvalidTimeException {
        if(expired()) {
            
        }
        else if(minutes > 0) {
            minutes--;
        }
        else if(minutes == 0) {
            minutes = 59;
            decrementHour();
        }
        else {
            throw new InvalidTimeException();
        }
    }

    /**
     * Decrement the time by 1 hour.
     * @throws InvalidTimeException Thrown if invalid time encountered.
     */
    public void decrementHour() throws InvalidTimeException {
        if(expired()){
            
        }
        else if(hours > 0) {
            hours--;
        }
        else {
            throw new InvalidTimeException();
        }
    }

    /**
     * Reset the time to it's initial state (before any increments/decrements)
     */
    public void reset() {
        seconds = initSecs;
        minutes = initMins;
        hours = initHrs;
    }

    /**
     * Returns true if time is expired (00:00:00), false otherwise.
     * @return True if time is expired (00:00:00), false otherwise.
     */
    public boolean expired() {
        return seconds == 0 && minutes == 0 && hours == 0;
    }
}
