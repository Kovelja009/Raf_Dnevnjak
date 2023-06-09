package rs.raf.projekat1.vanja_kovinic_4220rn.model;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;


public class Task {

    private static final HashMap<String, Integer> monthMap = new HashMap<String, Integer>(){
        {
            put("january", 1);
            put("february", 2);
            put("march", 3);
            put("april", 4);
            put("may", 5);
            put("june", 6);
            put("july", 7);
            put("august", 8);
            put("september", 9);
            put("october", 10);
            put("november", 11);
            put("december", 12);
        }
    };

    public static final int PRIORITY_HIGH = 0;
    public static final int PRIORITY_MEDIUM = 1;
    public static final int PRIORITY_LOW = 2;

    private int priority;
    private String title;
    private String description;
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public Task(int priority, String title, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.priority = priority;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static String parseTimeToString(LocalDateTime time){
        return time.getHour() + ":" + time.getMinute();
    }



    public static String convertDateToStringDBFormat(String date){
        String[] parts = date.split(" ");
        int month = monthMap.get(parts[0].toLowerCase());
        int day = Integer.parseInt(parts[1].toLowerCase().replace(".",""));
        int year = Integer.parseInt(parts[2].toLowerCase().replace(".",""));
        return day + "-" + month + "-" + year;
    }

    public static String convertTimeToDBFromat(LocalDateTime time){
        return time.getDayOfMonth() + "-" + time.getMonthValue() + "-" + time.getYear() + "|" + time.getHour() + ":" + time.getMinute();
    }

    public static LocalDateTime convertTimeFromDBFormat(String time){
        String[] parts = time.split("\\|");
        String[] dateParts = parts[0].split("-");
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
        String[] timeParts = parts[1].split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        return LocalDateTime.of(year,month,day,hour,minute);
    }

    public static LocalDateTime parseStringToTime(String time, String Date){
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        parts = Date.split("-");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        return LocalDateTime.of(year,month,day,hour,minute);
    }

    public static LocalDate getLocalDateFromTimeString(String timeString){
        String[] og_parts = timeString.split("\\|");

        String[] parts = og_parts[0].split("-");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        return LocalDate.of(year,month,day);
    }

    public static String convertDateTimeToPresentString(LocalDateTime time){
        String month = monthMap.keySet().stream().filter(key -> monthMap.get(key) == time.getMonthValue()).findFirst().get().toUpperCase();

        return month + " " + time.getMonthValue() + ". " + time.getYear() + ".";
    }

    public String from_toDate(){
        return startTime.getHour() + ":" + startTime.getMinute() + " - " + endTime.getHour() + ":" + endTime.getMinute();
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
