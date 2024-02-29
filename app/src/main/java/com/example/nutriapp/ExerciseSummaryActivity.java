package com.example.nutriapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExerciseSummaryActivity extends AppCompatActivity {

    private String exerciseType;
    private int duration;
    private int caloriesBurned;
    private double distance;
    private int averageHeartRate;

    private WorkoutHistoryDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_summary);

        dbHelper = new WorkoutHistoryDbHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            exerciseType = extras.getString("EXERCISE_TYPE");
            duration = extras.getInt("DURATION");
            caloriesBurned = extras.getInt("CALORIES_BURNED");
            distance = extras.getDouble("DISTANCE", 0.0);
            averageHeartRate = extras.getInt("AVERAGE_HEART_RATE", 0);

            displayExerciseSummary();
        }

        Button okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWorkoutHistory();
                navigateToWorkoutHistory();
            }
        });
    }

    private void displayExerciseSummary() {
        TextView exerciseTypeTextView = findViewById(R.id.exerciseTypeTextView);
        TextView durationTextView = findViewById(R.id.durationTextView);
        TextView caloriesBurnedTextView = findViewById(R.id.caloriesBurnedTextView);
        TextView distanceTextView = findViewById(R.id.distanceTextView);
        TextView heartRateTextView = findViewById(R.id.heartRateTextView);
        TextView summaryTextView = findViewById(R.id.summaryTextView);
        TextView recommendationsTextView = findViewById(R.id.recommendationsTextView);

        exerciseTypeTextView.setText(exerciseType);
        durationTextView.setText(String.format(Locale.getDefault(), "%d mins", duration));
        caloriesBurnedTextView.setText(String.format(Locale.getDefault(), "%d kcal", caloriesBurned));
        distanceTextView.setText(String.format(Locale.getDefault(), "%.2f km", distance));
        heartRateTextView.setText(String.format(Locale.getDefault(), "%d bpm", averageHeartRate));

        // Dynamically update summary and recommendations based on the exercise data
        summaryTextView.setText("Here is a summary of your workout.");
        recommendationsTextView.setText(generateRecommendations());

        if (!"Cardio".equals(exerciseType)) {
            distanceTextView.setVisibility(View.GONE);
            heartRateTextView.setVisibility(View.GONE);
        }
    }

    private String generateRecommendations() {
        // Example logic to generate recommendations based on the workout data
        if (distance > 5) {
            return "Great distance! Try increasing your pace next time for a new challenge.";
        } else {
            return "Good job! Consider increasing your distance next time for better cardiovascular health.";
        }
    }

    private void navigateToWorkoutHistory() {
        Intent intent = new Intent(ExerciseSummaryActivity.this, WorkoutHistoryActivity.class);
        startActivity(intent);
        finish(); // Finish the ExerciseSummaryActivity after navigating
    }

    private void saveWorkoutHistory() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_EXERCISE_TYPE, exerciseType);
        values.put(WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_DURATION, duration);
        values.put(WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_CALORIES_BURNED, caloriesBurned);
        values.put(WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_DISTANCE, distance);
        values.put(WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_AVERAGE_HEART_RATE, averageHeartRate);
        values.put(WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_TIMESTAMP, getCurrentTimestamp());

        db.insert(WorkoutHistoryContract.WorkoutEntry.TABLE_NAME, null, values);
    }

    private String getCurrentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }
}
