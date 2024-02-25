package com.example.nutriapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class ExerciseSummaryActivity extends AppCompatActivity {

    // Variables to hold exercise data
    private String exerciseType;
    private int duration;
    private int caloriesBurned;
    private double distance;
    private int averageHeartRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_summary);

        // Retrieve exercise data from the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            exerciseType = extras.getString("EXERCISE_TYPE");
            duration = extras.getInt("DURATION");
            caloriesBurned = extras.getInt("CALORIES_BURNED");
            distance = extras.getDouble("DISTANCE", 0.0);
            averageHeartRate = extras.getInt("AVERAGE_HEART_RATE", 0);

            // Display the retrieved data
            displayExerciseSummary();
        }

        // Setup the OK button to save workout history and navigate
        Button okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(v -> {
            saveWorkoutHistory(); // Save current workout data
            navigateToWorkoutHistory(); // Navigate to the workout history activity
        });
    }

    // Displays the exercise summary information on the screen
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

        // Additional UI updates based on exercise type can be done here
    }

    // Saves the current workout session to SharedPreferences
    private void saveWorkoutHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        // Retrieve the existing workout history, add the new session, and save it
        String json = sharedPreferences.getString("workoutHistory", null);
        Type type = new TypeToken<ArrayList<WorkoutHistoryItem>>() {}.getType();
        ArrayList<WorkoutHistoryItem> workoutHistoryItems = gson.fromJson(json, type);

        if (workoutHistoryItems == null) workoutHistoryItems = new ArrayList<>();

        WorkoutHistoryItem newItem = new WorkoutHistoryItem(exerciseType, duration, caloriesBurned, distance, averageHeartRate, getCurrentTimestamp());
        workoutHistoryItems.add(newItem);

        // Serialize and save the updated workout history
        String updatedJson = gson.toJson(workoutHistoryItems);
        editor.putString("workoutHistory", updatedJson);
        editor.apply();
    }

    // Generates a timestamp for the current moment
    private String getCurrentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }

    // Navigates to the WorkoutHistoryActivity
    private void navigateToWorkoutHistory() {
        Intent intent = new Intent(ExerciseSummaryActivity.this, WorkoutHistoryActivity.class);
        startActivity(intent);
        finish(); // Close this activity
    }
}
