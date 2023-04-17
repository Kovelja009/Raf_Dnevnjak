package rs.raf.projekat1.vanja_kovinic_4220rn.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.util.List;

import rs.raf.projekat1.vanja_kovinic_4220rn.R;
import rs.raf.projekat1.vanja_kovinic_4220rn.activities.BottomNavigationActivity;
import rs.raf.projekat1.vanja_kovinic_4220rn.activities.EditTaskActivity;
import rs.raf.projekat1.vanja_kovinic_4220rn.model.Task;
import rs.raf.projekat1.vanja_kovinic_4220rn.recycler.task.TaskAdapter;

public class ShowTaskFragment extends Fragment {
    private TextView dateTv;
    private TextView timeTv;
    private TextView titleTv;
    private TextView descriptionTv;
    private TextView priorityTv;
    private Button editBtn;
    private Button deleteBtn;

    private Task task;



    public ShowTaskFragment(){
        super(R.layout.fragment_show_task);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        String title = args.getString(BottomNavigationActivity.TASK_TITLE);

//        TODO get task from database
        task = new Task(1, title, "descriptionnnn", LocalDateTime.now(), LocalDateTime.now());
        init(view);
    }

    private void init(View view){
        initViews(view);
//        titleTv.setText(task.getTitle());
        initListeners(view);
    }
    private void initViews(View view){
        dateTv = view.findViewById(R.id.dateTaskTVshow);
        dateTv.setText(Task.convertDateTimeToPresentString(task.getStartTime()));

        timeTv = view.findViewById(R.id.timer_show);
        timeTv.setText(task.from_toDate());

        titleTv = view.findViewById(R.id.title_show);
        titleTv.setText(task.getTitle());

        descriptionTv = view.findViewById(R.id.description_show);
        descriptionTv.setText(task.getDescription());

        priorityTv = view.findViewById(R.id.prior_view);
        priorityTv.setText(StringPriority(task.getPriority()));

        editBtn = view.findViewById(R.id.editBtn_show);
        deleteBtn = view.findViewById(R.id.delete_btn_show);
    }

    private void initListeners(View view){
        editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditTaskActivity.class);
            intent.putExtra(BottomNavigationActivity.DATE_STRING, Task.convertDateTimeToPresentString(task.getStartTime()));
            intent.putExtra(BottomNavigationActivity.TASK_TITLE, task.getTitle());
            getActivity().startActivity(intent);
        });
        deleteBtn.setOnClickListener(v -> {
            //TODO delete task from database

            Snackbar.make(view, "Delete", Snackbar.LENGTH_SHORT)
                    .setAction("Delete", v1 -> {
                        Toast.makeText(view.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    })
                    .show();
        });
    }

    private String StringPriority(int priority){
        if(priority == 2){
            return "Low";
        }else if(priority == 1){
            return "Medium";
        }else if(priority == 0){
            return "High";
        }
        return "Low";
    }


}
