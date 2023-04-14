package rs.raf.projekat1.vanja_kovinic_4220rn.model;

import androidx.annotation.NonNull;

import java.util.List;

public class Day {
    private List<Task> tasks;

    private int day;

    public Day(int day, List<Task> tasks) {
        this.tasks = tasks;
        this.day = day;
    }

    public Day(int day) {
        this.day = day;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }


    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @NonNull
    @Override
    public String toString() {
        return "Day " + day;
    }
}
