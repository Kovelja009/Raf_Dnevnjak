package rs.raf.projekat1.vanja_kovinic_4220rn.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;

import rs.raf.projekat1.vanja_kovinic_4220rn.R;
import rs.raf.projekat1.vanja_kovinic_4220rn.activities.BottomNavigationActivity;
import rs.raf.projekat1.vanja_kovinic_4220rn.activities.MainActivity;
import rs.raf.projekat1.vanja_kovinic_4220rn.db.CalendarDBHelper;
import rs.raf.projekat1.vanja_kovinic_4220rn.recycler.calendar.CalendarAdapter;
import rs.raf.projekat1.vanja_kovinic_4220rn.recycler.calendar.DayDiffItemCallback;
import rs.raf.projekat1.vanja_kovinic_4220rn.viewmodels.RecyclerViewModel;
import rs.raf.projekat1.vanja_kovinic_4220rn.viewpager.PagerAdapter;

public class CalendarFragment extends Fragment {
    private TextView currentMonthTv;
    private RecyclerView recyclerView;

    private RecyclerViewModel recyclerViewModel;
    private LinearLayout mainLayout;

    private CalendarAdapter calendarAdapter;

    private CalendarDBHelper dbHelper;

    private String username;


    public CalendarFragment() {
        super(R.layout.fragment_calendar);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewModel = new ViewModelProvider(requireActivity()).get(RecyclerViewModel.class);
        initDatabase();
        recyclerViewModel.setDatabase(dbHelper, username);
        init(view);
    }

    private void initDatabase(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        String usernameString = sharedPreferences.getString(MainActivity.PREF_USERNAME, "");
        username = usernameString;
        dbHelper = CalendarDBHelper.instanceOfDatabase(null);
    }

    private void init(View view){
        initView(view);
        initRecycler(view);
        initObservers();
//        TODO database init
    }

    private void initView(View view){
        mainLayout = view.findViewById(R.id.recyclerMainLayout);
        recyclerView = view.findViewById(R.id.calendarRecyclerView);
        currentMonthTv = view.findViewById(R.id.monthDisplayTV);

        recyclerViewModel.getDisplayMonth().setValue(RecyclerViewModel.displayMonth(LocalDate.now()));
        currentMonthTv.setText(recyclerViewModel.getDisplayMonth().getValue());
    }

    private void initRecycler(View view) {
        calendarAdapter = new CalendarAdapter(new DayDiffItemCallback(), day -> {
            Toast.makeText(view.getContext(), day.toString(), Toast.LENGTH_SHORT).show();
            recyclerViewModel.getSelectedDay().setValue(day);
            recyclerViewModel.getTasks().setValue(day.getTasks());
            ((BottomNavigationActivity)getActivity()).getViewPager().setCurrentItem(PagerAdapter.FRAGMENT_2, false);
        });
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 7));
        recyclerView.setAdapter(calendarAdapter);

       initRecyclerListeners();
    }



    private void initRecyclerListeners() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if(!recyclerView.canScrollVertically(1)){
                    recyclerViewModel.updateDown();
                }

                if(!recyclerView.canScrollVertically(-1)){
                    recyclerViewModel.updateUp();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager glm = ((GridLayoutManager)recyclerView.getLayoutManager());
                int firstVisiblePosition = glm.findFirstVisibleItemPosition();
                recyclerViewModel.checkUpdateMonth(firstVisiblePosition);
            }
        });

    }

    private void initObservers() {
        recyclerViewModel.getDays().observe(getViewLifecycleOwner(), days -> {
            calendarAdapter.submitList(days);
        });

        recyclerViewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
            // notify calendar that color of day might have changed
            for(int i = 0; i < recyclerViewModel.getDays().getValue().size(); i++)
                if(recyclerViewModel.getDays().getValue().get(i).equals(recyclerViewModel.getSelectedDay().getValue()))
                    calendarAdapter.notifyItemChanged(i);
        });

        recyclerViewModel.getDisplayMonth().observe(getViewLifecycleOwner(), month -> {
            currentMonthTv.setText(month);
        });
    }
}
