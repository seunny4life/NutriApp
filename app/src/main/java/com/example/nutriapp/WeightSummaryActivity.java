package com.example.nutriapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WeightSummaryActivity extends AppCompatActivity {

    private double weight;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_summary);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            weight = extras.getDouble("WEIGHT");
            date = extras.getString("DATE");

            displayWeightSummary();
        }

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            saveWeightSummary();
            navigateToWorkoutHistory();
        });
    }

    private void displayWeightSummary() {
        TextView weightTextView = findViewById(R.id.weightTextView);
        TextView dateTextView = findViewById(R.id.dateTextView);

        weightTextView.setText(String.valueOf(weight) + " kg");
        dateTextView.setText(date);
    }

    private void saveWeightSummary() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String json = sharedPreferences.getString("weightSummaries", null);
        Type type = new TypeToken<ArrayList<WeightSummaryItem>>() {}.getType();
        ArrayList<WeightSummaryItem> weightSummaries = gson.fromJson(json, type);

        if (weightSummaries == null) weightSummaries = new ArrayList<>();

        WeightSummaryItem newItem = new WeightSummaryItem(weight, date);
        weightSummaries.add(newItem);

        String updatedJson = gson.toJson(weightSummaries);
        editor.putString("weightSummaries", updatedJson);
        editor.apply();
    }

    private void navigateToWorkoutHistory() {
        Intent intent = new Intent(WeightSummaryActivity.this, WorkoutHistoryActivity.class);
        startActivity(intent);
        finish();
    }
}
