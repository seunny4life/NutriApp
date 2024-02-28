package com.example.nutriapp;

import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
        // descriptionTextView.setText(exercise.getDescription());
        benefitsTextView.setText(exercise.getBenefits());

        // Start the exercise
        startCountdownTimer(10000); // Maximum duration of 10 seconds
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

    @SuppressLint("DefaultLocale")
    private void updateDurationTextView(long millisUntilFinished) {
        long minutes = millisUntilFinished / 60000;
        long seconds = (millisUntilFinished % 60000) / 1000;
        durationTextView.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void exerciseFinished() {
        isExerciseRunning = false;
        sessionStatusTextView.setText("Exercise Completed");
        startBreakBeforeNextExercise();
    }

    @SuppressLint("SetTextI18n")
    private void startBreakBeforeNextExercise() {
        // Change the ImageView to display a break image
        imageView.setImageResource(R.drawable.ic_drik_water);

        // Display a message indicating the break
        nameTextView.setText("Break Time");
        sessionStatusTextView.setText("Take a 25-second break before the next exercise.");

        // Set benefits of the break time
        String breakBenefits = "Taking breaks during exercise helps prevent fatigue, reduce the risk of injury, and allows muscles to recover.";
        benefitsTextView.setText(breakBenefits);

        // Start a countdown timer for the break duration (25 seconds)
        startCountdownTimerForBreak(25000); // 25 seconds break
    }

    private void startCountdownTimerForBreak(long breakDuration) {
        countDownTimer = new CountDownTimer(breakDuration, 1000) {
            public void onTick(long millisUntilFinished) {
                // Update the remaining break time
                updateDurationTextView(millisUntilFinished);
                remainingTimeMillis = millisUntilFinished;
            }

            public void onFinish() {
                // Finish the break and start the next exercise
                startNextExercise();
            }
        }.start();
    }

    private void skipBreak() {
        // Cancel the current break countdown timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        // Proceed to the next exercise immediately
        startNextExercise();
    }

    private void togglePauseResume() {
        if (isExerciseRunning) {
            // Pause the exercise
            pauseExercise();
        } else {
            // Resume the exercise
            resumeExercise();
        }
    }

    private void pauseExercise() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isExerciseRunning = false;
        PauseResumeButton.setText("Resume");
    }

    private void resumeExercise() {
        if (remainingTimeMillis > 0) {
            startCountdownTimer(remainingTimeMillis);
        } else {
            // If remainingTimeMillis is 0 or negative, start from the beginning
            startNextExercise();
        }
        PauseResumeButton.setText("Pause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
