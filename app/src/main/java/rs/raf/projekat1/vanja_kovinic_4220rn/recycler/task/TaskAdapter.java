package rs.raf.projekat1.vanja_kovinic_4220rn.recycler.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import rs.raf.projekat1.vanja_kovinic_4220rn.R;
import rs.raf.projekat1.vanja_kovinic_4220rn.model.Task;
import rs.raf.projekat1.vanja_kovinic_4220rn.viewmodels.RecyclerViewModel;


public class TaskAdapter extends ListAdapter<Task, TaskAdapter.ViewHolder> {
    private final Consumer<Task> onTaskClicked;
    private final RecyclerViewModel recyclerViewModel;
    public TaskAdapter(@NonNull DiffUtil.ItemCallback<Task> diffCallback, Consumer<Task> onTaskClicked, RecyclerViewModel recyclerViewModel) {
        super(diffCallback);
        this.onTaskClicked = onTaskClicked;
        this.recyclerViewModel = recyclerViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
//        view.getLayoutParams().height = (int) (parent.getHeight() * 0.17);

        return new TaskAdapter.ViewHolder(view, parent.getContext(), position -> {
            Task task = getItem(position);
            onTaskClicked.accept(task);
        }, recyclerViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
        Task task = getItem(position);
        holder.bind(task);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final Context context;
        private final RecyclerViewModel recyclerViewModel;

        public ViewHolder(@NonNull View itemView, Context context, Consumer<Integer> onItemClicked, RecyclerViewModel recyclerViewModel) {
            super(itemView);
            this.context = context;
            this.recyclerViewModel = recyclerViewModel;
            itemView.setOnClickListener(v -> {
                if (getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                    onItemClicked.accept(getBindingAdapterPosition());
                }
            });
        }

        public void bind(Task task) {
            ImageView taskIV = itemView.findViewById(R.id.taskPV);
            TextView titleTV = itemView.findViewById(R.id.taskTitleTV);
            titleTV.setText(task.getTitle());

            if(task.getEndTime().isBefore(LocalDateTime.now()))
                itemView.setBackgroundResource(R.color.eva_grey);

            TextView timeTV = itemView.findViewById(R.id.timeTV);
            String time = task.parseTimeToString(task.getStartTime()) + " - " + task.parseTimeToString(task.getEndTime());
            timeTV.setText(time);

            ImageView editIV = itemView.findViewById(R.id.editPV);
            editIV.setOnClickListener(v -> {
                Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show();
            });

            ImageView deleteIV = itemView.findViewById(R.id.deletePV);
            deleteIV.setOnClickListener(v -> {
                Snackbar.make(itemView, "Delete", Snackbar.LENGTH_SHORT)
                        .setAction("Delete", v1 -> {
                            List<Task>  updated = recyclerViewModel.deleteTask(task);
                            TaskAdapter.this.submitList(updated);
                            Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                        })
                        .show();
            });

            switch(task.getPriority()){
                case Task.PRIORITY_LOW:
                    taskIV.setBackgroundResource(R.color.LOW_PRIORITY);
                    break;
                case Task.PRIORITY_MEDIUM:
                    taskIV.setBackgroundResource(R.color.MEDIUM_PRIORITY);
                    break;
                case Task.PRIORITY_HIGH:
                    taskIV.setBackgroundResource(R.color.HIGH_PRIORITY);
                    break;
                default:
                    taskIV.setBackgroundResource(R.color.DEFAULT);
                    break;
            }
        }

    }
}