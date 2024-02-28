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

import com.example.nutriapp.ExerciseAdapter;

import java.util.ArrayList;
import java.util.List;

public class WeightFragment extends Fragment implements ExerciseAdapter.OnExerciseClickListener {

    private RecyclerView recyclerView;
    private ExerciseAdapter adapter;
    private List<Exercise> weightExercises;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_weight, container, false);

        // Find RecyclerView in the layout
        recyclerView = rootView.findViewById(R.id.recycler_view_exercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Generate the list of weight exercises
        weightExercises = generateWeightExercises();

        // Create and set up the adapter
        adapter = new ExerciseAdapter(getContext(), weightExercises, this);
        recyclerView.setAdapter(adapter);

        // Find and set click listener for the start session button
        Button startButton = rootView.findViewById(R.id.startWeightSessionButton);
        startButton.setOnClickListener(v -> startWeightSession());

        return rootView;
    }

    // Method to generate a list of weight exercises
    private List<Exercise> generateWeightExercises() {
        List<Exercise> exercises = new ArrayList<>();

        // Add weight exercises to the list
        exercises.add(new WeightsExercise("Seated Toe Touch", "1 sets of 10 reps", R.drawable.seated_toe_touch_a, "Description for seated toe touch", "Benefits for seated toe touch"));
        exercises.add(new WeightsExercise("Wall Stretch", "1 sets of 10 reps", R.drawable.wall_stretch_a, "Description for wall stretch", "Benefits for wall stretch"));
        exercises.add(new WeightsExercise("Standing Vertical Stretch", "1 sets of 10 reps", R.drawable.standing_vertical_stretch_a, "Description for standing vertical stretch", "Benefits for standing vertical stretch"));
        exercises.add(new WeightsExercise("Pelvic Shift", "1 sets of 10 reps", R.drawable.pelvic_shift_a, "Description for pelvic shift", "Benefits for pelvic shift"));
        exercises.add(new WeightsExercise("Pilates Rollover", "1 sets of 10 reps", R.drawable.pilates_rool_over_a, "Description for pilates rollover", "Benefits for pilates rollover"));
        exercises.add(new WeightsExercise("Dry Land Swim", "1 sets of 10 reps", R.drawable.drt_land_swim_a, "Description for dry land swim", "Benefits for dry land swim"));
        exercises.add(new WeightsExercise("Cat-Cow Stretch", "1 sets of 10 reps", R.drawable.cat_cow_stretch_a, "Description for cat-cow stretch", "Benefits for cat-cow stretch"));
        exercises.add(new WeightsExercise("Quad Stretch", "1 sets of 10 reps", R.drawable.run_b, "Description for quad stretch", "Benefits for quad stretch"));
        exercises.add(new WeightsExercise("Hamstring Stretch with Strap", "1 sets of 10 reps", R.drawable.hanging_exercise_b, "Description for hamstring stretch with strap", "Benefits for hamstring stretch with strap"));
        exercises.add(new WeightsExercise("Trunk Rotation Stretch", "1 sets of 10 reps", R.drawable.forward_spine_stretch_a, "Description for trunk rotation stretch", "Benefits for trunk rotation stretch"));

        return exercises;
    }

    // Listener for exercise item click (not used currently)
    @Override
    public void onExerciseClick(Exercise exercise, int position) {
        // Handle exercise click if needed
    }

    // Method to start the weight session
    private void startWeightSession() {
        // Start the WeightDetailActivity with the list of weight exercises
        Intent intent = new Intent(getActivity(), WeightDetailActivity.class);
        intent.putExtra("EXERCISE_TYPE", "Weights");
        intent.putExtra("EXERCISES", new ArrayList<>(weightExercises));
        startActivity(intent);
    }
}
