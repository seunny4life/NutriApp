package com.example.nutriapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class WorkoutHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WorkoutHistoryAdapter adapter;
    private List<WorkoutHistoryItem> workoutHistoryItems;

    private WorkoutHistoryDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history);

        dbHelper = new WorkoutHistoryDbHelper(this);

        recyclerView = findViewById(R.id.workoutHistoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadWorkoutHistory();
    }

    private void loadWorkoutHistory() {
        workoutHistoryItems = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                WorkoutHistoryContract.WorkoutEntry._ID,
                WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_EXERCISE_TYPE,
                WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_DURATION,
                WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_CALORIES_BURNED,
                WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_DISTANCE,
                WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_AVERAGE_HEART_RATE,
                WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_TIMESTAMP
        };

        Cursor cursor = db.query(
                WorkoutHistoryContract.WorkoutEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(WorkoutHistoryContract.WorkoutEntry._ID));
            String exerciseType = cursor.getString(cursor.getColumnIndexOrThrow(WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_EXERCISE_TYPE));
            int duration = cursor.getInt(cursor.getColumnIndexOrThrow(WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_DURATION));
            int caloriesBurned = cursor.getInt(cursor.getColumnIndexOrThrow(WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_CALORIES_BURNED));
            double distance = cursor.getDouble(cursor.getColumnIndexOrThrow(WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_DISTANCE));
            int averageHeartRate = cursor.getInt(cursor.getColumnIndexOrThrow(WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_AVERAGE_HEART_RATE));
            String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_TIMESTAMP));

            workoutHistoryItems.add(new WorkoutHistoryItem(exerciseType, duration, caloriesBurned, distance, averageHeartRate, timestamp));
        }
        cursor.close();

        adapter = new WorkoutHistoryAdapter(workoutHistoryItems);
        recyclerView.setAdapter(adapter);
    }
}
