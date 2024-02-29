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

import java.util.ArrayList;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        // Initialize UI components
        bindUIComponents(view);

        // Set listeners for buttons
        setListeners();

        // Retrieve and display the saved calorie goal if available
        retrieveAndDisplaySavedGoal();

        // Get and display the summary of added foods
        displaySummaryFromArguments();

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
            setGoal(); // Update the goal TextView based on the saved goal
        }
    }

    private void displaySummaryFromArguments() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            ArrayList<FoodItem> addedFoods = (ArrayList<FoodItem>) bundle.getSerializable("addedFoods");
            if (addedFoods != null) {
                displaySummary(addedFoods);
            }
        }
    }

    private void displaySummary(ArrayList<FoodItem> addedFoods) {
        totalCalories = 0;
        double totalProtein = 0;
        double totalFat = 0;
        double totalCarbs = 0;
        double totalFiber = 0;
        double totalSugar = 0;
        double totalSodium = 0;
        double totalCholesterol = 0;

        for (FoodItem food : addedFoods) {
            totalCalories += food.getCalories();
            totalProtein += food.getProteinG();
            totalFat += food.getFatTotalG();
            totalCarbs += food.getCarbohydratesTotalG();
            totalFiber += food.getFiberG(); // Assuming a getFiberG method exists
            totalSugar += food.getSugarG(); // Assuming a getSugarG method exists
            totalSodium += food.getSodiumMg(); // Assuming a getSodiumMg method exists
            totalCholesterol += food.getCholesterolMg(); // Assuming a getCholesterolMg method exists
        }

        totalCaloriesTextView.setText("Total Calories: " + totalCalories + " kcal");
        totalProteinTextView.setText("Total Protein: " + totalProtein + " g");
        totalFatTextView.setText("Total Fat: " + totalFat + " g");
        totalCarbsTextView.setText("Total Carbohydrates: " + totalCarbs + " g");
        totalFiberTextView.setText("Total Fiber: " + totalFiber + " g");
        totalSugarTextView.setText("Total Sugar: " + totalSugar + " g");
        totalSodiumTextView.setText("Total Sodium: " + totalSodium + " mg");
        totalCholesterolTextView.setText("Total Cholesterol: " + totalCholesterol + " mg");

        // Set the goal whenever the total values are updated
        setGoal();
    }

    private void setGoal() {
        String calorieGoalString = calorieGoalEditText.getText().toString().trim();
        if (!calorieGoalString.isEmpty()) {
            // Calculate the remaining calories based on the goal
            double remainingCalories = Double.parseDouble(calorieGoalString) - totalCalories;

            // Get the MainActivity instance and call the updateCaloriesCard method
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.updateCaloriesCard("Today target: " + remainingCalories + " kcal");
            }

            // Update the goal TextView to display the remaining calories
            goalTextView.setText("Goal: " + totalCalories + "/" + calorieGoalString);

            // Save the goal into SharedPreferences
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("calorieGoal", calorieGoalString);
            editor.apply();
        }
    }

    private void clearTotals() {
        totalCaloriesTextView.setText("Total Calories: 0 kcal");
        totalProteinTextView.setText("Total Protein: 0 g");
        totalFatTextView.setText("Total Fat: 0 g");
        totalCarbsTextView.setText("Total Carbohydrates: 0 g");
    }
}
