package com.example.nutriapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.nutriapp.ExerciseSummaryFragment;

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
    private ExerciseSummaryFragment fragment;

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
            durationTextView.setText(String.format("%d mins", duration));

            durationNumberPicker.setMinValue(1);
            durationNumberPicker.setMaxValue(120);
            durationNumberPicker.setValue(duration);
            durationNumberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> durationTextView.setText(String.format("%d mins", newVal)));

            startPauseButton.setOnClickListener(v -> {
                long durationMillis = (long) durationNumberPicker.getValue() * 60 * 1000;
                startOrPauseCountdown(durationMillis);
            });
        }
    }

    private void startOrPauseCountdown(long durationMillis) {
        if (isExerciseRunning) {
            countDownTimer.cancel();
            isExerciseRunning = false;
            startPauseButton.setText("Start");
            remainingTimeMillis = durationMillis - (System.currentTimeMillis() - startTimeMillis);
        } else {
            if (remainingTimeMillis > 0) {
                startCountdown(remainingTimeMillis);
            } else {
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
                isExerciseRunning = false;
                startPauseButton.setText("Start");
                Log.d("CountdownTimer", "Exercise finished, navigating to summary.");
                navigateToExerciseSummary();
            }
        }.start();
    }

    private void updateDurationTextView(long millisUntilFinished) {
        long minutes = (millisUntilFinished / (1000 * 60)) % 60;
        long seconds = (millisUntilFinished / 1000) % 60;
        durationTextView.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void navigateToExerciseSummary() {
        if (isFinishing() || isDestroyed()) {
            return;
        }

        // Hide activity views to make space for the fragment
        hideActivityViews();

        // Check if a fragment is already added to the container
        ExerciseSummaryFragment existingFragment = (ExerciseSummaryFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if (existingFragment != null) {
            // Fragment is already added, no need to replace it
            return;
        }

        // Create a new instance of ExerciseSummaryFragment
        ExerciseSummaryFragment fragment = new ExerciseSummaryFragment();
        Bundle args = new Bundle();
        args.putString("EXERCISE_TYPE", nameTextView.getText().toString());
        args.putInt("DURATION", durationNumberPicker.getValue());
        args.putInt("CALORIES_BURNED", calculateCaloriesBurned(durationNumberPicker.getValue()));
        // Example values, replace with actual logic if necessary
        args.putDouble("DISTANCE", 5.0);
        args.putInt("AVERAGE_HEART_RATE", 120);
        fragment.setArguments(args);

        // Replace the fragment container with ExerciseSummaryFragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void hideActivityViews() {
        nameTextView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        durationTextView.setVisibility(View.GONE);
        durationNumberPicker.setVisibility(View.GONE);
        startPauseButton.setVisibility(View.GONE);
    }

    private int calculateCaloriesBurned(int duration) {
        return duration * 10; // Example calculation
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
