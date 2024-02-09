package com.example.nutriapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ExerciseSummaryFragment extends Fragment {

    private String exerciseType;
    private int duration;
    private int caloriesBurned;
    private double distance; // Additional field for cardio exercise
    private int averageHeartRate; // Additional field for cardio exercise

    public ExerciseSummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exerciseType = getArguments().getString("EXERCISE_TYPE");
            duration = getArguments().getInt("DURATION");
            caloriesBurned = getArguments().getInt("CALORIES_BURNED");
            // Retrieve additional cardio exercise data
            distance = getArguments().getDouble("DISTANCE");
            averageHeartRate = getArguments().getInt("AVERAGE_HEART_RATE");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView exerciseTypeTextView = view.findViewById(R.id.exerciseTypeTextView);
        TextView durationTextView = view.findViewById(R.id.durationTextView);
        TextView caloriesBurnedTextView = view.findViewById(R.id.caloriesBurnedTextView);
        TextView distanceTextView = view.findViewById(R.id.distanceTextView); // TextView for distance
        TextView heartRateTextView = view.findViewById(R.id.heartRateTextView); // TextView for heart rate

        exerciseTypeTextView.setText(exerciseType);
        durationTextView.setText(String.valueOf(duration));
        caloriesBurnedTextView.setText(String.valueOf(caloriesBurned));
        // Set cardio exercise data
        distanceTextView.setText(String.valueOf(distance));
        heartRateTextView.setText(String.valueOf(averageHeartRate));
    }
}
