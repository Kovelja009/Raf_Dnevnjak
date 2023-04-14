package rs.raf.projekat1.vanja_kovinic_4220rn.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import rs.raf.projekat1.vanja_kovinic_4220rn.model.Day;

public class RecyclerViewModel extends ViewModel {
    private final MutableLiveData<List<Day>> days = new MutableLiveData<>();
    private List<Day> daysList = new ArrayList<>();

    public RecyclerViewModel() {
        for (int i = 0; i <= 100; i++) {
            Day day = new Day(i);
            daysList.add(day);
        }
        // We are doing this because cars.setValue in the background is first checking if the reference on the object is same
        // and if it is it will not do notifyAll. By creating a new list, we get the new reference everytime
        ArrayList<Day> listToSubmit = new ArrayList<>(daysList);
        days.setValue(listToSubmit);
    }

    public MutableLiveData<List<Day>> getDays() {
        return days;
    }

}
