package minesweeper;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Time {
    
    private int seconds;
    private int initSecs;
    private int minutes;
    private int initMins;
    private int hours;
    private int initHrs;
    
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
    
    public String getStrSeconds() {
        return (seconds >= 10) ? Integer.toString(seconds) : "0" + Integer.toString(seconds);
    }
    
    public String getStrMinutes() {
        return (minutes >= 10) ? Integer.toString(minutes) : "0" + Integer.toString(minutes);
    }
    
    public String getStrHours() {
        return (hours >= 10) ? Integer.toString(hours) : "0" + Integer.toString(hours);
    }
    
    public int getSeconds() {
        return seconds;
    }
    
    public int getMinutes() {
        return minutes;
    }
    
    public int getHours() {
        return hours;
    }
    
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
    
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
    
    public void setHours(int hours) {
        this.hours = hours;
    }
    
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
    
    public void incrementHour() throws InvalidTimeException {
        if(hours < 99) {
            hours++;
        }
        else {
            throw new InvalidTimeException();
        }
    }
    
    public void decrementSecond() throws InvalidTimeException {
        if(expired()) {
            
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
    
    public void reset() {
        seconds = initSecs;
        minutes = initMins;
        hours = initHrs;
    }
    
    public boolean expired() {
        return seconds == 0 && minutes == 0 && hours == 0;
    }
}
