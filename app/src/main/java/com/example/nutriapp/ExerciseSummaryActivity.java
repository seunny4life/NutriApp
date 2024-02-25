package com.example.nutriapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ExerciseSummaryActivity extends AppCompatActivity {

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
            exerciseType = extras.getString("EXERCISE_TYPE");
            duration = extras.getInt("DURATION");
            caloriesBurned = extras.getInt("CALORIES_BURNED");
            distance = extras.getDouble("DISTANCE", 0.0);
            averageHeartRate = extras.getInt("AVERAGE_HEART_RATE", 0);

            displayExerciseSummary();
        }

        Button okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(v -> {
            saveWorkoutHistory();
            navigateToWorkoutHistory();
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
        finish();
    }

    private void saveWorkoutHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String json = sharedPreferences.getString("workoutHistory", null);
        Type type = new TypeToken<ArrayList<WorkoutHistoryItem>>() {}.getType();
        ArrayList<WorkoutHistoryItem> workoutHistoryItems = gson.fromJson(json, type);

        if (workoutHistoryItems == null) workoutHistoryItems = new ArrayList<>();

        WorkoutHistoryItem newItem = new WorkoutHistoryItem(exerciseType, duration, caloriesBurned, distance, averageHeartRate, getCurrentTimestamp());
        workoutHistoryItems.add(newItem);

        String updatedJson = gson.toJson(workoutHistoryItems);
        editor.putString("workoutHistory", updatedJson);
        editor.apply();
    }

    private String getCurrentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }
}
