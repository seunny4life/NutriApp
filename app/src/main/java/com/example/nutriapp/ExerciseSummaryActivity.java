package com.example.nutriapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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
            displayExerciseSummary();
        }

        Button okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(v -> navigateToWorkoutHistory());
    }

    private void displayExerciseSummary() {
        TextView exerciseTypeTextView = findViewById(R.id.exerciseTypeTextView);
        TextView durationTextView = findViewById(R.id.durationTextView);
        TextView caloriesBurnedTextView = findViewById(R.id.caloriesBurnedTextView);
        TextView distanceTextView = findViewById(R.id.distanceTextView);
        TextView heartRateTextView = findViewById(R.id.heartRateTextView);

        exerciseTypeTextView.setText(exerciseType);
        durationTextView.setText(String.format(Locale.getDefault(), "%d mins", duration));
        caloriesBurnedTextView.setText(String.format(Locale.getDefault(), "%d kcal", caloriesBurned));
        distanceTextView.setText(String.format(Locale.getDefault(), "%.2f km", distance));
        heartRateTextView.setText(String.format(Locale.getDefault(), "%d bpm", averageHeartRate));

        if (!"Cardio".equals(exerciseType)) {
            distanceTextView.setVisibility(View.GONE);
            heartRateTextView.setVisibility(View.GONE);
        }
    }

    private void navigateToWorkoutHistory() {
        // Log the intent extras before starting the WorkoutHistoryActivity
        Log.d("ExerciseSummary", "Navigating to WorkoutHistoryActivity with extras: " +
                "Exercise Type: " + exerciseType +
                ", Duration: " + duration +
                ", Calories Burned: " + caloriesBurned +
                ", Distance: " + distance +
                ", Average Heart Rate: " + averageHeartRate +
                ", Timestamp: " + getCurrentTimestamp());

        Intent intent = new Intent(ExerciseSummaryActivity.this, WorkoutHistoryActivity.class);
        intent.putExtra("EXERCISE_TYPE", exerciseType);
        intent.putExtra("DURATION", duration);
        intent.putExtra("CALORIES_BURNED", caloriesBurned);
        intent.putExtra("DISTANCE", distance);
        intent.putExtra("AVERAGE_HEART_RATE", averageHeartRate);
        intent.putExtra("TIMESTAMP", getCurrentTimestamp()); // Pass the current timestamp
        startActivity(intent);
        finish();
    }


    private String getCurrentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }
}
