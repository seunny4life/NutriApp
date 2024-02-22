package com.example.nutriapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CardioFragment extends Fragment implements ExerciseAdapter.OnExerciseClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cardio, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view_exercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        List<Exercise> cardioExercises = generateCardioExercises();
        ExerciseAdapter adapter = new ExerciseAdapter(getContext(), cardioExercises, this);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private List<Exercise> generateCardioExercises() {
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(new CardioExercise("Running", "30", R.drawable.run, "Improves cardiovascular health.", "Great for endurance and stamina."));
        exercises.add(new CardioExercise("Cycling", "45", R.drawable.cycling, "Low impact but effective for weight loss.", "Strengthens lower body muscles."));
        exercises.add(new CardioExercise("Jumping Jacks", "40", R.drawable.hanging_exercise_b, "A full-body workout that reduces body fat.", "Helps to improve heart health."));
        exercises.add(new CardioExercise("High Knees", "40", R.drawable.img_calorie_burn, "Enhances coordination and flexibility of leg muscles.", "Aids in burning calories, boosting metabolism."));
        exercises.add(new CardioExercise("Burpees", "60", R.drawable.ic_walk_black_24dp, "Increases strength and muscle tone.", "Improves cardiovascular fitness and endurance."));
        return exercises;
    }

    @Override
    public void onExerciseClick(Exercise exercise, int position) {
        Intent intent = new Intent(getContext(), ExerciseDetailActivity.class);
        intent.putExtra("EXERCISE_NAME", exercise.getName());
        intent.putExtra("EXERCISE_DURATION", exercise.getDuration());
        intent.putExtra("EXERCISE_IMAGE", exercise.getImageResourceId());
        intent.putExtra("EXERCISE_DESCRIPTION", exercise.getDescription());
        intent.putExtra("EXERCISE_BENEFITS", exercise.getBenefits());
        intent.putExtra("EXERCISE_TYPE", exercise.getType());
        startActivity(intent);
    }
}
