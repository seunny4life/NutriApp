package com.example.nutriapp;

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

    private ArrayList<MealItem> mealItemList;
    private MealListAdapter mealListAdapter;
    private FirebaseFirestore db;
    private String userId;
    private TextView totalCaloriesTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calories, container, false);

        // Initialize RecyclerView and other UI components
        RecyclerView mealRecyclerView = view.findViewById(R.id.mealRecyclerView);
        totalCaloriesTextView = view.findViewById(R.id.totalCalories);
        mealItemList = new ArrayList<>();
        mealListAdapter = new MealListAdapter(mealItemList);
        mealRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mealRecyclerView.setAdapter(mealListAdapter);

        // Initialize Firebase Firestore and get the current user
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            loadUserMeals();
        }

        // Setup button to add a meal
        Button addMealButton = view.findViewById(R.id.addMealButton);
        addMealButton.setOnClickListener(v -> openInputDialog());

        return view;
    }

    private void loadUserMeals() {
        db.collection("meals").whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mealItemList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            MealItem mealItem = document.toObject(MealItem.class);
                            mealItemList.add(mealItem);
                        }
                        mealListAdapter.notifyDataSetChanged();
                        updateTotalCalories();
                    } else {
                        // Handle error
                    }
                });
    }

    private void openInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add a Meal");

        View inputView = getLayoutInflater().inflate(R.layout.dialog_add_meal, null);
        final EditText mealNameEditText = inputView.findViewById(R.id.mealNameEditText);
        final EditText caloriesEditText = inputView.findViewById(R.id.caloriesEditText);
        builder.setView(inputView);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mealName = mealNameEditText.getText().toString().trim();
                String caloriesString = caloriesEditText.getText().toString().trim();

                if (mealName.isEmpty() || caloriesString.isEmpty()) {
                    Toast.makeText(getActivity(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                int calories;
                try {
                    calories = Integer.parseInt(caloriesString);
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Invalid number format", Toast.LENGTH_SHORT).show();
                    return;
                }

                MealItem mealItem = new MealItem(userId, mealName, calories);
                mealItemList.add(mealItem);
                mealListAdapter.notifyDataSetChanged();
                updateTotalCalories();

                db.collection("meals").add(mealItem)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(getActivity(), "Meal added successfully", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Error adding meal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void updateTotalCalories() {
        int totalCalories = 0;
        for (MealItem item : mealItemList) {
            totalCalories += item.getCalories();
        }
        totalCaloriesTextView.setText(MessageFormat.format("Total Calories: {0}", totalCalories));
    }
}
