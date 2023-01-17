package br.com.felipe.productionsolution.Model;


import java.util.Calendar;
import java.util.Date;

public class Time {
    private Integer hours;
    private Integer minutes;
    private Integer seconds;

    public Time() {
        hours = 0;
        minutes = 0;
        seconds = 0;
    }

    public Time(Time time) {
        this.hours = time.getHours();
        this.minutes = time.getMinutes();
        this.seconds = time.getSeconds();
    }

    public Time(Integer hours, Integer minutes) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = 0;
    }

    public void addMinutes(int minutesToAdd) {
        minutes += minutesToAdd;
        while (minutes > 59) {
            hours++;
            minutes -= 60;
        }
    }

    public String getTime() {
        return hours + ":" + minutes;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getMinutes() {
        return minutes;
    }



    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    public int compareTo(Time other) {
        if (!this.hours.equals(other.getHours())) {
            return this.hours - other.getHours();
        } else if (!this.minutes.equals(other.getMinutes())) {
            return this.minutes - other.getMinutes();
        } else {
            return this.seconds - other.getSeconds();
        }
    }

    public boolean isGreater(Time time) {
        return this.compareTo(time) > 0;
    }

    public boolean isLesser(Time time) {
        return this.compareTo(time) < 0;
    }

    public boolean isEqual(Time time) {
        return this.compareTo(time) == 0;
    }

    public int getMinuteDifference(Time other) {
        int totalMinutesThis = (this.hours * 60) + this.minutes;
        int totalMinutesOther = (other.getHours() * 60) + other.getMinutes();
        return Math.abs(totalMinutesThis - totalMinutesOther);
    }

    public Date toDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, this.hours);
        cal.set(Calendar.MINUTE, this.minutes);
        cal.set(Calendar.SECOND, this.seconds);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
