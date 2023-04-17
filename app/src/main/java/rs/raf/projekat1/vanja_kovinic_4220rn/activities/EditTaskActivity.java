package rs.raf.projekat1.vanja_kovinic_4220rn.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;

import rs.raf.projekat1.vanja_kovinic_4220rn.R;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Intent intent = getIntent();
        String taskTitle = "";
        String dateString = "";
        if(intent != null){
            dateString = intent.getStringExtra(BottomNavigationActivity.DATE_STRING);
            taskTitle = intent.getStringExtra(BottomNavigationActivity.TASK_TITLE);
        }

        Task task = new Task(0, taskTitle, "dummy", LocalDateTime.now(), LocalDateTime.now());
        init(task, dateString);
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
            String date = Task.convertStringTimeToDBFormat(dateTv.getText().toString());
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

            finish();
        });

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
