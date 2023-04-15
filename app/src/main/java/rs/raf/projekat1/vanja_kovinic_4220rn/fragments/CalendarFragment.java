package rs.raf.projekat1.vanja_kovinic_4220rn.fragments;

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

import rs.raf.projekat1.vanja_kovinic_4220rn.R;
import rs.raf.projekat1.vanja_kovinic_4220rn.recycler.CalendarAdapter;
import rs.raf.projekat1.vanja_kovinic_4220rn.recycler.DayDiffItemCallback;
import rs.raf.projekat1.vanja_kovinic_4220rn.viewmodels.RecyclerViewModel;

public class CalendarFragment extends Fragment {
    private TextView currentMonthTv;
    private RecyclerView recyclerView;

    private RecyclerViewModel recyclerViewModel;
    private LinearLayout mainLayout;

    private CalendarAdapter calendarAdapter;


    public CalendarFragment() {
        super(R.layout.fragment_calendar);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewModel = new ViewModelProvider(this).get(RecyclerViewModel.class);
        init(view);
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
    }

    private void initRecycler(View view) {
        calendarAdapter = new CalendarAdapter(new DayDiffItemCallback(), day -> {
            Toast.makeText(view.getContext(), day.toString(), Toast.LENGTH_SHORT).show();
        });
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 7));
        recyclerView.setAdapter(calendarAdapter);

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
        });
    }

    private void initObservers() {
        recyclerViewModel.getDays().observe(getViewLifecycleOwner(), days -> {
            calendarAdapter.submitList(days);
        });
    }
}
