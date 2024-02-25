package com.example.nutriapp;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WorkoutHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WorkoutHistoryAdapter adapter;
    private List<WorkoutHistoryItem> workoutHistoryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history);

        recyclerView = findViewById(R.id.workoutHistoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadWorkoutHistory();
        adapter = new WorkoutHistoryAdapter(workoutHistoryItems);
        recyclerView.setAdapter(adapter);

        setupSwipeToDelete();
    }

    private void loadWorkoutHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("workoutHistory", null);
        Type type = new TypeToken<ArrayList<WorkoutHistoryItem>>() {}.getType();
        workoutHistoryItems = gson.fromJson(json, type);

        if (workoutHistoryItems == null) {
            workoutHistoryItems = new ArrayList<>();
        }
    }

    private void saveWorkoutHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String updatedJson = gson.toJson(workoutHistoryItems); // Serialize updated list
        editor.putString("workoutHistory", updatedJson);
        editor.apply();
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false; // No need to implement drag & drop
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                workoutHistoryItems.remove(position); // Remove the item from the list
                adapter.notifyItemRemoved(position); // Notify the adapter of item removal
                saveWorkoutHistory(); // Save the updated list to SharedPreferences
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                // Optional: Implement custom background draw functionality on swipe
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
