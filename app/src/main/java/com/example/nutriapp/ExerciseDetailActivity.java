package com.example.nutriapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class ExerciseDetailActivity extends AppCompatActivity {
    private TextView nameTextView, durationTextView, sessionStatusTextView, descriptionTextView, benefitsTextView, setsAndRepsTextView;
    private ImageView imageView;
    private NumberPicker durationNumberPicker;
    private Button startPauseButton, skipBreakButton;
    private ProgressBar progressBar;
    private boolean isExerciseRunning = false;
    private CountDownTimer countDownTimer;
    private long remainingTimeMillis;
    private boolean isFirstStart = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        initializeViews();
        retrieveAndSetExerciseDetails();

        startPauseButton.setOnClickListener(v -> {
            if (isExerciseRunning) {
                pauseCountdownTimer();
            } else {
                long millisInFuture = remainingTimeMillis > 0 ? remainingTimeMillis : durationNumberPicker.getValue() * 60000L;
                startCountdownTimer(millisInFuture);
            }
        });

        durationNumberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if (!isExerciseRunning) {
                updateDurationTextView(newVal * 60000L);
            }
        });

        skipBreakButton.setOnClickListener(v -> finishExerciseEarly());
    }

    private void initializeViews() {
        nameTextView = findViewById(R.id.exerciseNameTextView);
        imageView = findViewById(R.id.exerciseImageView);
        durationTextView = findViewById(R.id.exerciseDurationTextView);
        durationNumberPicker = findViewById(R.id.durationNumberPicker);
        setsAndRepsTextView = findViewById(R.id.setsAndRepsTextView);
        startPauseButton = findViewById(R.id.startPauseButton);
        skipBreakButton = findViewById(R.id.skipBreakButton);
        sessionStatusTextView = findViewById(R.id.sessionStatusTextView);
        descriptionTextView = findViewById(R.id.exerciseDescriptionTextView);
        benefitsTextView = findViewById(R.id.exerciseBenefitsTextView);
        progressBar = findViewById(R.id.progressBar);


        durationNumberPicker.setMinValue(1);
        durationNumberPicker.setMaxValue(120);
        sessionStatusTextView.setVisibility(View.VISIBLE);
        sessionStatusTextView.setText("Ready");
    }

    private void retrieveAndSetExerciseDetails() {
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("EXERCISE_NAME");
            int imageResId = intent.getIntExtra("EXERCISE_IMAGE", 0);
            String type = intent.getStringExtra("EXERCISE_TYPE");
            String description = intent.getStringExtra("EXERCISE_DESCRIPTION");
            String benefits = intent.getStringExtra("EXERCISE_BENEFITS");
            int sets = intent.getIntExtra("EXERCISE_SETS", 3); // Default to 3 sets
            int reps = intent.getIntExtra("EXERCISE_REPS", 10); // Default to 10 reps

            nameTextView.setText(name);
            imageView.setImageResource(imageResId);
            descriptionTextView.setText(description);
            benefitsTextView.setText(benefits);

            if ("Weights".equals(type)) {
                // Display sets and reps
                setsAndRepsTextView.setText(sets + " sets of " + reps + " reps");
            } else {
                prepareCardioExercise();
            }
        }
    }

    private void prepareCardioExercise() {
        durationTextView.setVisibility(View.VISIBLE);
        durationNumberPicker.setVisibility(View.VISIBLE);
        startPauseButton.setVisibility(View.VISIBLE);
        sessionStatusTextView.setVisibility(View.VISIBLE);
        descriptionTextView.setVisibility(View.GONE);
        benefitsTextView.setVisibility(View.GONE);
        skipBreakButton.setVisibility(View.GONE); // Hide the skipBreakButton for cardio exercises.
    }

    private void prepareWeightExercise(Intent intent) {
        String description = intent.getStringExtra("EXERCISE_DESCRIPTION");
        String benefits = intent.getStringExtra("EXERCISE_BENEFITS");

        durationTextView.setVisibility(View.GONE);
        durationNumberPicker.setVisibility(View.GONE);
        startPauseButton.setVisibility(View.GONE);
        sessionStatusTextView.setVisibility(View.GONE);
        descriptionTextView.setVisibility(View.VISIBLE);
        benefitsTextView.setVisibility(View.VISIBLE);
        descriptionTextView.setText(description);
        benefitsTextView.setText(benefits);
    }

    private void startCountdownTimer(long millisInFuture) {
        sessionStatusTextView.setText("Exercise Running"); // Update status to indicate exercise has started
        sessionStatusTextView.setTextColor(ContextCompat.getColor(this, R.color.red));

        if (isFirstStart) {
            progressBar.setMax((int) millisInFuture);
            isFirstStart = false;
        }

        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
            public void onTick(long millisUntilFinished) {
                remainingTimeMillis = millisUntilFinished;
                updateDurationTextView(millisUntilFinished);
                progressBar.setProgress((int) (progressBar.getMax() - millisUntilFinished));
            }

            public void onFinish() {
                exerciseFinished();
            }
        }.start();
        isExerciseRunning = true;
        startPauseButton.setText("Pause");
    }

    private void pauseCountdownTimer() {
        countDownTimer.cancel();
        isExerciseRunning = false;
        startPauseButton.setText("Resume");
        sessionStatusTextView.setText("Exercise Paused");
        sessionStatusTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
    }

    private void updateDurationTextView(long millisUntilFinished) {
        long minutes = millisUntilFinished / 60000;
        long seconds = (millisUntilFinished % 60000) / 1000;
        durationTextView.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void exerciseFinished() {
        isExerciseRunning = false;
        startPauseButton.setText("Start");
        sessionStatusTextView.setText("Exercise Completed");
        navigateToSummary();
    }

    private void finishExerciseEarly() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isExerciseRunning = false;
        sessionStatusTextView.setText("Exercise Stopped Early");
        navigateToSummary();
    }

    private void navigateToSummary() {
        Intent intent = getIntent();
        String type = intent.getStringExtra("EXERCISE_TYPE");
        int duration = durationNumberPicker.getValue(); // Get the selected duration

        Intent summaryIntent = new Intent(ExerciseDetailActivity.this, ExerciseSummaryActivity.class);

        summaryIntent.putExtra("EXERCISE_TYPE", type);
        summaryIntent.putExtra("DURATION", duration);
        summaryIntent.putExtra("CALORIES_BURNED", calculateCaloriesBurned(duration));
        summaryIntent.putExtra("DISTANCE", calculateDistance(duration));
        summaryIntent.putExtra("AVERAGE_HEART_RATE", calculateAverageHeartRate(duration));
        startActivity(summaryIntent);
        finish();
    }

    private int calculateCaloriesBurned(int duration) {
        return duration * 10;
    }

    private double calculateDistance(int duration) {
        return duration * 0.05;
    }

    private int calculateAverageHeartRate(int duration) {
        return 150;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
