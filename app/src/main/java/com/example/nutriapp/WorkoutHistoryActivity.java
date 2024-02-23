package com.example.nutriapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class WorkoutHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history);

        // Retrieve exercise details from intent extras
        Intent intent = getIntent();
        if (intent != null) {
            String exerciseType = intent.getStringExtra("EXERCISE_TYPE");
            int duration = intent.getIntExtra("DURATION", 0);
            int caloriesBurned = intent.getIntExtra("CALORIES_BURNED", 0);
            double distance = intent.getDoubleExtra("DISTANCE", 0.0);
            int averageHeartRate = intent.getIntExtra("AVERAGE_HEART_RATE", 0);
            String timestamp = intent.getStringExtra("TIMESTAMP");

            // Display exercise details including timestamp
            displayExerciseDetails(exerciseType, duration, caloriesBurned, distance, averageHeartRate, timestamp);
        }
    }

    private void displayExerciseDetails(String exerciseType, int duration, int caloriesBurned, double distance, int averageHeartRate, String timestamp) {
        // Find TextViews and set their text
        TextView exerciseTypeTextView = findViewById(R.id.exerciseTypeTextView);
        TextView durationTextView = findViewById(R.id.durationTextView);
        TextView caloriesBurnedTextView = findViewById(R.id.caloriesBurnedTextView);
        TextView distanceTextView = findViewById(R.id.distanceTextView);
        TextView heartRateTextView = findViewById(R.id.heartRateTextView);
        TextView timestampTextView = findViewById(R.id.timestampTextView);

        exerciseTypeTextView.setText(exerciseType);
        durationTextView.setText(String.format(Locale.getDefault(), "%d mins", duration));
        caloriesBurnedTextView.setText(String.format(Locale.getDefault(), "%d kcal", caloriesBurned));
        distanceTextView.setText(String.format(Locale.getDefault(), "%.2f km", distance));
        heartRateTextView.setText(String.format(Locale.getDefault(), "%d bpm", averageHeartRate));
        timestampTextView.setText("Timestamp: " + timestamp);
    }
}
