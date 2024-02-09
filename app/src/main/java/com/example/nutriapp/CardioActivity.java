package com.example.nutriapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CardioActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardio);


        RecyclerView recyclerView = findViewById(R.id.recycler_view_cardio_exercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<CardioExercise> exercises = new ArrayList<>();
        exercises.add(new CardioExercise("Running", 30, R.drawable.run_b));
        exercises.add(new CardioExercise("Cycling", 45, R.drawable.bicycle));
        exercises.add(new CardioExercise("Treadmill", 45, R.drawable.treadmill));

        CardioExercisesAdapter adapter = new CardioExercisesAdapter(this, exercises, exercise -> {
            Intent intent = new Intent(CardioActivity.this, CardioExerciseDetailActivity.class);
            intent.putExtra("EXERCISE_NAME", exercise.getName());
            intent.putExtra("EXERCISE_DURATION", exercise.getDuration());
            intent.putExtra("EXERCISE_IMAGE", exercise.getImageResourceId());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }

}
