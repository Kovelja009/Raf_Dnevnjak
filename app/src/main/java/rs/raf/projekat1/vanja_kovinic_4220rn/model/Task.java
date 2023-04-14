package rs.raf.projekat1.vanja_kovinic_4220rn.model;


import java.time.LocalDateTime;


public class Task {
    public static final int PRIORITY_HIGH = 0;
    public static final int PRIORITY_MEDIUM = 1;
    public static final int PRIORITY_LOW = 2;

    private int priority;
    private String title;
    private String description;
    private LocalDateTime dateTime;

    public Task(int priority, String title, String description, LocalDateTime dateTime) {
        this.priority = priority;
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
