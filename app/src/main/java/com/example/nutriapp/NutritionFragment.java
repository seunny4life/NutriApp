package com.example.nutriapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NutritionFragment extends Fragment {

    private RecyclerView recyclerViewMeals;
    private Button buttonLogMeal;
    private TextView textViewMacronutrients;
    private TextView textViewLoggedMeals;
    private List<Meal> mealList;
    private MealAdapter mealAdapter;
    private OkHttpClient client;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nutrition, container, false);

        // Initialize UI components
        recyclerViewMeals = rootView.findViewById(R.id.recyclerViewMeals);
        buttonLogMeal = rootView.findViewById(R.id.buttonLogMeal);
        textViewMacronutrients = rootView.findViewById(R.id.textViewMacronutrients);
        textViewLoggedMeals = rootView.findViewById(R.id.textViewLoggedMeals);

        // Set up RecyclerView for displaying logged meals
        mealList = new ArrayList<>();
        mealAdapter = new MealAdapter(mealList);
        recyclerViewMeals.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMeals.setAdapter(mealAdapter);

        // Set up OkHttpClient
        client = new OkHttpClient();

        // Set up button click listener for logging a new meal
        buttonLogMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddMealDialog();
            }
        });

        // Make API call to retrieve nutrition information
        fetchNutritionData();

        return rootView;
    }

    private void fetchNutritionData() {
        Request request = new Request.Builder()
                .url("https://nutrition-by-api-ninjas.p.rapidapi.com/v1/nutrition?query=1%20cup%20of%20Rice%20and%201%20glass%20of%20Milk")
                .get()
                .addHeader("X-RapidAPI-Key", "4759714ef0msh8911b6e20138358p144449jsnfe564f842526")
                .addHeader("X-RapidAPI-Host", "nutrition-by-api-ninjas.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Handle failure (e.g., show error message)
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                // Parse JSON response
                try {
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    JSONArray foodsArray = jsonResponse.getJSONArray("foods");

                    // Extract nutrition information for each food item
                    for (int i = 0; i < foodsArray.length(); i++) {
                        JSONObject foodObject = foodsArray.getJSONObject(i);
                        String foodName = foodObject.getString("food_name");
                        String servingSize = foodObject.getString("serving_size");
                        String calories = foodObject.getString("calories");

                        // Create a Meal object and add it to the mealList
                        Meal meal = new Meal(foodName, servingSize, calories);
                        mealList.add(meal);
                    }

                    // Update RecyclerView with the retrieved meal data
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mealAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle JSON parsing error
                }
            }
        });
    }

    private void showAddMealDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_meal, null);
        builder.setView(dialogView);

        EditText editTextFoodName = dialogView.findViewById(R.id.mealNameEditText);

        builder.setPositiveButton("Add Meal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the meal name entered by the user
                String foodName = editTextFoodName.getText().toString();

                // Create a Meal object and add it to the mealList
                // Here, you might want to implement the logic to fetch nutrition information
                // based on the meal name entered by the user
                // For demonstration purposes, I'm just adding the meal with empty nutrition information
                Meal meal = new Meal(foodName, "", "");
                mealList.add(meal);
                mealAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
