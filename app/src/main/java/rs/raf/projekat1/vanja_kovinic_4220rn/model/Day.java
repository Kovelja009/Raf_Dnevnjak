package rs.raf.projekat1.vanja_kovinic_4220rn.model;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Day {
    private List<Task> tasks;
    private LocalDate date;

    public Day(LocalDate date) {
        this.date = date;
        tasks = new ArrayList<>();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @NonNull
    @Override
    public String toString() {
        return "Day " + date.getDayOfMonth();
    }
}
