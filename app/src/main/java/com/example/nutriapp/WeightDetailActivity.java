package com.example.nutriapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.Serializable;
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
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_detail);
        initializeViews();
        retrieveAndSetExerciseDetails();
        startNextExercise();

        PauseResumeButton.setOnClickListener(v -> togglePauseResume());
        skipBreakButton.setOnClickListener(v -> skipBreak());

        // Initialize Vibrator
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
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
        vibrateExerciseComplete();
    }

    private void retrieveAndSetExerciseDetails() {
        Intent intent = getIntent();
        exercises = (List<Exercise>) intent.getSerializableExtra("EXERCISES");
    }

    private void startNextExercise() {
        if (currentExerciseIndex < exercises.size()) {
            Exercise exercise = exercises.get(currentExerciseIndex);
            totalSets = ((WeightsExercise) exercise).getSets();
            repsPerSet = ((WeightsExercise) exercise).getReps();
            displayExercise(exercise);
            currentExerciseIndex++;
        } else {
            // All exercises finished, start WeightSummaryActivity
            Intent intent = new Intent(this, WeightSummaryActivity.class);
            intent.putExtra("EXERCISES", (Serializable) exercises);
            startActivity(intent);
            finish(); // Finish WeightDetailActivity
        }
    }

    private void displayExercise(Exercise exercise) {
        nameTextView.setText(exercise.getName());
        imageView.setImageResource(exercise.getImageResourceId());
        benefitsTextView.setText(exercise.getBenefits());

        int durationPerSet = 20;
        int totalDurationInSeconds = durationPerSet * totalSets;

        startCountdownTimer(totalDurationInSeconds * 1000L);
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
            startCountdownTimer(20 * 1000L);
            sessionStatusTextView.setText("Set " + currentSet + " of " + totalSets);
        } else {
            isExerciseRunning = false;
            sessionStatusTextView.setText("Exercise Completed");
            startBreakBeforeNextExercise();
        }
    }

    private void startBreakBeforeNextExercise() {
        imageView.setImageResource(R.drawable.ic_drik_water);

        // Display a message indicating the break
        nameTextView.setText("Break Time");
        sessionStatusTextView.setText("Take a 40-second break before the next exercise.");

        setsAndRepsTextView.setVisibility(View.INVISIBLE);

        // Set benefits of the break time
        String breakBenefits = "Taking breaks during exercise helps prevent fatigue, injury, and allows muscles to recover.";
        benefitsTextView.setText(breakBenefits);

        new CountDownTimer(40000, 1000) {
            public void onTick(long millisUntilFinished) {
                durationTextView.setText("Break: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                durationTextView.setText("");
                sessionStatusTextView.setText("Get ready for the next exercise");
                PauseResumeButton.setVisibility(View.VISIBLE);
                skipBreakButton.setVisibility(View.VISIBLE);
                startNextExercise();
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
        if (isExerciseRunning) {
            countDownTimer.cancel();
            isExerciseRunning = false;
            PauseResumeButton.setText("Resume");
        } else {
            startCountdownTimer(remainingTimeMillis);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void vibrateExerciseComplete() {
        if (vibrator != null) {
            vibrator.vibrate(500);
        }
    }
}
