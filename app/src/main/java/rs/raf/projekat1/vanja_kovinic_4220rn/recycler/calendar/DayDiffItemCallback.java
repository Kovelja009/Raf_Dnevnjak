package rs.raf.projekat1.vanja_kovinic_4220rn.recycler.calendar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import rs.raf.projekat1.vanja_kovinic_4220rn.model.Day;

public class DayDiffItemCallback extends DiffUtil.ItemCallback<Day>{

    @Override
    public boolean areItemsTheSame(@NonNull Day oldItem, @NonNull Day newItem) {
        return oldItem.equals(newItem) && oldItem.getTasks().equals(newItem.getTasks());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Day oldItem, @NonNull Day newItem) {
         return oldItem.getDate().equals(newItem.getDate());
    }
}
