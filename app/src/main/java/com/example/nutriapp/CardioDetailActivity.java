// Import statements
package com.example.nutriapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

// Class definition
public class CardioDetailActivity extends AppCompatActivity {

    // Member variables declaration
    private TextView nameTextView, durationTextView, sessionStatusTextView;
    private ImageView imageView;
    private NumberPicker durationNumberPicker;
    private Button startPauseButton;
    private ProgressBar progressBar;
    private boolean isExerciseRunning = false;
    private CountDownTimer countDownTimer;
    private long remainingTimeMillis;
    private boolean isFirstStart = true;
    private Vibrator vibrator;
    private static final int CALORIES_BURNED_PER_MINUTE = 10;

    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardio_detail);

        // Initialize views
        initializeViews();
        // Initialize vibrator
        initVibrator();
        // Retrieve and set exercise details from intent
        retrieveAndSetExerciseDetails();
        // Set listeners for buttons and number picker
        setListeners();
    }

    // Method to initialize views
    private void initializeViews() {
        // Find views by their IDs
        nameTextView = findViewById(R.id.cardioExerciseNameTextView);
        imageView = findViewById(R.id.cardioExerciseImageView);
        durationTextView = findViewById(R.id.cardioExerciseDurationTextView);
        durationNumberPicker = findViewById(R.id.durationNumberPicker);
        startPauseButton = findViewById(R.id.startPauseButton);
        sessionStatusTextView = findViewById(R.id.sessionStatusTextView);
        progressBar = findViewById(R.id.progressBar);

        // Set number picker range
        durationNumberPicker.setMinValue(1);
        durationNumberPicker.setMaxValue(60);
        // Set session status text view to visible with default text
        sessionStatusTextView.setVisibility(View.VISIBLE);
        sessionStatusTextView.setText(R.string.ready);
    }

    // Method to initialize vibrator
    private void initVibrator() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    // Method to vibrate
    private void vibrate() {
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(100);
        }
    }

    // Method to vibrate with pattern
    private void vibratePattern(long[] pattern) {
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1));
            } else {
                vibrator.vibrate(pattern, -1);
            }
        }
    }

    // Method to retrieve and set exercise details from intent
    @SuppressLint("SetTextI18n")
    private void retrieveAndSetExerciseDetails() {
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("EXERCISE_NAME");
            int imageResId = intent.getIntExtra("EXERCISE_IMAGE", 0);
            // Check if exercise details are available
            if (name != null && imageResId != 0) {
                // Set exercise name and image
                nameTextView.setText(name);
                imageView.setImageResource(imageResId);
            } else {
                // If exercise details are missing, display a placeholder
                nameTextView.setText("Exercise Details Not Available");
                imageView.setImageResource(R.drawable.ic_exercise_placeholder);
            }
        }
    }

    // Method to set listeners for buttons and number picker
    private void setListeners() {
        // Set onClickListener for start/pause button
        startPauseButton.setOnClickListener(v -> {
            if (isExerciseRunning) {
                pauseCountdownTimer();
            } else {
                long millisInFuture = remainingTimeMillis > 0 ? remainingTimeMillis : durationNumberPicker.getValue() * 60000L;
                startCountdownTimer(millisInFuture);
            }
        });

        // Set onValueChangedListener for duration number picker
        durationNumberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if (isExerciseRunning) {
                long differenceMillis = (newVal - oldVal) * 60000L;
                remainingTimeMillis += differenceMillis;
                updateCountdownTimer(remainingTimeMillis);
            }
        });
    }

    // Method to start the countdown timer
    @SuppressLint("SetTextI18n")
    private void startCountdownTimer(long millisInFuture) {
        // Limit the duration to a maximum of 120 minutes
        millisInFuture = Math.min(millisInFuture, 120 * 60000L); // Convert 120 minutes to milliseconds

        // Trigger vibration
        vibrate(); // Vibrate for 100 milliseconds
        sessionStatusTextView.setText("Exercise Running");
        sessionStatusTextView.setTextColor(ContextCompat.getColor(this, R.color.red));

        if (isFirstStart) {
            progressBar.setMax((int) millisInFuture);
            isFirstStart = false;
        }

        // Create and start countdown timer
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

    // Method to pause the countdown timer
    @SuppressLint("SetTextI18n")
    private void pauseCountdownTimer() {
        countDownTimer.cancel();
        isExerciseRunning = false;
        startPauseButton.setText("Start");
        sessionStatusTextView.setText("Exercise Paused");
        sessionStatusTextView.setTextColor(ContextCompat.getColor(this, R.color.colorGray));
    }

    // Method to update the duration text view
    @SuppressLint("DefaultLocale")
    private void updateDurationTextView(long millisUntilFinished) {
        long minutes = millisUntilFinished / 60000;
        long seconds = (millisUntilFinished % 60000) / 1000;
        durationTextView.setText(String.format("%02d:%02d", minutes, seconds));
    }

    // Method called when exercise is finished
    @SuppressLint("SetTextI18n")
    private void exerciseFinished() {
        // Trigger vibration
        vibratePattern(new long[]{0, 200, 100, 200}); // Vibrate pattern: 200ms on, 100ms off, 200ms on
        isExerciseRunning = false;
        startPauseButton.setText(R.string.start);
        sessionStatusTextView.setText("Exercise Completed");
        navigateToSummary();
    }

    // Method to navigate to exercise summary activity
    private void navigateToSummary() {
        Intent intent = getIntent();
        String type = intent.getStringExtra("EXERCISE_TYPE");
        int duration = durationNumberPicker.getValue();

        // Create summary intent and pass relevant data
        Intent summaryIntent = new Intent(CardioDetailActivity.this, ExerciseSummaryActivity.class);
        summaryIntent.putExtra("EXERCISE_TYPE", type);
        summaryIntent.putExtra("DURATION", duration);
        summaryIntent.putExtra("CALORIES_BURNED", calculateCaloriesBurned(duration));
        summaryIntent.putExtra("DISTANCE", calculateDistance(duration));
        summaryIntent.putExtra("AVERAGE_HEART_RATE", calculateAverageHeartRate(duration));
        startActivity(summaryIntent);
        finish();
    }

    // Method to calculate calories burned
    private int calculateCaloriesBurned(int duration) {
        return duration * CALORIES_BURNED_PER_MINUTE;
    }

    // Method to calculate distance
    private double calculateDistance(int duration) {
        return duration * 0.05;
    }

    // Method to calculate average heart rate
    private int calculateAverageHeartRate(int duration) {
        return 150;
    }

    // Method to update the countdown timer
    private void updateCountdownTimer(long millisInFuture) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        // Update the maximum value of the ProgressBar
        progressBar.setMax((int) millisInFuture);
        startCountdownTimer(millisInFuture);
    }

    // onDestroy method to release resources
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (vibrator != null) {
            vibrator.cancel();
        }
    }
}
