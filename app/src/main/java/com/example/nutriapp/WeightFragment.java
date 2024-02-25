package com.example.nutriapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
    private List<Exercise> weightExercises;
    private int currentExerciseIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weight, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view_exercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        weightExercises = generateWeightExercises();
        adapter = new ExerciseAdapter(getContext(), weightExercises, this);
        recyclerView.setAdapter(adapter);

        Button startButton = rootView.findViewById(R.id.startWeightSessionButton);
        startButton.setOnClickListener(v -> startWeightSession());

        return rootView;
    }

    private List<Exercise> generateWeightExercises() {
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(new WeightsExercise("Squats", "3, 12", R.drawable.super_cobra_stretch, "Builds lower body and core strength.", "Improves flexibility and balance."));
        exercises.add(new WeightsExercise("Deadlifts", "3, 10",R.drawable.two_straight_legs_up, "Targets the back, buttocks, and leg muscles.", "Helps correct posture and increases muscle mass."));
        exercises.add(new WeightsExercise("Bench Press", "3, 8", R.drawable.pilates_rool_over_a, "Strengthens the chest, shoulders, and triceps.", "Improves upper body strength."));
        return exercises;
    }

    @Override
    public void onExerciseClick(Exercise exercise, int position) {
        // Handle exercise click if needed
    }

    private void startWeightSession() {
        if (!weightExercises.isEmpty()) {
            startNextExercise();
        } else {
            Toast.makeText(getContext(), "No weight exercises available.", Toast.LENGTH_SHORT).show();
        }
    }

    private void startNextExercise() {
        if (currentExerciseIndex < weightExercises.size()) {
            Exercise nextExercise = weightExercises.get(currentExerciseIndex);
            navigateToExerciseDetail(nextExercise);
            currentExerciseIndex++;
        } else {
            Toast.makeText(getContext(), "Weight session completed.", Toast.LENGTH_SHORT).show();
            currentExerciseIndex = 0; // Reset index for next session
        }
    }

    private void navigateToExerciseDetail(Exercise exercise) {
        Intent intent = new Intent(getActivity(), ExerciseDetailActivity.class);
        intent.putExtra("EXERCISE_NAME", exercise.getName());
        intent.putExtra("EXERCISE_IMAGE", exercise.getImageResourceId());
        intent.putExtra("EXERCISE_TYPE", exercise.getType());
        intent.putExtra("EXERCISE_DESCRIPTION", exercise.getDescription());
        intent.putExtra("EXERCISE_BENEFITS", exercise.getBenefits());
        startActivity(intent);
    }
}
