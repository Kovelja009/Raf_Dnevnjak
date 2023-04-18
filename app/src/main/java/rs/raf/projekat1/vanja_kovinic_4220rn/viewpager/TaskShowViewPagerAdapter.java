package rs.raf.projekat1.vanja_kovinic_4220rn.viewpager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.time.LocalDate;

import rs.raf.projekat1.vanja_kovinic_4220rn.activities.BottomNavigationActivity;
import rs.raf.projekat1.vanja_kovinic_4220rn.db.CalendarDBHelper;
import rs.raf.projekat1.vanja_kovinic_4220rn.fragments.ShowTaskFragment;
import rs.raf.projekat1.vanja_kovinic_4220rn.model.Task;

public class TaskShowViewPagerAdapter extends FragmentStatePagerAdapter {
    private CalendarDBHelper dbHelper;
    private String username;

    private LocalDate currDate;


    public TaskShowViewPagerAdapter(@NonNull FragmentManager fm, CalendarDBHelper dbHelper, String username, LocalDate currDate) {
        super(fm);
        this.dbHelper = dbHelper;
        this.username = username;
        this.currDate = currDate;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new ShowTaskFragment();
        Bundle args = new Bundle();
//        TODO add database entry
        Task taks = dbHelper.getTasksByDayFromDB(username, currDate).get(position);

        args.putString(BottomNavigationActivity.CURRENT_START_TIME, Task.convertTimeToDBFromat(taks.getStartTime()));
        args.putInt(BottomNavigationActivity.POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return dbHelper.getTasksByDayFromDB(username, currDate).size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}
