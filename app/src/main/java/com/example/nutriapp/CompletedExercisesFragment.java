/*
package com.example.nutriapp;

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

public class CompletedExercisesFragment extends Fragment {

    private RecyclerView recyclerViewCompletedExercises;
    private List<CompletedExercise> completedExercises;

    public CompletedExercisesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_exercises, container, false);

        recyclerViewCompletedExercises = view.findViewById(R.id.recyclerViewCompletedExercises);
        recyclerViewCompletedExercises.setLayoutManager(new LinearLayoutManager(getContext()));
        completedExercises = new ArrayList<>();

        // Add completed exercises to the list (You can add more as needed)
        //completedExercises.add(new CompletedExercise("Push-ups", "30 seconds", R.drawable.push_up_image));
        //completedExercises.add(new CompletedExercise("Squats", "45 seconds", R.drawable.));
        //completedExercises.add(new CompletedExercise("Plank", "60 seconds", R.drawable.));
        //completedExercises.add(new CompletedExercise("Lean Body Mass", "5 minutes", R.drawable.img_lean_body_mass));

        // Set up the RecyclerView adapter
        CompletedExerciseAdapter adapter = new CompletedExerciseAdapter(completedExercises);
        recyclerViewCompletedExercises.setAdapter(adapter);

        return view;
    }
}
*/
