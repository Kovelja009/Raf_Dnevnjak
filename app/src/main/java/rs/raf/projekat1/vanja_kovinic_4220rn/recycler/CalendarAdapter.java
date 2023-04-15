package rs.raf.projekat1.vanja_kovinic_4220rn.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;


import java.util.function.Consumer;

import rs.raf.projekat1.vanja_kovinic_4220rn.R;
import rs.raf.projekat1.vanja_kovinic_4220rn.model.Day;
import rs.raf.projekat1.vanja_kovinic_4220rn.model.Task;
import rs.raf.projekat1.vanja_kovinic_4220rn.viewmodels.RecyclerViewModel;

public class CalendarAdapter extends ListAdapter<Day, CalendarAdapter.ViewHolder> {
    private final Consumer<Day> onDayClicked;

    public CalendarAdapter(@NonNull DiffUtil.ItemCallback<Day> diffCallback, Consumer<Day> onDayClicked) {
        super(diffCallback);
        this.onDayClicked = onDayClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item, parent, false);
        view.getLayoutParams().height = (int) (parent.getHeight() * 0.17);

        return new ViewHolder(view, parent.getContext(), position -> {
            Day day = getItem(position);
            onDayClicked.accept(day);
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Day day = getItem(position);
        holder.bind(day);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final Context context;

        public ViewHolder(@NonNull View itemView, Context context, Consumer<Integer> onItemClicked) {
            super(itemView);
            this.context = context;
            itemView.setOnClickListener(v -> {
                if (getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                    onItemClicked.accept(getBindingAdapterPosition());
                }
            });
        }

        public void bind(Day day) {
            TextView dayTV = itemView.findViewById(R.id.calendarItemTV);
            dayTV.setText(String.valueOf(day.getDate().getDayOfMonth()));
            switch(RecyclerViewModel.getHighestPriority(day)){
                case Task.PRIORITY_LOW:
                    dayTV.setBackgroundResource(R.color.LOW_PRIORITY);
                    break;
                case Task.PRIORITY_MEDIUM:
                    dayTV.setBackgroundResource(R.color.MEDIUM_PRIORITY);
                    break;
                case Task.PRIORITY_HIGH:
                    dayTV.setBackgroundResource(R.color.HIGH_PRIORITY);
                    break;
                default:
                    dayTV.setBackgroundResource(R.color.DEFAULT);
                    break;
            }
        }

    }
}
