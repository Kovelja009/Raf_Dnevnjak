package rs.raf.projekat1.vanja_kovinic_4220rn.recycler.task;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import rs.raf.projekat1.vanja_kovinic_4220rn.model.Day;
import rs.raf.projekat1.vanja_kovinic_4220rn.model.Task;

public class TaskDiffItemCallback extends DiffUtil.ItemCallback<Task>{
    @Override
    public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
        return oldItem.getStartTime().equals(newItem.getStartTime())
                && oldItem.getEndTime().equals(newItem.getEndTime());
    }
}
