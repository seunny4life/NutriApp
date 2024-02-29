package com.example.nutriapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class SummaryFragment extends Fragment {
    private TextView totalCaloriesTextView;
    private TextView totalProteinTextView;
    private TextView totalFatTextView;
    private TextView totalCarbsTextView;
    private TextView totalFiberTextView;
    private TextView totalSugarTextView;
    private TextView totalSodiumTextView;
    private TextView totalCholesterolTextView;
    private EditText calorieGoalEditText;
    private TextView goalTextView;
    private Button setGoalButton;
    private Button clearButton;
    private double totalCalories = 0;
    private double totalProtein = 0;
    private double totalFat = 0;
    private double totalCarbs = 0;
    private double totalFiber = 0;
    private double totalSugar = 0;
    private double totalSodium = 0;
    private double totalCholesterol = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        // Initialize UI components
        bindUIComponents(view);

        // Set listeners for buttons
        setListeners();

        // Retrieve and display the saved calorie goal and totals
        retrieveAndDisplaySavedGoal();
        retrieveAndDisplaySavedTotals();

        return view;
    }

    private void bindUIComponents(View view) {
        totalCaloriesTextView = view.findViewById(R.id.totalCaloriesTextView);
        totalProteinTextView = view.findViewById(R.id.totalProteinTextView);
        totalFatTextView = view.findViewById(R.id.totalFatTextView);
        totalCarbsTextView = view.findViewById(R.id.totalCarbsTextView);
        totalFiberTextView = view.findViewById(R.id.totalFiberTextView);
        totalSugarTextView = view.findViewById(R.id.totalSugarTextView);
        totalSodiumTextView = view.findViewById(R.id.totalSodiumTextView);
        totalCholesterolTextView = view.findViewById(R.id.totalCholesterolTextView);
        calorieGoalEditText = view.findViewById(R.id.calorieGoalEditText);
        goalTextView = view.findViewById(R.id.goalTextView);
        setGoalButton = view.findViewById(R.id.setGoalButton);
        clearButton = view.findViewById(R.id.clearButton);
    }

    private void setListeners() {
        setGoalButton.setOnClickListener(v -> setGoal());
        clearButton.setOnClickListener(v -> clearTotals());
    }

    private void retrieveAndDisplaySavedGoal() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "0";
        String calorieGoal = sharedPref.getString("calorieGoal", defaultValue);
        if (!calorieGoal.equals(defaultValue)) {
            calorieGoalEditText.setText(calorieGoal);
            updateGoalDisplay(); // Update the goal TextView based on the saved goal
        }
    }

    private void retrieveAndDisplaySavedTotals() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        totalCalories = sharedPref.getFloat("totalCalories", 0f);
        totalProtein = sharedPref.getFloat("totalProtein", 0f);
        totalFat = sharedPref.getFloat("totalFat", 0f);
        totalCarbs = sharedPref.getFloat("totalCarbs", 0f);
        totalFiber = sharedPref.getFloat("totalFiber", 0f);
        totalSugar = sharedPref.getFloat("totalSugar", 0f);
        totalSodium = sharedPref.getFloat("totalSodium", 0f);
        totalCholesterol = sharedPref.getFloat("totalCholesterol", 0f);

        updateUITotals();
        updateGoalDisplay(); // Add this line to update goal display
    }

    private void updateUITotals() {
        totalCaloriesTextView.setText(String.format("Total Calories: %.2f kcal", totalCalories));
        totalProteinTextView.setText(String.format("Total Protein: %.2f g", totalProtein));
        totalFatTextView.setText(String.format("Total Fat: %.2f g", totalFat));
        totalCarbsTextView.setText(String.format("Total Carbohydrates: %.2f g", totalCarbs));
        totalFiberTextView.setText(String.format("Total Fiber: %.2f g", totalFiber));
        totalSugarTextView.setText(String.format("Total Sugar: %.2f g", totalSugar));
        totalSodiumTextView.setText(String.format("Total Sodium: %.2f mg", totalSodium));
        totalCholesterolTextView.setText(String.format("Total Cholesterol: %.2f mg", totalCholesterol));
    }

    private void setGoal() {
        String calorieGoalString = calorieGoalEditText.getText().toString().trim();
        if (!calorieGoalString.isEmpty()) {
            // Save the goal into SharedPreferences
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("calorieGoal", calorieGoalString);
            editor.apply();

            updateGoalDisplay(); // Update the goal display with the new goal
        }
    }

    private void clearTotals() {
        totalCalories = 0;
        totalProtein = 0;
        totalFat = 0;
        totalCarbs = 0;
        totalFiber = 0;
        totalSugar = 0;
        totalSodium = 0;
        totalCholesterol = 0;

        updateUITotals();
        updateGoalDisplay(); // Update the goal display after clearing totals

        // Clear totals from SharedPreferences
        clearTotalsFromSharedPreferences();
    }

    private void clearTotalsFromSharedPreferences() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("totalCalories");
        editor.remove("totalProtein");
        editor.remove("totalFat");
        editor.remove("totalCarbs");
        editor.remove("totalFiber");
        editor.remove("totalSugar");
        editor.remove("totalSodium");
        editor.remove("totalCholesterol");
        editor.apply();
    }

    private void updateGoalDisplay() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "0";
        String calorieGoalString = sharedPref.getString("calorieGoal", defaultValue);
        if (!calorieGoalString.isEmpty()) {
            double calorieGoal = Double.parseDouble(calorieGoalString);
            goalTextView.setText(String.format("Goal: %.2f/%.2f kcal", totalCalories, calorieGoal));
        } else {
            goalTextView.setText("Set your calorie goal");
        }
    }
}
