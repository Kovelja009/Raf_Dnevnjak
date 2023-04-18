package rs.raf.projekat1.vanja_kovinic_4220rn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import rs.raf.projekat1.vanja_kovinic_4220rn.R;
import rs.raf.projekat1.vanja_kovinic_4220rn.viewpager.PagerAdapter;

public class BottomNavigationActivity extends AppCompatActivity {
    public static final String EMAIL_STRING = "email";
    public static final String USERNAME_STRING = "username";
    public static final String PASSWORD_STRING = "password";

    public static final String DATE_STRING = "date_string";

    public static final String CURRENT_DATE = "current_date";

    public static final String CURRENT_START_TIME = "current_start_time";
    public static final String TASK_TITLE = "task_title";

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        init();
    }

    private void init(){
        initViewPager();
        initNavigation();
    }

    private void initViewPager(){
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
    }

    private void initNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.getMenu().findItem(R.id.navigation_2).setEnabled(false);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                // setCurrentItem metoda viewPager samo obavesti koji je Item trenutno aktivan i onda metoda getItem u adapteru setuje odredjeni fragment za tu poziciju
                case R.id.navigation_1: viewPager.setCurrentItem(PagerAdapter.FRAGMENT_1, false); break;
//                case R.id.navigation_2: viewPager.setCurrentItem(PagerAdapter.FRAGMENT_2, true); break;
                case R.id.navigation_3: viewPager.setCurrentItem(PagerAdapter.FRAGMENT_3, false); break;
            }
            return true;
        });
    }

    public ViewPager getViewPager() {
        return viewPager;
    }
}
