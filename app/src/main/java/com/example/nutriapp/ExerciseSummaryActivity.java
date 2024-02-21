package com.example.nutriapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.MessageFormat;

public class ExerciseSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_summary);

        // Retrieve data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String exerciseType = extras.getString("EXERCISE_TYPE");
            int duration = extras.getInt("DURATION");
            int caloriesBurned = extras.getInt("CALORIES_BURNED");
            double distance = extras.getDouble("DISTANCE", 0.0);
            int averageHeartRate = extras.getInt("AVERAGE_HEART_RATE", 0);

            // Find views
            TextView exerciseTypeTextView = findViewById(R.id.exerciseTypeTextView);
            TextView durationTextView = findViewById(R.id.durationTextView);
            TextView caloriesBurnedTextView = findViewById(R.id.caloriesBurnedTextView);
            TextView distanceTextView = findViewById(R.id.distanceTextView);
            TextView heartRateTextView = findViewById(R.id.heartRateTextView);

            // Set values to views
            exerciseTypeTextView.setText(exerciseType);
            durationTextView.setText(MessageFormat.format("{0} mins", duration));
            caloriesBurnedTextView.setText(MessageFormat.format("{0} kcal", caloriesBurned));

            if ("Cardio".equals(exerciseType)) {
                distanceTextView.setVisibility(View.VISIBLE);
                heartRateTextView.setVisibility(View.VISIBLE);

                distanceTextView.setText(MessageFormat.format("{0} km", distance));
                heartRateTextView.setText(MessageFormat.format("{0} bpm", averageHeartRate));
            } else {
                distanceTextView.setVisibility(View.GONE);
                heartRateTextView.setVisibility(View.GONE);
            }
        }

        // Set OnClickListener for the "Ok" button
        Button okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the activity when the button is clicked
                finish();
            }
        });
    }
}
