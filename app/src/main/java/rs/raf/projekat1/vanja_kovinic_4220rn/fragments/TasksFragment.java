package rs.raf.projekat1.vanja_kovinic_4220rn.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import rs.raf.projekat1.vanja_kovinic_4220rn.R;
import rs.raf.projekat1.vanja_kovinic_4220rn.activities.BottomNavigationActivity;
import rs.raf.projekat1.vanja_kovinic_4220rn.activities.CreateTaskActivity;
import rs.raf.projekat1.vanja_kovinic_4220rn.activities.MainActivity;
import rs.raf.projekat1.vanja_kovinic_4220rn.activities.ShowActivity;
import rs.raf.projekat1.vanja_kovinic_4220rn.db.CalendarDBHelper;
import rs.raf.projekat1.vanja_kovinic_4220rn.model.Day;
import rs.raf.projekat1.vanja_kovinic_4220rn.model.Task;
import rs.raf.projekat1.vanja_kovinic_4220rn.recycler.calendar.DayDiffItemCallback;
import rs.raf.projekat1.vanja_kovinic_4220rn.recycler.task.TaskAdapter;
import rs.raf.projekat1.vanja_kovinic_4220rn.recycler.task.TaskDiffItemCallback;
import rs.raf.projekat1.vanja_kovinic_4220rn.viewmodels.RecyclerViewModel;
import rs.raf.projekat1.vanja_kovinic_4220rn.viewpager.PagerAdapter;

public class TasksFragment extends Fragment {

    private TextView currentDateTV;
    private SwitchMaterial showPastSwitch;
    private EditText searchView;
    private TextView lowTasksTV;
    private TextView mediumTasksTV;
    private TextView highTasksTV;

    private RecyclerView recyclerView;

    private RecyclerViewModel recyclerViewModel;

    private FloatingActionButton addTaskBtn;

    private TaskAdapter taskAdapter;

    private static boolean lowTasks = false;
    private static boolean mediumTasks = false;
    private static boolean highTasks = false;

    private  String username = "";

    private CalendarDBHelper dbHelper;


    public TasksFragment() {
        super(R.layout.fragment_tasks);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewModel = new ViewModelProvider(requireActivity()).get(RecyclerViewModel.class);
        initDatabase();
        init(view);
    }

    private void initDatabase(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        String usernameString = sharedPreferences.getString(MainActivity.PREF_USERNAME, "");
        username = usernameString;
        dbHelper = CalendarDBHelper.instanceOfDatabase(null);
    }

    private void init(View view){
        initView(view);
        initRecycler(view);
        initObservers();
        initListeners();
//        TODO database init
    }

    private void initView(View view){
        recyclerView = view.findViewById(R.id.tasksRecyclerView);
        currentDateTV = view.findViewById(R.id.dateTaskTV);
        showPastSwitch = view.findViewById(R.id.task_toggle);
        searchView = view.findViewById(R.id.search_view);
        lowTasksTV = view.findViewById(R.id.low_tv);
        mediumTasksTV = view.findViewById(R.id.medium_tv);
        highTasksTV = view.findViewById(R.id.high_tv);

        addTaskBtn = view.findViewById(R.id.fab_button);
    }

    private void initRecycler(View view){
        taskAdapter = new TaskAdapter(new TaskDiffItemCallback(), task ->{
            String date = recyclerViewModel.getSelectedDay().getValue().getParsableDate();
            Intent intent = new Intent(getActivity(), ShowActivity.class);
            intent.putExtra(BottomNavigationActivity.DATE_STRING, Task.convertDateTimeToPresentString(task.getEndTime()));
            intent.putExtra(BottomNavigationActivity.CURRENT_DATE, date);
            //get position of task in list
            List<Task> tasks = dbHelper.getTasksByDayFromDB(username, task.getStartTime().toLocalDate());
            int i = 0;
            for(Task t : tasks){
                if(t.getStartTime().equals(task.getStartTime()))
                    intent.putExtra(BottomNavigationActivity.POSITION, i);
                i++;
            }

            startActivity(intent);

        }, recyclerViewModel, getActivity());
        // context od activity-a
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(taskAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        //do things
                        Toast.makeText(getActivity(), String.valueOf(viewHolder.getBindingAdapterPosition()), Toast.LENGTH_SHORT).show();
                        int pos = viewHolder.getBindingAdapterPosition();
                        List<Task> updated = recyclerViewModel.getTasks().getValue();
                        Day day = recyclerViewModel.getSelectedDay().getValue();
                        Task deleted = updated.remove(pos);
                        dbHelper.deleteTaskFromDB(username, Task.convertTimeToDBFromat(deleted.getStartTime()));
                        recyclerViewModel.getTasks().setValue(updated);
                        day.setTasks(updated);
                        recyclerViewModel.getSelectedDay().setValue(day);
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void initListeners(){
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                taskAdapter.submitList(recyclerViewModel.filterTasks(editable.toString()));
            }
        });

        lowTasksTV.setOnClickListener(view -> {
            lowTasks = !lowTasks;
            highTasks = false;
            mediumTasks = false;
            if(lowTasks)
                taskAdapter.submitList(recyclerViewModel.filterTasksByPriority(Task.PRIORITY_LOW));
            else
                taskAdapter.submitList(recyclerViewModel.getPastOrAll(true));
        });

        mediumTasksTV.setOnClickListener(view -> {
            mediumTasks = !mediumTasks;
            highTasks = false;
            lowTasks = false;
            if(mediumTasks)
                taskAdapter.submitList(recyclerViewModel.filterTasksByPriority(Task.PRIORITY_MEDIUM));
            else
                taskAdapter.submitList(recyclerViewModel.getPastOrAll(true));
        });

        highTasksTV.setOnClickListener(view -> {
            highTasks = !highTasks;
            lowTasks = false;
            mediumTasks = false;
            if(highTasks)
                taskAdapter.submitList(recyclerViewModel.filterTasksByPriority(Task.PRIORITY_HIGH));
            else
                taskAdapter.submitList(recyclerViewModel.getPastOrAll(true));
        });

        addTaskBtn.setOnClickListener(view -> {
            String date = recyclerViewModel.getSelectedDay().getValue().getParsableDate();

            Intent intent = new Intent(getActivity(), CreateTaskActivity.class);
            intent.putExtra(BottomNavigationActivity.DATE_STRING, recyclerViewModel.getSelectedDay().getValue().toString());
            intent.putExtra(BottomNavigationActivity.CURRENT_DATE, date);

            startActivity(intent);
        });
    }

    private void initObservers(){
        recyclerViewModel.getSelectedDay().observe(getViewLifecycleOwner(), day -> {
            if(day != null){
                currentDateTV.setText(day.toString());
                List<Task> tasks = dbHelper.getTasksByDayFromDB(username, day.getDate());

                taskAdapter.submitList(tasks
                        .stream()
                        .sorted(Comparator.comparing(Task::getEndTime))
                        .collect(Collectors.toList()));
//                TODO set tasks
            }
        });

        showPastSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
                // return all or only future tasks
                taskAdapter.submitList(recyclerViewModel.getPastOrAll(b));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Task> tasks = dbHelper.getTasksByDayFromDB(username, recyclerViewModel.getSelectedDay().getValue().getDate());
        recyclerViewModel.getTasks().setValue(tasks);
        recyclerViewModel.getSelectedDay().getValue().setTasks(tasks);
        taskAdapter.submitList(recyclerViewModel.getPastOrAll(showPastSwitch.isChecked()));
        taskAdapter.notifyDataSetChanged();
    }

}
