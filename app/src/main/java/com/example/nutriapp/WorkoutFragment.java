package com.example.nutriapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WorkoutFragment extends Fragment implements DayAdapter.OnDayClickListener {

    private WorkoutManager workoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view_days);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        List<Day> days = generateDays();
        DayAdapter adapter = new DayAdapter(getContext(), days, this);
        recyclerView.setAdapter(adapter);

        // Initialize WorkoutManager
        workoutManager = new WorkoutManager();

        // Check for rest day and show a toast if necessary
        workoutManager.checkForRestDay(isRestDay -> {
            if (isRestDay) {
                showRestDayToast();
            }
        });

        return rootView;
    }

    private List<Day> generateDays() {
        List<Day> days = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        boolean isRestDay = false;
        int consecutiveWorkoutDays = 0;

        for (int i = 1; i <= daysInMonth; i++) {
            boolean isCardioDay = i % 2 == 0;
            String date = getCurrentDate();
            Day day;

            if (consecutiveWorkoutDays < 4 && !isRestDay) {
                if (isCardioDay) {
                    consecutiveWorkoutDays++;
                } else {
                    consecutiveWorkoutDays = 0;
                }
            } else {
                isCardioDay = false;
                consecutiveWorkoutDays = 0;
                isRestDay = true;
            }

            day = new Day(String.valueOf(i), isCardioDay, date);
            days.add(day);
        }
        return days;
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    public void onDayClick(String dayName, String date) {
        // Handle day click
        Fragment fragment;
        boolean isCardioDay = isCardioDayByName(dayName);
        if (isCardioDay) {
            fragment = new CardioFragment();
        } else {
            fragment = new WeightFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString("selected_day", dayName);
        bundle.putString("selected_date", date);
        fragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private boolean isCardioDayByName(String dayName) {
        // Implement your logic to determine if the day is cardio or not
        // For now, let's assume all even numbered days are cardio days
        int dayNumber = Integer.parseInt(dayName);
        return dayNumber % 2 == 0;
    }

    private void showRestDayToast() {
        Toast.makeText(getContext(), "It's recommended to take a rest day today.", Toast.LENGTH_SHORT).show();
    }
}
