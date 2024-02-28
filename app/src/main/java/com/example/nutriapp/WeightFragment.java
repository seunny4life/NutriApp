package com.example.nutriapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

        // Create and add weight exercises to the list
        exercises.add(new WeightsExercise("Squats", "3, 1", R.drawable.two_straight_legs_up, "Description for squats", "Benefits for squats"));
        exercises.add(new WeightsExercise("Deadlifts", "3, 1", R.drawable.super_cobra_stretch, "Description for deadlifts", "Benefits for deadlifts"));
        exercises.add(new WeightsExercise("Bench Press", "3, 1", R.drawable.pilates_rool_over_a, "Description for bench press", "Benefits for bench press"));

        return exercises;
    }

    @Override
    public void onExerciseClick(Exercise exercise, int position) {
        // Handle exercise click if needed
    }

    private void startWeightSession() {
        Intent intent = new Intent(getActivity(), WeightDetailActivity.class);
        intent.putExtra("EXERCISE_TYPE", "Weights");
        intent.putExtra("EXERCISES", new ArrayList<>(weightExercises));
        startActivity(intent);
    }
}
