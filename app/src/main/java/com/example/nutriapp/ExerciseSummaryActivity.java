package com.example.nutriapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ExerciseSummaryActivity extends AppCompatActivity {

    // Declare class-level variables
    private String exerciseType;
    private int duration;
    private int caloriesBurned;
    private double distance;
    private int averageHeartRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_summary);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Assign values to class-level variables
            exerciseType = extras.getString("EXERCISE_TYPE");
            duration = extras.getInt("DURATION");
            caloriesBurned = extras.getInt("CALORIES_BURNED");
            distance = extras.getDouble("DISTANCE", 0.0);
            averageHeartRate = extras.getInt("AVERAGE_HEART_RATE", 0);

            // Display exercise summary data
            TextView exerciseTypeTextView = findViewById(R.id.exerciseTypeTextView);
            TextView durationTextView = findViewById(R.id.durationTextView);
            TextView caloriesBurnedTextView = findViewById(R.id.caloriesBurnedTextView);
            TextView distanceTextView = findViewById(R.id.distanceTextView);
            TextView heartRateTextView = findViewById(R.id.heartRateTextView);

            exerciseTypeTextView.setText(exerciseType);
            durationTextView.setText(duration + " mins");
            caloriesBurnedTextView.setText(caloriesBurned + " kcal");
            distanceTextView.setText(distance + " km");
            heartRateTextView.setText(averageHeartRate + " bpm");

            if (!"Cardio".equals(exerciseType)) {
                distanceTextView.setVisibility(View.GONE);
                heartRateTextView.setVisibility(View.GONE);
            }
        }

        Button okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(v -> {
            // Get the current date and time
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1; // Months start from 0
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            // Create a timestamp with the current date and time
            String timestamp = dayOfMonth + "/" + month + "/" + year + " " + hourOfDay + ":" + minute;

            // Create a new Intent to start WorkoutHistoryActivity
            Intent intent = new Intent(ExerciseSummaryActivity.this, WorkoutHistoryActivity.class);
            intent.putExtra("EXERCISE_TYPE", exerciseType);
            intent.putExtra("DURATION", duration);
            intent.putExtra("CALORIES_BURNED", caloriesBurned);
            intent.putExtra("DISTANCE", distance);
            intent.putExtra("AVERAGE_HEART_RATE", averageHeartRate);
            intent.putExtra("TIMESTAMP", timestamp);
            startActivity(intent);
        });
    }
}
