package com.example.nutriapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WeightFragment extends Fragment implements ExerciseAdapter.OnExerciseClickListener {

    private RecyclerView recyclerView;
    private ExerciseAdapter adapter;
    private Exercise selectedExercise; // Field to store the selected exercise

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_weight, container, false);

        // Initialize RecyclerView
        recyclerView = rootView.findViewById(R.id.recycler_view_exercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Generate and set adapter with weight exercises
        List<Exercise> weightExercises = generateWeightExercises();
        adapter = new ExerciseAdapter(getContext(), weightExercises, this);
        recyclerView.setAdapter(adapter);

        // Set onClickListener for the start weight session button
        Button startButton = rootView.findViewById(R.id.startWeightSessionButton);
        startButton.setOnClickListener(v -> startWeightSession());

        return rootView;
    }

    // Method to generate a list of weight exercises
    private List<Exercise> generateWeightExercises() {
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(new WeightsExercise("Squats", "3 sets of 12", R.drawable.pilates_rool_over_a, "Builds lower body and core strength.", "Improves flexibility and balance"));
        exercises.add(new WeightsExercise("Deadlifts", "3 sets of 10", R.drawable.drt_land_swim_a, "Targets the back, buttocks, and leg muscles.", "Helps correct posture and increases muscle mass"));
        exercises.add(new WeightsExercise("Bench Press", "3 sets of 8", R.drawable.forward_spine_stretch_a, "Strengthens the chest, shoulders, and triceps.", "Improves upper body strength"));
        return exercises;
    }

    // Handle exercise item click
    @Override
    public void onExerciseClick(Exercise exercise, int position) {
        selectedExercise = exercise; // Update the selected exercise
        adapter.highlightItem(position); // Highlight the selected item
    }

    // Method to start the weight session
    private void startWeightSession() {
        // Check if a exercise is selected
        if (selectedExercise != null) {
            // Start ExerciseDetailActivity with details of the selected exercise
            Intent intent = new Intent(getActivity(), ExerciseDetailActivity.class);
            intent.putExtra("EXERCISE_NAME", selectedExercise.getName());
            intent.putExtra("EXERCISE_IMAGE", selectedExercise.getImageResourceId());
            intent.putExtra("EXERCISE_TYPE", selectedExercise.getType());
            intent.putExtra("EXERCISE_DESCRIPTION", selectedExercise.getDescription());
            intent.putExtra("EXERCISE_BENEFITS", selectedExercise.getBenefits());
            startActivity(intent);
        } else {
            // Show a message or handle the case where no exercise is selected
        }
    }

    // Clean up any resources onDestroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Clean up any resources here
    }
}
