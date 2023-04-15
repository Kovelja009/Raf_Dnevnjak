package rs.raf.projekat1.vanja_kovinic_4220rn.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import rs.raf.projekat1.vanja_kovinic_4220rn.model.Day;
import rs.raf.projekat1.vanja_kovinic_4220rn.model.Task;

public class RecyclerViewModel extends ViewModel {
    private final MutableLiveData<List<Day>> days = new MutableLiveData<>();

    public RecyclerViewModel() {
        initialize();
        // We are doing this because cars.setValue in the background is first checking if the reference on the object is same
        // and if it is it will not do notifyAll. By creating a new list, we get the new reference everytime

    }


    private void initialize(){
        LocalDate date = LocalDate.now();
        List<Day> initialDays = new ArrayList<>();
        initialDays.addAll(fillStart(date));
        initialDays.addAll(currentMonth(date));
        initialDays.addAll(nextMonth(date));

        ArrayList<Day> listToSubmit = new ArrayList<>(initialDays);
        days.setValue(listToSubmit);

    }

    private List<Day> currentMonth(LocalDate date){
        List<Day> days = new ArrayList<>();

        LocalDate firstDay = date.withDayOfMonth(1);
        for(int i = 0; i < date.lengthOfMonth(); i++){
            Day day = new Day(firstDay.plusDays(i));
            // randomize if day has task
            if(ThreadLocalRandom.current().nextInt(0, 2) == 1){
                Task task = new Task(ThreadLocalRandom.current().nextInt(0, 3), "Task " + i, "Description " + i, null);
                day.getTasks().add(task);
            }
            days.add(day);
        }
        return days;
    }

    private List<Day> nextMonth(LocalDate date){
        List<Day> days = new ArrayList<>();
        LocalDate firstDay = date.plusMonths(1).withDayOfMonth(1);
        for(int i = 0; i < date.lengthOfMonth()+1; i++){
            Day day = new Day(firstDay.plusDays(i));

            // randomize if day has task
            if(ThreadLocalRandom.current().nextInt(0, 2) == 1){
                Task task = new Task(ThreadLocalRandom.current().nextInt(0, 3), "Task " + i, "Description " + i, null);
                day.getTasks().add(task);
            }
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

            // randomize if day has task
            if(ThreadLocalRandom.current().nextInt(0, 2) == 1){
                Task task = new Task(ThreadLocalRandom.current().nextInt(0, 3), "Task " + i, "Description " + i, null);
                day.getTasks().add(task);
            }

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

            // randomize if day has task
            if(ThreadLocalRandom.current().nextInt(0, 2) == 1){
                Task task = new Task(ThreadLocalRandom.current().nextInt(0, 3), "Task " + i, "Description " + i, null);
                day.getTasks().add(task);
            }

            days.add(day);
        }
        return days;
    }

    public void updateDown(){
        Day lastDay = days.getValue().get(days.getValue().size() - 1);
        LocalDate lastDate = lastDay.getDate();

        List<Day> newDays = new ArrayList<>();
        newDays.addAll(days.getValue());
        newDays.addAll(nextMonth(lastDate));
        newDays.addAll(nextMonth(lastDate.plusMonths(1)));
        ArrayList<Day> listToSubmit = new ArrayList<>(newDays);
        days.setValue(listToSubmit);

    }

    private List<Day> fillPreviousRest(LocalDate monday){
        List<Day> newDays = new ArrayList<>();
        int finishDay = monday.getDayOfMonth()-1;
        LocalDate firstDay = monday.withDayOfMonth(1);
        for(int i = 0; i < finishDay; i++){
            Day day = new Day(firstDay.plusDays(i));
            // randomize if day has task
            if(ThreadLocalRandom.current().nextInt(0, 2) == 1){
                Task task = new Task(ThreadLocalRandom.current().nextInt(0, 3), "Task " + i, "Description " + i, null);
                day.getTasks().add(task);
            }
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

    public MutableLiveData<List<Day>> getDays() {
        return days;
    }

    public static int getHighestPriority(Day day){
        int max = -1;
        for(Task task : day.getTasks())
            max = Math.max(max, task.getPriority());

        return max;
    }

}
