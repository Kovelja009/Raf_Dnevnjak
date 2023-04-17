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
import rs.raf.projekat1.vanja_kovinic_4220rn.activities.ShowActivity;
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
        init(view);
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
            Toast.makeText(view.getContext(), task.getTitle(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), ShowActivity.class);
            intent.putExtra(BottomNavigationActivity.DATE_STRING, Task.convertDateTimeToPresentString(task.getEndTime()));
            startActivity(intent);

        }, recyclerViewModel, getActivity());
        // context od activity-a
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(taskAdapter);
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
            if(lowTasks)
                taskAdapter.submitList(recyclerViewModel.filterTasksByPriority(Task.PRIORITY_LOW));
            else
                taskAdapter.submitList(recyclerViewModel.getPastOrAll(true));
        });

        mediumTasksTV.setOnClickListener(view -> {
            mediumTasks = !mediumTasks;
            if(mediumTasks)
                taskAdapter.submitList(recyclerViewModel.filterTasksByPriority(Task.PRIORITY_MEDIUM));
            else
                taskAdapter.submitList(recyclerViewModel.getPastOrAll(true));
        });

        highTasksTV.setOnClickListener(view -> {
            highTasks = !highTasks;
            if(highTasks)
                taskAdapter.submitList(recyclerViewModel.filterTasksByPriority(Task.PRIORITY_HIGH));
            else
                taskAdapter.submitList(recyclerViewModel.getPastOrAll(true));
        });

        addTaskBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), CreateTaskActivity.class);
            intent.putExtra(BottomNavigationActivity.DATE_STRING, recyclerViewModel.getSelectedDay().getValue().toString());
            startActivity(intent);
        });
    }

    private void initObservers(){
        recyclerViewModel.getSelectedDay().observe(getViewLifecycleOwner(), day -> {
            if(day != null){
                currentDateTV.setText(day.toString());
                taskAdapter.submitList(day.getTasks()
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


}
