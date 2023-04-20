package rs.raf.projekat1.vanja_kovinic_4220rn.viewmodels;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import rs.raf.projekat1.vanja_kovinic_4220rn.db.CalendarDBHelper;
import rs.raf.projekat1.vanja_kovinic_4220rn.model.Day;
import rs.raf.projekat1.vanja_kovinic_4220rn.model.Task;

public class RecyclerViewModel extends ViewModel {
    private final MutableLiveData<List<Day>> days = new MutableLiveData<>();

    private final MutableLiveData<String> displayMonth = new MutableLiveData<>();

    private final MutableLiveData<Day> selectedDay = new MutableLiveData<>();

    private final MutableLiveData<List<Task>> tasks = new MutableLiveData<>();

    private CalendarDBHelper dbHelper;

    private String username;
    public RecyclerViewModel() {
//        initialize();
    }


    private void initialize(){
        LocalDate date = LocalDate.now();
        List<Day> initialDays = new ArrayList<>();
        initialDays.addAll(fillStart(date));
        initialDays.addAll(currentMonth(date));
        initialDays.addAll(nextMonth(date));

        // We are doing this because cars.setValue in the background is first checking if the reference on the object is same
        // and if it is it will not do notifyAll. By creating a new list, we get the new reference everytime
        ArrayList<Day> listToSubmit = new ArrayList<>(initialDays);
        days.setValue(listToSubmit);
    }

    private List<Day> currentMonth(LocalDate date){
        List<Day> days = new ArrayList<>();

        LocalDate firstDay = date.withDayOfMonth(1);
        for(int i = 0; i < date.lengthOfMonth(); i++){
            Day day = new Day(firstDay.plusDays(i));
//            // randomize if day has task
//            if(ThreadLocalRandom.current().nextInt(0, 2) == 1){
//                Task task = new Task(ThreadLocalRandom.current().nextInt(0, 3), "Task " + i, "Description " + i, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(1));
//                day.getTasks().add(task);
//                task = new Task(ThreadLocalRandom.current().nextInt(0, 3), "Task " + i, "Description " + i, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1));
//                day.getTasks().add(task);
//            }
            List<Task> taskList = dbHelper.getTasksByDayFromDB(username, day.getDate());
            day.getTasks().addAll(taskList);
            days.add(day);
        }
        return days;
    }

    private List<Day> nextMonth(LocalDate date){
        List<Day> days = new ArrayList<>();
        LocalDate firstDay = date.plusMonths(1).withDayOfMonth(1);
        for(int i = 0; i < date.lengthOfMonth()+1; i++){
            Day day = new Day(firstDay.plusDays(i));

//            // randomize if day has task
//            if(ThreadLocalRandom.current().nextInt(0, 2) == 1){
//                Task task = new Task(ThreadLocalRandom.current().nextInt(0, 3), "Task " + i, "Description " + i, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(1));
//                day.getTasks().add(task);
//                task = new Task(ThreadLocalRandom.current().nextInt(0, 3), "Task " + i, "Description " + i, LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(1));
//                day.getTasks().add(task);
//            }
            List<Task> taskList = dbHelper.getTasksByDayFromDB(username, day.getDate());
            day.getTasks().addAll(taskList);
            days.add(day);
        }
        return days;
    }

    private List<Day> fillStart(LocalDate date){
        List<Day> days = new ArrayList<>();
        LocalDate firstDay = date.withDayOfMonth(1);
        int daysToFill = firstDay.getDayOfWeek().getValue() - 1;
        for(int i = 0; i < daysToFill; i++){
            Day day = new Day(firstDay.minusDays(daysToFill - i));

//            // randomize if day has task
//            if(ThreadLocalRandom.current().nextInt(0, 2) == 1){
//                Task task = new Task(ThreadLocalRandom.current().nextInt(0, 3), "Task " + i, "Description " + i, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(1));
//                day.getTasks().add(task);
//                task = new Task(ThreadLocalRandom.current().nextInt(0, 3), "Task " + i, "Description " + i, LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(1));
//                day.getTasks().add(task);
//            }
            List<Task> taskList = dbHelper.getTasksByDayFromDB(username, day.getDate());
            day.getTasks().addAll(taskList);
            days.add(day);
        }
        return days;
    }


    private List<Day> fillEnd(LocalDate date){
        List<Day> days = new ArrayList<>();
        LocalDate lastDay = date.withDayOfMonth(date.lengthOfMonth());
        int daysToFill = 7 - lastDay.getDayOfWeek().getValue();
        for(int i = 0; i < daysToFill; i++){
            Day day = new Day(lastDay.plusDays(i+1));

//            // randomize if day has task
//            if(ThreadLocalRandom.current().nextInt(0, 2) == 1){
//                Task task = new Task(ThreadLocalRandom.current().nextInt(0, 3), "Task " + i, "Description " + i, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(1));
//                day.getTasks().add(task);
//                task = new Task(ThreadLocalRandom.current().nextInt(0, 3), "Task " + i, "Description " + i, LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(1));
//                day.getTasks().add(task);
//            }
            List<Task> taskList = dbHelper.getTasksByDayFromDB(username, day.getDate());
            day.getTasks().addAll(taskList);
            days.add(day);
        }
        return days;
    }

    public void updateDown(){
        Day lastDay = days.getValue().get(days.getValue().size() - 1);
        LocalDate lastDate = lastDay.getDate();

        List<Day> newDays = new ArrayList<>();
        newDays.addAll(days.getValue());

        List<Day> appendDays = nextMonth(lastDate.minusDays(5));
        for(Day appendDay : appendDays){
            if(!newDays.contains(appendDay))
                newDays.add(appendDay);
        }
        ArrayList<Day> listToSubmit = new ArrayList<>(newDays);
        days.setValue(listToSubmit);

    }

    private List<Day> fillPreviousRest(LocalDate monday){
        List<Day> newDays = new ArrayList<>();
        int finishDay = monday.getDayOfMonth()-1;
        LocalDate firstDay = monday.withDayOfMonth(1);
        for(int i = 0; i < finishDay; i++){
            Day day = new Day(firstDay.plusDays(i));
//            // randomize if day has task
//            if(ThreadLocalRandom.current().nextInt(0, 2) == 1){
//                Task task = new Task(ThreadLocalRandom.current().nextInt(0, 3), "Task " + i, "Description " + i, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(1));
//                day.getTasks().add(task);
//                task = new Task(ThreadLocalRandom.current().nextInt(0, 3), "Task " + i, "Description " + i, LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(1));
//                day.getTasks().add(task);
//            }

            List<Task> taskList = dbHelper.getTasksByDayFromDB(username, day.getDate());
            day.getTasks().addAll(taskList);
            newDays.add(day);
        }

        return newDays;
    }



    public void updateUp(){
        Day firstDay = days.getValue().get(0);
        LocalDate firstDate = firstDay.getDate();

        List<Day> newDays = new ArrayList<>();
        newDays.addAll(fillStart(firstDate));
        newDays.addAll(fillPreviousRest(firstDate));
        newDays.addAll(days.getValue());
        ArrayList<Day> listToSubmit = new ArrayList<>(newDays);
        days.setValue(listToSubmit);

    }

    public static String displayMonth(LocalDate date){
        return date.getMonth().toString() + " " + date.getYear();
    }
    public void checkUpdateMonth(int pos){
        for(int i = 0; i < 7; i++){
            LocalDate date = days.getValue().get(pos+i).getDate();
            if(date.getDayOfMonth() == 1){
                displayMonth.setValue(displayMonth(date));
                return;
            }
        }
    }

    public List<Task> getPastOrAll(boolean all){
        List<Task> allTasks = selectedDay.getValue().getTasks()
                .stream()
                .sorted(Comparator.comparing(Task::getEndTime))
                .collect(Collectors.toList());
        if(all)
            return allTasks;
        else
            return allTasks.stream()
                    .filter(task -> task.getEndTime().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
    }

    public List<Task> filterTasks(String text){
        if(selectedDay.getValue() == null)
            return new ArrayList<>();
        return selectedDay.getValue().getTasks()
                .stream()
                .filter(task -> task.getTitle().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Task> filterTasksByPriority(int priority){
        if(selectedDay.getValue() == null)
            return new ArrayList<>();
        return selectedDay.getValue().getTasks()
                .stream()
                .filter(task -> task.getPriority() == priority)
                .sorted(Comparator.comparing(Task::getEndTime))
                .collect(Collectors.toList());
    }

    public List<Task> deleteTask(Task task){
        selectedDay.getValue().getTasks().remove(task);
        tasks.setValue(selectedDay.getValue().getTasks());
        dbHelper.deleteTaskFromDB(username, Task.convertTimeToDBFromat(task.getStartTime()));
        return selectedDay.getValue().getTasks()
                .stream()
                .sorted(Comparator.comparing(Task::getEndTime))
                .collect(Collectors.toList());
    }



    public MutableLiveData<List<Day>> getDays() {
        return days;
    }

    public MutableLiveData<String> getDisplayMonth() {
        return displayMonth;
    }

    public MutableLiveData<Day> getSelectedDay() {
        return selectedDay;
    }

    public MutableLiveData<List<Task>> getTasks() {
        return tasks;
    }

    public static int getHighestPriority(Day day){
        int min = 3;
        for(Task task : day.getTasks())
            min = Math.min(min, task.getPriority());

        return min;
    }

    public void setDatabase(CalendarDBHelper dbHelper, String username){
        this.dbHelper = dbHelper;
        this.username = username;
        initialize();
    }

}
