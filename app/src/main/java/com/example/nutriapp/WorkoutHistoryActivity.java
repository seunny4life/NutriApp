package com.example.nutriapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class WorkoutHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history);

        // Initialize RecyclerView
        RecyclerView workoutHistoryRecyclerView = findViewById(R.id.workoutHistoryRecyclerView);
        workoutHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch workout history data
        List<WorkoutHistoryItem> workoutHistoryItems = fetchWorkoutHistoryItems();

        // Set up the RecyclerView with the Adapter
        WorkoutHistoryAdapter adapter = new WorkoutHistoryAdapter(workoutHistoryItems);
        workoutHistoryRecyclerView.setAdapter(adapter);
    }

    private List<WorkoutHistoryItem> fetchWorkoutHistoryItems() {
        // This method should fetch the actual data. For demonstration, here's some mock data.
        List<WorkoutHistoryItem> items = new ArrayList<>();
        items.add(new WorkoutHistoryItem("Running", 30, 300, 5.0, 120, "2023-01-01 10:00"));
        items.add(new WorkoutHistoryItem("Cycling", 45, 450, 20.0, 135, "2023-01-02 15:00"));
        // Add more items as needed
        return items;
    }
}
