package com.example.nutriapp;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class WeightsActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView exerciseTextView;
    private TextView descriptionTextView;
    private TextView timerTextView;
    private Button startPauseButton;
    private Button completeButton;
    private ImageView imageView;

    private int exerciseIndex = 0;
    private Exercise currentExercise;
    private CountDownTimer timer;
    private boolean isPaused = false;
    private boolean isWorkoutStarted = false;
    private long pausedTimeRemaining = 0;
    private long timerStartTime = 0;

    private long totalWorkoutDuration = 0;
    private boolean isWorkoutComplete = false;

    private final List<Exercise> exercises = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weights);

        // Initialize views
        titleTextView = findViewById(R.id.titleTextView);
        exerciseTextView = findViewById(R.id.exerciseTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        timerTextView = findViewById(R.id.timerTextView);
        startPauseButton = findViewById(R.id.startPauseButton);
        completeButton = findViewById(R.id.completeButton);
        imageView = findViewById(R.id.imageView);

        // Initialize exercise data
        exercises.add(new Exercise("Push-ups", "Place your hands shoulder-width apart on the floor. Lower your body until your chest nearly touches the floor. Push your body back up until your arms are fully extended.", 10, "https://media.tenor.com/gI-8qCUEko8AAAAC/pushup.gif"));
        exercises.add(new Exercise("Squats", "Stand with your feet shoulder-width apart. Lower your body as far as you can by pushing your hips back and bending your knees. Return to the starting position.", 10, "https://thumbs.gfycat.com/HeftyPartialGroundbeetle-size_restricted.gif"));
        exercises.add(new Exercise("Plank", "Start in a push-up position, then bend your elbows and rest your weight on your forearms. Hold this position for as long as you can.", 10, "https://media.tenor.com/6SOetkNbfakAAAAM/plank-abs.gif"));

        // Disable completeButton initially
        completeButton.setEnabled(false);

        // Set onClickListeners for buttons
        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isWorkoutStarted) {
                    startWorkout();
                } else {
                    pauseResumeWorkout();
                }
            }
        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWorkoutComplete) {
                    // Handle "Complete" button click after all exercises
                    // Add your logic here
                }
            }
        });
    }

    // Start the workout
    private void startWorkout() {
        exerciseIndex = 0;
        titleTextView.setText("Workout Started");
        isWorkoutStarted = true;
        completeButton.setEnabled(false);
        isPaused = false;
        startPauseButton.setText("Pause");
        startNextExercise();
    }

    // Pause or resume the workout timer
    private void pauseResumeWorkout() {
        isPaused = !isPaused;
        if (isPaused) {
            timer.cancel();
            startPauseButton.setText("Resume");
            long currentTime = System.currentTimeMillis();
            pausedTimeRemaining = totalWorkoutDuration - (currentTime - timerStartTime);
        } else {
            startPauseButton.setText("Pause");
            startNextExerciseWithRemainingTime();
        }
    }

    // Start the next exercise
    private void startNextExercise() {
        if (exerciseIndex < exercises.size()) {
            currentExercise = exercises.get(exerciseIndex);
            exerciseTextView.setText(currentExercise.getName());
            descriptionTextView.setText(currentExercise.getDescription());

            if (isPaused) {
                totalWorkoutDuration = pausedTimeRemaining;
            } else {
                totalWorkoutDuration = currentExercise.getDurationInSeconds() * 1000L;
            }

            timerTextView.setText(formatTime((int) (totalWorkoutDuration / 1000)));
            timerStartTime = System.currentTimeMillis();

            timer = new CountDownTimer(totalWorkoutDuration, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (!isPaused) {
                        totalWorkoutDuration = millisUntilFinished;
                        timerTextView.setText(formatTime((int) (totalWorkoutDuration / 1000)));
                    }
                }

                @Override
                public void onFinish() {
                    timerTextView.setText("Exercise Complete");
                    imageView.setVisibility(View.VISIBLE);
                    completeButton.setEnabled(true);
                    startPauseButton.setEnabled(false);

                    exerciseIndex++;

                    if (exerciseIndex >= exercises.size()) {
                        isWorkoutComplete = true;
                        completeButton.setEnabled(true);
                    }
                }
            }.start();
        } else {
            exerciseTextView.setText("Workout Complete");
            descriptionTextView.setText("");
            timerTextView.setText("");
            completeButton.setEnabled(false);
            startPauseButton.setEnabled(false);
            startPauseButton.setText("Start");
            isWorkoutStarted = false;
        }
    }

    // Start the next exercise with remaining time if paused
    private void startNextExerciseWithRemainingTime() {
        if (exerciseIndex < exercises.size()) {
            currentExercise = exercises.get(exerciseIndex);
            exerciseTextView.setText(currentExercise.getName());
            descriptionTextView.setText(currentExercise.getDescription());

            long remainingTime = pausedTimeRemaining;

            timer = new CountDownTimer(remainingTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (!isPaused) {
                        timerTextView.setText(formatTime((int) (millisUntilFinished / 1000)));
                    }
                }

                @Override
                public void onFinish() {
                    timerTextView.setText("Exercise Complete");
                    imageView.setVisibility(View.VISIBLE);
                    completeButton.setEnabled(true);
                    startPauseButton.setEnabled(false);

                    exerciseIndex++;

                    if (exerciseIndex >= exercises.size()) {
                        isWorkoutComplete = true;
                        completeButton.setEnabled(true);
                    }
                }
            }.start();
        } else {
            exerciseTextView.setText("Workout Complete");
            descriptionTextView.setText("");
            timerTextView.setText("");
            completeButton.setEnabled(false);
            startPauseButton.setEnabled(false);
            startPauseButton.setText("Start");
            isWorkoutStarted = false;
        }
    }

    // Complete the exercise
    private void completeExercise() {
        timer.cancel();
        completeButton.setEnabled(false);
        startNextExercise();
    }

    // Format time in MM:SS format
    @SuppressLint("DefaultLocale")
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    // Exercise class to store exercise data
    private static class Exercise {
        String name;
        String description;
        int duration;
        String imageUrl;

        Exercise(String name, String description, int duration, String imageUrl) {
            this.name = name;
            this.description = description;
            this.duration = duration;
            this.imageUrl = imageUrl;
        }

        String getName() {
            return name;
        }

        String getDescription() {
            return description;
        }

        int getDurationInSeconds() {
            return duration;
        }
    }
}
