package rs.raf.projekat1.vanja_kovinic_4220rn.viewpager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import rs.raf.projekat1.vanja_kovinic_4220rn.activities.BottomNavigationActivity;
import rs.raf.projekat1.vanja_kovinic_4220rn.fragments.ShowTaskFragment;

public class TaskShowViewPagerAdapter extends FragmentStatePagerAdapter {
    public TaskShowViewPagerAdapter(@NonNull FragmentManager fm){
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new ShowTaskFragment();
        Bundle args = new Bundle();
        // Our object is just an integer.
//        TODO add database entry
        String title = Integer.toString(position + 1);
        args.putString(BottomNavigationActivity.TASK_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}
