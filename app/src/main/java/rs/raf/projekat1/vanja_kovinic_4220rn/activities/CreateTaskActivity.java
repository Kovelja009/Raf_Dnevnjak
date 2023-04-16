package rs.raf.projekat1.vanja_kovinic_4220rn.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDateTime;

import rs.raf.projekat1.vanja_kovinic_4220rn.R;
import rs.raf.projekat1.vanja_kovinic_4220rn.model.Task;
import rs.raf.projekat1.vanja_kovinic_4220rn.viewmodels.RecyclerViewModel;
import timber.log.Timber;

public class CreateTaskActivity extends AppCompatActivity {
    private TextView dateTv;
    private TextView lowTasksTV;
    private TextView mediumTasksTV;
    private TextView highTasksTV;

    private TextView startTV;

    private TextView endTV;

    private EditText titleEt;
    private EditText descriptionEt;

    private Button createBtn;
    private Button cancelBtn;

    private boolean isLow = false;
    private boolean isMedium = false;
    private boolean isHigh = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Intent intent = getIntent();
        String dateString = "";
        if(intent != null){
        dateString = intent.getStringExtra(BottomNavigationActivity.DATE_STRING);
        }
        init(dateString);
    }


    private void init(String dateString){
        initView(dateString);
        initListeners();
    }

    private void initView(String dateString){
        dateTv = findViewById(R.id.dateTaskTV);
        dateTv.setText(dateString);

        lowTasksTV = findViewById(R.id.low_tvv);
        mediumTasksTV = findViewById(R.id.medium_tvv);
        highTasksTV = findViewById(R.id.high_tvv);
        startTV = findViewById(R.id.startTV);
        endTV = findViewById(R.id.endTV);
        titleEt = findViewById(R.id.titleEt);
        descriptionEt = findViewById(R.id.descriptionEt);
        createBtn = findViewById(R.id.createBtn);
        cancelBtn = findViewById(R.id.cancel_btn_create);

    }


    private void initListeners(){
        lowTasksTV.setOnClickListener(v -> {
            isLow = !isLow;
        });

        mediumTasksTV.setOnClickListener(v -> {
            isMedium = !isMedium;
        });

        highTasksTV.setOnClickListener(v -> {
            isHigh = !isHigh;
        });

        cancelBtn.setOnClickListener(v -> {
            finish();
        });

        startTV.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(CreateTaskActivity.this, (view, hourOfDay, minute) -> {
                startTV.setText(hourOfDay + ":" + minute);
            }, 0, 0, true);
            timePickerDialog.show();
        });

        endTV.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(CreateTaskActivity.this, (view, hourOfDay, minute) -> {
                endTV.setText(hourOfDay + ":" + minute);
            }, 0, 0, true);
            timePickerDialog.show();
        });

        createBtn.setOnClickListener(v -> {
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

            if(title.isEmpty() || description.isEmpty() || !start.contains(":") || !end.contains(":") ||  priority.isEmpty()){
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
}
