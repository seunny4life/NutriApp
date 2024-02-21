package com.example.nutriapp;

// Import statements for Android and Firebase components
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.MessageFormat;
import java.util.ArrayList;

public class CaloriesFragment extends Fragment {

    // Declaration of variables for UI components and Firebase
    private ArrayList<MealItem> mealItemList;
    private MealListAdapter mealListAdapter;
    private FirebaseFirestore db;
    private String userId;
    private TextView totalCaloriesTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calories, container, false);

        // Initialize RecyclerView for displaying meals and set up its adapter
        RecyclerView mealRecyclerView = view.findViewById(R.id.mealRecyclerView);
        totalCaloriesTextView = view.findViewById(R.id.totalCalories);
        mealItemList = new ArrayList<>();
        mealListAdapter = new MealListAdapter(mealItemList);
        mealRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mealRecyclerView.setAdapter(mealListAdapter);

        // Initialize Firebase Firestore and get the current user's details
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            loadUserMeals(); // Load meals from Firestore for the current user
        }

        // Setup button to add a new meal
        Button addMealButton = view.findViewById(R.id.addMealButton);
        addMealButton.setOnClickListener(v -> openInputDialog());

        return view;
    }

    // Load user meals from Firestore
    private void loadUserMeals() {
        db.collection("meals").whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mealItemList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            MealItem mealItem = document.toObject(MealItem.class);
                            mealItemList.add(mealItem); // Add the meal to the list
                        }
                        mealListAdapter.notifyDataSetChanged(); // Refresh the adapter
                        updateTotalCalories(); // Update the total calories displayed
                    } else {
                        // Handle the error scenario
                    }
                });
    }

    // Display dialog to input a new meal
    private void openInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add a Meal");

        // Inflate and set the layout for the dialog
        View inputView = getLayoutInflater().inflate(R.layout.dialog_add_meal, null);
        final EditText mealNameEditText = inputView.findViewById(R.id.mealNameEditText);
        final EditText caloriesEditText = inputView.findViewById(R.id.caloriesEditText);
        builder.setView(inputView);

        // Setup Add and Cancel buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the meal name and calories from input fields
                String mealName = mealNameEditText.getText().toString().trim();
                String caloriesString = caloriesEditText.getText().toString().trim();

                // Validate input
                if (mealName.isEmpty() || caloriesString.isEmpty()) {
                    Toast.makeText(getActivity(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Parse calories and handle number format exception
                int calories;
                try {
                    calories = Integer.parseInt(caloriesString);
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Invalid number format", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new meal item and add it to Firestore
                MealItem mealItem = new MealItem(userId, mealName, calories);
                mealItemList.add(mealItem);
                mealListAdapter.notifyDataSetChanged(); // Refresh the adapter
                updateTotalCalories(); // Update the total calories displayed

                // Add meal to Firestore and handle success or failure
                db.collection("meals").add(mealItem)
                        .addOnSuccessListener(documentReference -> Toast.makeText(getActivity(), "Meal added successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(getActivity(), "Error adding meal: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).setNegativeButton("Cancel", null);
        builder.show();
    }

    // Update and display the total calories of all meals
    private void updateTotalCalories() {
        int totalCalories = 0;
        for (MealItem item : mealItemList) {
            totalCalories += item.getCalories(); // Sum up the calories
        }
        totalCaloriesTextView.setText(MessageFormat.format("Total Calories: {0}", totalCalories)); // Display the total calories
    }
}
