package rs.raf.projekat1.vanja_kovinic_4220rn.viewpager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import rs.raf.projekat1.vanja_kovinic_4220rn.fragments.CalendarFragment;
import rs.raf.projekat1.vanja_kovinic_4220rn.activities.CreateTaskActivity;
import rs.raf.projekat1.vanja_kovinic_4220rn.fragments.TasksFragment;
import rs.raf.projekat1.vanja_kovinic_4220rn.fragments.UserFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    private final int ITEM_COUNT = 3;
    public static final int FRAGMENT_1 = 0;
    public static final int FRAGMENT_2 = 1;
    public static final int FRAGMENT_3 = 2;

    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case FRAGMENT_1: fragment = new CalendarFragment(); break;
            case FRAGMENT_2: fragment = new TasksFragment(); break;
            default: fragment = new UserFragment(); break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case FRAGMENT_1: return "1";
            case FRAGMENT_2: return "2";
            default: return "4";
        }
    }
}
