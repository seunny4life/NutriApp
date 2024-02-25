package com.example.nutriapp;

import android.content.Context;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
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
    private Exercise selectedExercise; // To store the selected exercise

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weight, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view_exercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        List<Exercise> weightExercises = generateWeightExercises();
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
        selectedExercise = exercise;
        if (exercise instanceof WeightsExercise) {
            showAdjustRepsSetsDialog((WeightsExercise) exercise, position);
        }
    }

    private void startWeightSession() {
        if (selectedExercise != null) {
            Intent intent = new Intent(getActivity(), ExerciseDetailActivity.class);
            intent.putExtra("EXERCISE_NAME", selectedExercise.getName());
            intent.putExtra("EXERCISE_IMAGE", selectedExercise.getImageResourceId());
            intent.putExtra("EXERCISE_TYPE", selectedExercise.getType());
            intent.putExtra("EXERCISE_DESCRIPTION", selectedExercise.getDescription());
            intent.putExtra("EXERCISE_BENEFITS", selectedExercise.getBenefits());
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "Please select an exercise first.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAdjustRepsSetsDialog(WeightsExercise exercise, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_adjust_reps_sets, null);

        final NumberPicker setsPicker = dialogView.findViewById(R.id.setsPicker);
        setsPicker.setMaxValue(20);
        setsPicker.setMinValue(1);
        setsPicker.setValue(exercise.getSets());

        final NumberPicker repsPicker = dialogView.findViewById(R.id.repsPicker);
        repsPicker.setMaxValue(50);
        repsPicker.setMinValue(1);
        repsPicker.setValue(exercise.getReps());

        builder.setView(dialogView)
                .setTitle("Adjust Sets and Reps")
                .setPositiveButton("OK", (dialog, which) -> {
                    exercise.setSets(setsPicker.getValue());
                    exercise.setReps(repsPicker.getValue());
                    adapter.notifyItemChanged(position); // Refresh the list
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
