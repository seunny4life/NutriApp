package com.example.nutriapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class WeightDetailActivity extends AppCompatActivity {
    private TextView nameTextView, durationTextView, sessionStatusTextView, benefitsTextView, setsAndRepsTextView;
    private ImageView imageView;
    private ProgressBar progressBar;
    private boolean isExerciseRunning = false;
    private Button PauseResumeButton, skipBreakButton;

    private CountDownTimer countDownTimer;
    private boolean isFirstStart = true;
    private List<Exercise> exercises;
    private int currentExerciseIndex = 0;
    private int totalSets;
    private int currentSet = 1;
    private int repsPerSet = 10;
    private long remainingTimeMillis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_detail);
        initializeViews();
        retrieveAndSetExerciseDetails();
        startNextExercise();

        // Set click listener for PauseResumeButton
        PauseResumeButton.setOnClickListener(v -> togglePauseResume());

        // Set click listener for skipBreakButton
        skipBreakButton.setOnClickListener(v -> skipBreak());
    }

    private void initializeViews() {
        nameTextView = findViewById(R.id.exerciseNameTextView);
        imageView = findViewById(R.id.exerciseImageView);
        sessionStatusTextView = findViewById(R.id.sessionStatusTextView);
        benefitsTextView = findViewById(R.id.exerciseBenefitsTextView);
        progressBar = findViewById(R.id.progressBar);
        durationTextView = findViewById(R.id.exerciseDurationTextView);
        PauseResumeButton = findViewById(R.id.PauseResumeButton);
        skipBreakButton = findViewById(R.id.skipBreakButton);
        setsAndRepsTextView = findViewById(R.id.setsAndRepsTextView);
    }

    private void retrieveAndSetExerciseDetails() {
        Intent intent = getIntent();
        exercises = (List<Exercise>) intent.getSerializableExtra("EXERCISES");
    }

    private void startNextExercise() {
        if (currentExerciseIndex < exercises.size()) {
            Exercise exercise = exercises.get(currentExerciseIndex);
            totalSets = ((WeightsExercise) exercise).getSets(); // Get adjusted sets
            repsPerSet = ((WeightsExercise) exercise).getReps(); // Get adjusted reps
            displayExercise(exercise);
            currentExerciseIndex++;
        } else {
            // All exercises have been completed
            finish();
        }
    }

    private void displayExercise(Exercise exercise) {
        nameTextView.setText(exercise.getName());
        imageView.setImageResource(exercise.getImageResourceId());
        benefitsTextView.setText(exercise.getBenefits());

        int durationPerSet = 20; // 20 seconds per set
        int totalDurationInSeconds = durationPerSet * totalSets;

        startCountdownTimer(totalDurationInSeconds * 1000L); // Convert to milliseconds

        setsAndRepsTextView.setText(totalSets + " sets of " + repsPerSet + " reps");
    }

    private void startCountdownTimer(long millisInFuture) {
        sessionStatusTextView.setText("Exercise Running");

        if (isFirstStart) {
            progressBar.setMax((int) millisInFuture);
            isFirstStart = false;
        }

        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
            public void onTick(long millisUntilFinished) {
                updateDurationTextView(millisUntilFinished);
                progressBar.setProgress((int) (progressBar.getMax() - millisUntilFinished));
                remainingTimeMillis = millisUntilFinished;
            }

            public void onFinish() {
                exerciseFinished();
            }
        }.start();

        isExerciseRunning = true;
        PauseResumeButton.setText("Pause");
    }

    private void updateDurationTextView(long millisUntilFinished) {
        long minutes = millisUntilFinished / 60000;
        long seconds = (millisUntilFinished % 60000) / 1000;
        durationTextView.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void exerciseFinished() {
        currentSet++;
        if (currentSet <= totalSets) {
            // Restart the timer for the next set
            startCountdownTimer(20 * 1000L); // 20 seconds per set
            sessionStatusTextView.setText("Set " + currentSet + " of " + totalSets);
        } else {
            isExerciseRunning = false;
            sessionStatusTextView.setText("Exercise Completed");
            startBreakBeforeNextExercise();
        }
    }

    private void startBreakBeforeNextExercise() {
        // Implement the logic for the break before the next exercise
        // Change UI elements to indicate break
        imageView.setImageResource(R.drawable.ic_drik_water);
        sessionStatusTextView.setText("Taking a break before next exercise");
        PauseResumeButton.setVisibility(View.INVISIBLE); // Hide pause/resume button during break
        skipBreakButton.setVisibility(View.VISIBLE); // skip break button during break

        // Display a message to the user
        Toast.makeText(this, "Taking a break before next exercise", Toast.LENGTH_SHORT).show();

        // Example: Show a countdown timer for the break duration
        new CountDownTimer(40000, 1000) { // 1 minute break (adjust duration as needed)
            public void onTick(long millisUntilFinished) {
                // Update UI to show remaining break time
                durationTextView.setText("Break: " + millisUntilFinished / 1000 + " seconds remaining");
            }

            public void onFinish() {
                // Break is over, start next exercise
                durationTextView.setText(""); // Clear break duration text
                sessionStatusTextView.setText("Get ready for the next exercise");
                PauseResumeButton.setVisibility(View.VISIBLE); // Show pause/resume button
                skipBreakButton.setVisibility(View.VISIBLE); // Show skip break button
                startNextExercise(); // Start next exercise
            }
        }.start();
    }

    private void skipBreak() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        startNextExercise();
    }

    private void togglePauseResume() {
        // Implement pause/resume functionality if needed
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
