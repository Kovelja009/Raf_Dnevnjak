package rs.raf.projekat1.vanja_kovinic_4220rn.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.time.LocalDate;

import rs.raf.projekat1.vanja_kovinic_4220rn.R;
import rs.raf.projekat1.vanja_kovinic_4220rn.db.CalendarDBHelper;
import rs.raf.projekat1.vanja_kovinic_4220rn.model.Day;
import rs.raf.projekat1.vanja_kovinic_4220rn.viewpager.TaskShowViewPagerAdapter;

public class ShowActivity extends AppCompatActivity {
    TaskShowViewPagerAdapter taskShowViewPagerAdapter;
    ViewPager viewPager;

    private String username;
    private CalendarDBHelper dbHelper;

    private LocalDate currentDate;
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_task);
//        TODO pass date as String
        Intent intent = getIntent();
        String dateString = "";
        String currDate = "";
        if(intent != null){
            dateString = intent.getStringExtra(BottomNavigationActivity.DATE_STRING);
            currDate = intent.getStringExtra(BottomNavigationActivity.CURRENT_DATE);
            position = intent.getIntExtra(BottomNavigationActivity.POSITION, 0);

            currentDate = Day.getLocalDateFromString(currDate);
        }
        initDatabase();
        init(position);
    }

    private void initDatabase(){
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String usernameString = sharedPreferences.getString(MainActivity.PREF_USERNAME, "");
        username = usernameString;
        dbHelper = CalendarDBHelper.instanceOfDatabase(null);
    }

    private void init(int position){
        viewPager = findViewById(R.id.pager);
        taskShowViewPagerAdapter = new TaskShowViewPagerAdapter(getSupportFragmentManager(), dbHelper, username, currentDate);
        viewPager.setAdapter(taskShowViewPagerAdapter);
        viewPager.setCurrentItem(position);

    }
}
