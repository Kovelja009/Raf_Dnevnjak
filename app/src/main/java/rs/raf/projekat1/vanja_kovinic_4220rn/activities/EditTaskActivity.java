package rs.raf.projekat1.vanja_kovinic_4220rn.activities;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import rs.raf.projekat1.vanja_kovinic_4220rn.R;
import rs.raf.projekat1.vanja_kovinic_4220rn.db.CalendarDBHelper;
import rs.raf.projekat1.vanja_kovinic_4220rn.model.Day;
import rs.raf.projekat1.vanja_kovinic_4220rn.model.Task;

public class EditTaskActivity extends AppCompatActivity {
    private TextView dateTv;
    private TextView lowTasksTV;
    private TextView mediumTasksTV;
    private TextView highTasksTV;

    private TextView startTV;

    private TextView endTV;

    private EditText titleEt;
    private EditText descriptionEt;

    private Button saveBtn;
    private Button cancelBtn;

    private boolean isLow = false;
    private boolean isMedium = false;
    private boolean isHigh = false;

    private LocalDate currentDate;
    private String username;
    private CalendarDBHelper dbHelper;

    private LocalDateTime og_startTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Intent intent = getIntent();
        String dateString = "";
        String startTime = "";
        if(intent != null){
            dateString = intent.getStringExtra(BottomNavigationActivity.DATE_STRING);
            startTime = intent.getStringExtra(BottomNavigationActivity.CURRENT_START_TIME);
            currentDate = Task.getLocalDateFromTimeString(startTime);
        }

        initDatabase();
        Task task = dbHelper.getTaskFromDB(username, startTime);
        og_startTime = task.getStartTime();
        init(task, dateString);
    }

    private void initDatabase(){
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String usernameString = sharedPreferences.getString(MainActivity.PREF_USERNAME, "");
        username = usernameString;
        dbHelper = CalendarDBHelper.instanceOfDatabase(null);
    }

    private void init(Task task, String dateString){
        initView(task, dateString);
        initListeners();
    }

    private void initView(Task task, String dateString){
        dateTv = findViewById(R.id.dateTaskTV1);
        dateTv.setText(dateString);

        lowTasksTV = findViewById(R.id.low_tvv1);
        mediumTasksTV = findViewById(R.id.medium_tvv1);
        highTasksTV = findViewById(R.id.high_tvv1);
        setTasksPriority(task.getPriority());

        startTV = findViewById(R.id.startTV1);
        startTV.setText(Task.parseTimeToString(task.getStartTime()));

        endTV = findViewById(R.id.endTV1);
        endTV.setText(Task.parseTimeToString(task.getEndTime()));

        titleEt = findViewById(R.id.titleEt1);
        titleEt.setText(task.getTitle());

        descriptionEt = findViewById(R.id.descriptionEt1);
        descriptionEt.setText(task.getDescription());

        saveBtn = findViewById(R.id.saveBtn);
        cancelBtn = findViewById(R.id.cancel_btn_create1);
    }
    private void initListeners(){
        lowTasksTV.setOnClickListener(v -> {
                isLow = true;
                isMedium = false;
                isHigh = false;
            colorPriority();
        });

        mediumTasksTV.setOnClickListener(v -> {
            isMedium = true;
            isLow = false;
            isHigh = false;
            colorPriority();
        });

        highTasksTV.setOnClickListener(v -> {
            isHigh = true;
            isLow = false;
            isMedium = false;

            colorPriority();
        });

        cancelBtn.setOnClickListener(v -> {
            finish();
        });

        startTV.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(EditTaskActivity.this, (view, hourOfDay, minute) -> {
                startTV.setText(hourOfDay + ":" + minute);
            }, 0, 0, true);
            timePickerDialog.show();
        });

        endTV.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(EditTaskActivity.this, (view, hourOfDay, minute) -> {
                endTV.setText(hourOfDay + ":" + minute);
            }, 0, 0, true);
            timePickerDialog.show();
        });

        saveBtn.setOnClickListener(v -> {
            if(titleEt.getText() == null || descriptionEt.getText() == null){
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = titleEt.getText().toString();
            String description = descriptionEt.getText().toString();
            String start = startTV.getText().toString();
            String end = endTV.getText().toString();
            String priority = "";
            if(isLow){
                priority = "LOW";
            }else if(isMedium){
                priority = "MEDIUM";
            }else if(isHigh){
                priority = "HIGH";
            }
            String date = Task.convertDateToStringDBFormat(dateTv.getText().toString());
            LocalDateTime startDate = Task.parseStringToTime(start, date);
            LocalDateTime endDate = Task.parseStringToTime(end, date);

            if(title.isEmpty() || description.isEmpty()){
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            if(startDate.isAfter(endDate)){
                Toast.makeText(this, "Start time must be before end time", Toast.LENGTH_SHORT).show();
                return;
            }
            //TODO check in database
            if(!check_in_database(startDate, endDate)){
                Toast.makeText(this, "Time is already taken", Toast.LENGTH_SHORT).show();
                return;
            }
            dbHelper.updateTaskInDB(title, description, Task.convertTimeToDBFromat(startDate), Task.convertTimeToDBFromat(endDate), String.valueOf(getPriority()), username, Task.convertTimeToDBFromat(og_startTime));
            finish();
        });

    }

    private int getPriority(){
        if(isLow){
            return 2;
        }else if(isMedium){
            return 1;
        }else if(isHigh){
            return 0;
        }
        return 0;
    }

    private boolean check_in_database(LocalDateTime startDate, LocalDateTime endDate){
        List<Task> tasks = dbHelper.getTasksByDayFromDB(username, currentDate);
        for(Task task : tasks){
            if(task.getStartTime().isEqual(og_startTime))
                continue;
            if(task.getStartTime().isEqual(startDate) || task.getEndTime().isEqual(endDate)
                    || task.getStartTime().isEqual(endDate) || task.getEndTime().isEqual(startDate)){
                return false;
            }
            if(task.getStartTime().isBefore(startDate) && task.getEndTime().isAfter(startDate)){
                return false;
            }
            if(task.getStartTime().isBefore(endDate) && task.getEndTime().isAfter(endDate)){
                return false;
            }
            if(task.getStartTime().isAfter(startDate) && task.getEndTime().isBefore(endDate)){
                return false;
            }
        }
        return true;
    }

    private void setTasksPriority(int priority){
        switch (priority){
            case Task.PRIORITY_LOW:
                isLow = true;
                isHigh = false;
                isMedium = false;
                break;
            case Task.PRIORITY_MEDIUM:
                isMedium = true;
                isHigh = false;
                isLow = false;
                break;
            case Task.PRIORITY_HIGH:
                isHigh = true;
                isLow = false;
                isMedium = false;
                break;
            default:
                isLow = false;
                isHigh = false;
                isMedium = false;
                break;
        }
        colorPriority();
    }

    private void colorPriority(){
        if(isLow){
            lowTasksTV.setBackgroundResource(R.color.priority_background);
            mediumTasksTV.setBackgroundResource(R.color.priority_background_unselected);
            highTasksTV.setBackgroundResource(R.color.priority_background_unselected);
        } else if (isMedium){
            lowTasksTV.setBackgroundResource(R.color.priority_background_unselected);
            mediumTasksTV.setBackgroundResource(R.color.priority_background);
            highTasksTV.setBackgroundResource(R.color.priority_background_unselected);
        } else if (isHigh){
            lowTasksTV.setBackgroundResource(R.color.priority_background_unselected);
            mediumTasksTV.setBackgroundResource(R.color.priority_background_unselected);
            highTasksTV.setBackgroundResource(R.color.priority_background);
        } else {
            lowTasksTV.setBackgroundResource(R.color.priority_background_unselected);
            mediumTasksTV.setBackgroundResource(R.color.priority_background_unselected);
            highTasksTV.setBackgroundResource(R.color.priority_background_unselected);
        }


    }



}
