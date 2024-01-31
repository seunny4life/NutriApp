package com.example.nutriapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class NutritionFragment extends Fragment {

    private TextView dailyGoalsText, foodTrackingText;
    private Button addFoodButton;
    private List<FoodItem> foodItems; // List to hold food items
    private FoodItemAdapter foodItemAdapter; // Adapter for RecyclerView

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);
        initializeUIComponents(view);
        // Inside onCreateView
        RecyclerView recyclerView = view.findViewById(R.id.foodListRecyclerView);
        recyclerView.setAdapter(foodItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize your food item list and adapter
        foodItems = new ArrayList<>();
        foodItemAdapter = new FoodItemAdapter(foodItems);
        // TODO: Initialize your RecyclerView and set its adapter here

        return view;
    }

    private void initializeUIComponents(View view) {
        dailyGoalsText = view.findViewById(R.id.dailyGoalsText);
        foodTrackingText = view.findViewById(R.id.foodTrackingText);
        addFoodButton = view.findViewById(R.id.addFoodButton);

        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddMealDialog();
            }
        });
    }

    private void showAddMealDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.meal_upload_dialog, null);
        final EditText editTextMealName = dialogView.findViewById(R.id.editTextMealName);
        final EditText editTextCalories = dialogView.findViewById(R.id.editTextCalories);
        builder.setView(dialogView)
                .setTitle("Add a New Meal")
                .setPositiveButton("Upload", (dialog, which) -> {
                    String mealName = editTextMealName.getText().toString();
                    int calories = Integer.parseInt(editTextCalories.getText().toString());
                    FoodItem newFoodItem = new FoodItem(mealName, calories);
                    addNewMeal(newFoodItem);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }

    private void addNewMeal(FoodItem newFoodItem) {
        foodItems.add(newFoodItem);
        foodItemAdapter.notifyDataSetChanged();
        // Save to database if required
    }

    // Additional methods for data handling, user interactions, etc.
}
