package com.example.nutriapp;

import android.content.Intent;
import android.os.Bundle;
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
        okButton.setOnClickListener(v -> saveWorkoutHistoryToFirestore());
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

    private void saveWorkoutHistoryToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Ensure the user is logged in

        // Create a timestamp with the current date and time
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());

        WorkoutHistoryItem workoutHistoryItem = new WorkoutHistoryItem(
                exerciseType,
                duration,
                caloriesBurned,
                distance,
                averageHeartRate,
                timestamp
        );

        db.collection("users").document(userId).collection("workoutHistory").add(workoutHistoryItem)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(ExerciseSummaryActivity.this, "Workout saved successfully", Toast.LENGTH_SHORT).show();
                    navigateToWorkoutHistory();
                })
                .addOnFailureListener(e -> Toast.makeText(ExerciseSummaryActivity.this, "Error saving workout", Toast.LENGTH_SHORT).show());
    }

    private void navigateToWorkoutHistory() {
        Intent intent = new Intent(ExerciseSummaryActivity.this, WorkoutHistoryActivity.class);
        startActivity(intent);
        finish(); // Optionally finish this activity
    }
}
