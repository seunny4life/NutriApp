package com.example.nutriapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import java.text.MessageFormat;

public class CardioExerciseDetailActivity extends AppCompatActivity {
    private TextView nameTextView;
    private ImageView imageView;
    private TextView durationTextView;
    private NumberPicker durationNumberPicker;
    private Button startPauseButton;
    private boolean isExerciseRunning = false;
    private CountDownTimer countDownTimer;
    private long startTimeMillis;
    private long remainingTimeMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardio_exercise_detail);

        nameTextView = findViewById(R.id.exerciseNameTextView);
        imageView = findViewById(R.id.exerciseImageView);
        durationTextView = findViewById(R.id.exerciseDurationTextView);
        durationNumberPicker = findViewById(R.id.durationNumberPicker);
        startPauseButton = findViewById(R.id.startPauseButton);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString("EXERCISE_NAME");
            int duration = extras.getInt("EXERCISE_DURATION", 0);
            int imageResId = extras.getInt("EXERCISE_IMAGE", 0);

            nameTextView.setText(name);
            imageView.setImageResource(imageResId);
            durationTextView.setText(MessageFormat.format("{0} mins", duration));

            durationNumberPicker.setMinValue(1);
            durationNumberPicker.setMaxValue(120);
            durationNumberPicker.setValue(duration);
            durationNumberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
                durationTextView.setText(MessageFormat.format("{0} mins", newVal));
            });

            startPauseButton.setOnClickListener(v -> {
                long durationMillis = (long) durationNumberPicker.getValue() * 60 * 1000;
                startOrPauseCountdown(durationMillis);
            });
        }
    }

    private void startOrPauseCountdown(long durationMillis) {
        if (isExerciseRunning) {
            // Pause the countdown
            countDownTimer.cancel();
            isExerciseRunning = false;
            startPauseButton.setText("Start");
            // Store the remaining time when paused
            remainingTimeMillis = durationMillis - (System.currentTimeMillis() - startTimeMillis);
        } else {
            // Resume the countdown or start a new one if it hasn't started yet
            if (remainingTimeMillis > 0) {
                // Resume countdown with remaining time
                startCountdown(remainingTimeMillis);
            } else {
                // Start a new countdown
                startCountdown(durationMillis);
            }
        }
    }

    private void startCountdown(long durationMillis) {
        startTimeMillis = System.currentTimeMillis();
        isExerciseRunning = true;
        startPauseButton.setText("Pause");
        countDownTimer = new CountDownTimer(durationMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateDurationTextView(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                updateDurationTextView(0);
                // Perform any actions needed when the exercise is completed
                isExerciseRunning = false;
                startPauseButton.setText("Start");
                // Navigate to ExerciseSummaryFragment
                navigateToExerciseSummary();
            }
        }.start();
    }

    private void updateDurationTextView(long millisUntilFinished) {
        long seconds = (millisUntilFinished / 1000) % 60;
        long minutes = (millisUntilFinished / (1000 * 60)) % 60;
        long hours = (millisUntilFinished / (1000 * 60 * 60)) % 24;

        String duration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        durationTextView.setText(duration);
    }

    private void navigateToExerciseSummary() {
        ExerciseSummaryFragment fragment = new ExerciseSummaryFragment();
        Bundle args = new Bundle();
        args.putString("EXERCISE_TYPE", nameTextView.getText().toString());
        args.putInt("DURATION", durationNumberPicker.getValue());
        args.putInt("CALORIES_BURNED", calculateCaloriesBurned(durationNumberPicker.getValue()));
        // Additional cardio exercise data
        args.putDouble("DISTANCE", 5.0); // Example value
        args.putInt("AVERAGE_HEART_RATE", 120); // Example value
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace the fragment_container with ExerciseSummaryFragment
        transaction.replace(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Method to calculate calories burned (example implementation)
    private int calculateCaloriesBurned(int duration) {
        // Example calculation based on duration
        return duration * 10; // Assuming 10 calories burned per minute
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the timer when the activity is destroyed
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
