package rs.raf.projekat1.vanja_kovinic_4220rn.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import rs.raf.projekat1.vanja_kovinic_4220rn.R;
import rs.raf.projekat1.vanja_kovinic_4220rn.viewpager.TaskShowViewPagerAdapter;

public class ShowActivity extends AppCompatActivity {
    TaskShowViewPagerAdapter taskShowViewPagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_task);
//        TODO pass date as String
        Intent intent = getIntent();
        String dateString = "";
        if(intent != null){
            dateString = intent.getStringExtra(BottomNavigationActivity.DATE_STRING);
        }
        init();
    }

    private void init(){
        viewPager = findViewById(R.id.pager);
        taskShowViewPagerAdapter = new TaskShowViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(taskShowViewPagerAdapter);
    }
}
