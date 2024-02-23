package com.example.nutriapp;

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

        setGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGoal();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTotals();
            }
        });

        // Check if goalTextView already has a value and set it accordingly
        if (!goalTextView.getText().toString().isEmpty()) {
            setGoal();
        }

        // Get the list of added food items from arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            ArrayList<FoodItem> addedFoods = (ArrayList<FoodItem>) bundle.getSerializable("addedFoods");
            if (addedFoods != null) {
                displaySummary(addedFoods);
            }
        }

        return view;
    }

    private void displaySummary(ArrayList<FoodItem> addedFoods) {
        totalCalories = 0;
        double totalProtein = 0;
        double totalFat = 0;
        double totalCarbs = 0;

        for (FoodItem food : addedFoods) {
            totalCalories += food.getCalories();
            totalProtein += food.getProteinG();
            totalFat += food.getFatTotalG();
            totalCarbs += food.getCarbohydratesTotalG();
        }

        totalCaloriesTextView.setText("Total Calories: " + totalCalories + " kcal");
        totalProteinTextView.setText("Total Protein: " + totalProtein + " g");
        totalFatTextView.setText("Total Fat: " + totalFat + " g");
        totalCarbsTextView.setText("Total Carbohydrates: " + totalCarbs + " g");

        // Set the goal whenever the total values are updated
        setGoal();
    }

    private void setGoal() {
        String calorieGoalString = calorieGoalEditText.getText().toString().trim();
        if (!calorieGoalString.isEmpty()) {
            double calorieGoal = Double.parseDouble(calorieGoalString);
            goalTextView.setText("Goal: " + totalCalories + "/" + calorieGoal);
        }
    }

    private void clearTotals() {
        totalCaloriesTextView.setText("Total Calories: 0 kcal");
        totalProteinTextView.setText("Total Protein: 0 g");
        totalFatTextView.setText("Total Fat: 0 g");
        totalCarbsTextView.setText("Total Carbohydrates: 0 g");
        // Clear other total text views if necessary

        // Clear the goal
        goalTextView.setText("Goal: 0/0");
    }
}
