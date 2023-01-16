package br.com.felipe.productionsolution.Model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Task implements Serializable {
    private static final AtomicInteger count = new AtomicInteger(0);
    private Integer id;
    private String description;
    private Integer time;

    public Task(Integer id, String description, Integer time) {
        this.id = id;
        this.description = description;
        this.time = time;
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

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", time=" + time +
                "}\n";
    }

}
