package br.com.felipe.productionsolution.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class Task implements Serializable {
    private static final AtomicInteger count = new AtomicInteger(0);
    private Integer id;
    private Time executionTime;
    private String description;
    private Integer time;

    public Task(String description, Integer time) {
        this.id = count.incrementAndGet();
        this.description = description;
        this.time = time;
        this.executionTime = null;
    }

    public Task(Task task) {
        this.id = count.incrementAndGet();
        this.description = task.getDescription();
        this.executionTime = task.getExecutionTime();
        this.time = task.getTime();
    }

    public Task() {
        id = count.incrementAndGet();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Time getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Time executionTime) {
        this.executionTime = new Time(executionTime);
    }

    public String formatedTaskOutput() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(this.getExecutionTime().toDate()) + " " + this.description + " - " + this.getTime() + "min\n";
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", executionTime=" + executionTime +
                ", description='" + description + '\'' +
                ", time=" + time +
                "}\n";
    }
}
