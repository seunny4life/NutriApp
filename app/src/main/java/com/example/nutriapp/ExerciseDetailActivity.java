
package com.example.nutriapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ExerciseDetailActivity extends AppCompatActivity {
    private TextView nameTextView, durationTextView, sessionStatusTextView, descriptionTextView, benefitsTextView, breakStatusTextView;
    private ImageView imageView;
    private NumberPicker durationNumberPicker;
    private Button startPauseButton, skipBreakButton;
    private boolean isExerciseRunning = false;
    private CountDownTimer countDownTimer;
    private long remainingTimeMillis = 0;

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
                long millisInFuture = remainingTimeMillis > 0 ? remainingTimeMillis : durationNumberPicker.getValue() * 60000L; // Convert minutes to milliseconds
                startCountdownTimer(millisInFuture);
            }
        });

        durationNumberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if (isExerciseRunning) {
                adjustRemainingTime(oldVal, newVal);
            }
        });

        skipBreakButton.setOnClickListener(v -> {
            if (!isExerciseRunning) {
                moveToNextExercise();
            }
        });
    }

    private void initializeViews() {
        nameTextView = findViewById(R.id.exerciseNameTextView);
        imageView = findViewById(R.id.exerciseImageView);
        durationTextView = findViewById(R.id.exerciseDurationTextView);
        durationNumberPicker = findViewById(R.id.durationNumberPicker);
        startPauseButton = findViewById(R.id.startPauseButton);
        sessionStatusTextView = findViewById(R.id.sessionStatusTextView);
        descriptionTextView = findViewById(R.id.exerciseDescriptionTextView);
        benefitsTextView = findViewById(R.id.exerciseBenefitsTextView);
        breakStatusTextView = findViewById(R.id.breakStatusTextView);
        skipBreakButton = findViewById(R.id.skipBreakButton);

        durationNumberPicker.setMinValue(1);
        durationNumberPicker.setMaxValue(120);
    }

    private void retrieveAndSetExerciseDetails() {
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("EXERCISE_NAME");
            int imageResId = intent.getIntExtra("EXERCISE_IMAGE", 0);
            nameTextView.setText(name);
            imageView.setImageResource(imageResId);

            String type = intent.getStringExtra("EXERCISE_TYPE");
            if ("Cardio".equals(type)) {
                prepareCardioExercise();
                String duration = intent.getStringExtra("EXERCISE_DURATION");
                durationTextView.setText("Duration: " + duration);
            } else {
                prepareWeightExercise();
                String description = intent.getStringExtra("EXERCISE_DESCRIPTION");
                String benefits = intent.getStringExtra("EXERCISE_BENEFITS");
                descriptionTextView.setText(description);
                benefitsTextView.setText(benefits);
            }
        }
    }

    private void prepareCardioExercise() {
        durationTextView.setVisibility(View.VISIBLE);
        durationNumberPicker.setVisibility(View.VISIBLE);
        startPauseButton.setVisibility(View.VISIBLE);
        sessionStatusTextView.setVisibility(View.VISIBLE); // Show session status for cardio exercise
        descriptionTextView.setVisibility(View.GONE);
        benefitsTextView.setVisibility(View.GONE);
    }

    private void prepareWeightExercise() {
        durationTextView.setVisibility(View.GONE);
        durationNumberPicker.setVisibility(View.GONE);
        startPauseButton.setVisibility(View.GONE);
        sessionStatusTextView.setVisibility(View.GONE); // Hide session status for weight exercise
        descriptionTextView.setVisibility(View.VISIBLE);
        benefitsTextView.setVisibility(View.VISIBLE);
    }

    private void startCountdownTimer(long millisInFuture) {
        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTimeMillis = millisUntilFinished;
                updateDurationTextView(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                if (isExerciseRunning) {
                    moveToNextExercise();
                } else {
                    exerciseFinished();
                }
            }
        }.start();
        isExerciseRunning = true;
        startPauseButton.setText("Pause");
        sessionStatusTextView.setText("Exercise running");
    }

    private void pauseCountdownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isExerciseRunning = false;
        startPauseButton.setText("Start");
        sessionStatusTextView.setText("Exercise paused");
    }

    private void updateDurationTextView(long millisUntilFinished) {
        long minutes = millisUntilFinished / 60000;
        long seconds = (millisUntilFinished % 60000) / 1000;
        durationTextView.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void exerciseFinished() {
        isExerciseRunning = false;
        startPauseButton.setText("Start");
        sessionStatusTextView.setText("Break time");
        breakStatusTextView.setVisibility(View.VISIBLE);
        skipBreakButton.setVisibility(View.VISIBLE);
    }

    private void adjustRemainingTime(int oldVal, int newVal) {
        long differenceMillis = (newVal - oldVal) * 60000L; // Convert minutes difference to milliseconds
        remainingTimeMillis += differenceMillis; // Adjust the remaining time

        // Cancel the existing countdown timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Check if the adjustment does not result in a negative time.
        if (remainingTimeMillis < 0) {
            remainingTimeMillis = 0;
        }

        // Directly restart the countdown timer with the adjusted remaining time,
        // avoiding changing the exercise running state.
        startCountdownTimer(remainingTimeMillis);
    }

    private void moveToNextExercise() {
        // Implement logic to move to the next exercise after the break or skip the break
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
