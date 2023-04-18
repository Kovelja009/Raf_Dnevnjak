package rs.raf.projekat1.vanja_kovinic_4220rn.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import rs.raf.projekat1.vanja_kovinic_4220rn.activities.MainActivity;
import rs.raf.projekat1.vanja_kovinic_4220rn.db.CalendarDBHelper;
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

    private String username;
    private CalendarDBHelper dbHelper;

    private int position;

    public ShowTaskFragment(){
        super(R.layout.fragment_show_task);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        String startTime = args.getString(BottomNavigationActivity.CURRENT_START_TIME);
        position = args.getInt(BottomNavigationActivity.POSITION);
//        TODO get task from database
        initDatabase();
        task = dbHelper.getTaskFromDB(username, startTime);
        init(view);
    }

    private void initDatabase(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        String usernameString = sharedPreferences.getString(MainActivity.PREF_USERNAME, "");
        username = usernameString;
        dbHelper = CalendarDBHelper.instanceOfDatabase(null);
    }

    private void init(View view){
        initViews(view);
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
            intent.putExtra(BottomNavigationActivity.CURRENT_START_TIME, Task.convertTimeToDBFromat(task.getStartTime()));
            getActivity().startActivity(intent);
        });
        deleteBtn.setOnClickListener(v -> {
            //TODO delete task from database

            Snackbar.make(view, "Delete", Snackbar.LENGTH_SHORT)
                    .setAction("Delete", v1 -> {
                        dbHelper.deleteTaskFromDB(username, Task.convertTimeToDBFromat(task.getStartTime()));
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

    @Override
    public void onResume() {
        super.onResume();
        updateView(dbHelper.getTasksByDayFromDB(username, task.getStartTime().toLocalDate()).get(position));
    }

    private void updateView(Task task){

        timeTv.setText(task.from_toDate());

        titleTv.setText(task.getTitle());

        descriptionTv.setText(task.getDescription());

        priorityTv.setText(StringPriority(task.getPriority()));
    }
}
