package rs.raf.projekat1.vanja_kovinic_4220rn.model;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        return date.getDayOfMonth() + " " +date.getMonth().toString() + " " + date.getYear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Day day = (Day) o;
        return date.equals(day.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}
