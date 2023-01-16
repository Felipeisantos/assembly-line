package br.com.felipe.productionsolution.Model;

public class Time {

    private Integer hours;
    private Integer minutes;
    private Integer seconds;

    public Time() {
        hours = 9;
        minutes = 0;
        seconds = 0;
    }

    public Time(Integer hours, Integer minutes, Integer seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public void addMinutes(Integer minutesAdd) {
        minutes += minutesAdd;
        while (minutes >= 60) {
            hours += 1;
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
}
