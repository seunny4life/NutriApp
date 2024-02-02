package com.example.nutriapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import okhttp3.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class NutritionFragment extends Fragment {

    private EditText heightInput, weightInput;
    private Spinner activityLevelSpinner;
    private TextView resultText, nutrientBreakdownText, mealTimingText, nutritionalTipsText;
    private Button calculateButton;

    // Replace with your actual Edamam API key
    private static final String API_KEY = "4759714ef0msh8911b6e20138358p144449jsnfe564f842526";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);

        initializeUIComponents(view);
        setupSpinners();
        calculateButton.setOnClickListener(v -> calculateNutrition());

        return view;
    }

    private void initializeUIComponents(View view) {
        heightInput = view.findViewById(R.id.height);
        weightInput = view.findViewById(R.id.weight);
        activityLevelSpinner = view.findViewById(R.id.activityLevelSpinner);
        resultText = view.findViewById(R.id.resultText);
        nutrientBreakdownText = view.findViewById(R.id.nutrientBreakdownTextView);
        mealTimingText = view.findViewById(R.id.mealTimingTextView);
        nutritionalTipsText = view.findViewById(R.id.nutritionalTipsTextView);
        calculateButton = view.findViewById(R.id.calculateButton);
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.activity_level_options, android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityLevelSpinner.setAdapter(activityAdapter);
    }

    private void calculateNutrition() {
        String heightStr = heightInput.getText().toString();
        String weightStr = weightInput.getText().toString();
        if (TextUtils.isEmpty(heightStr) || TextUtils.isEmpty(weightStr)) {
            Toast.makeText(getActivity(), "Please enter height and weight", Toast.LENGTH_SHORT).show();
            return;
        }

        double height = Double.parseDouble(heightStr);
        double weight = Double.parseDouble(weightStr);
        String activityLevel = activityLevelSpinner.getSelectedItem().toString();

        double calories = calculateCalories(height, weight, activityLevel);
        resultText.setText(getString(R.string.calories_result, calories));

        // Retrieve meal suggestions and display them
        retrieveMealSuggestions(calories);
    }

    private double calculateCalories(double height, double weight, String activityLevel) {
        // Simplified example calculation without gender
        // Adjust this method based on your specific calculation requirements
        double bmr = 10 * weight + 6.25 * height - 5 * 30 + 5; // Example for male, adjust as needed
        double multiplier = getActivityLevelMultiplier(activityLevel);
        return bmr * multiplier;
    }

    private double getActivityLevelMultiplier(String activityLevel) {
        switch (activityLevel) {
            case "Sedentary":
                return 1.2;
            case "Lightly active":
                return 1.375;
            case "Moderately active":
                return 1.55;
            case "Very active":
                return 1.725;
            case "Extra active":
                return 1.9;
            default:
                return 1;
        }
    }

    private void retrieveMealSuggestions(double calories) {
        OkHttpClient client = new OkHttpClient();
        String apiUrl = "https://edamam-edamam-nutrition-analysis.p.rapidapi.com/api/nutrition-details?beta=true&force=true&ingr=chicken"; // Example ingredient, replace as needed

        Request request = new Request.Builder()
                .url(apiUrl)
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", "edamam-edamam-nutrition-analysis.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONArray foodsArray = jsonObject.getJSONArray("foods");
                        if (foodsArray.length() > 0) {
                            JSONObject foodItem = foodsArray.getJSONObject(0);
                            JSONArray nutrientsArray = foodItem.getJSONArray("foodNutrients");

                            // Parse and format the nutrient breakdown
                            StringBuilder nutrientBreakdown = new StringBuilder("Nutrient Breakdown:\n");
                            for (int i = 0; i < nutrientsArray.length(); i++) {
                                JSONObject nutrient = nutrientsArray.getJSONObject(i);
                                String nutrientName = nutrient.getString("nutrientName");
                                double nutrientValue = nutrient.getDouble("value");
                                String unitName = nutrient.getString("unitName");

                                nutrientBreakdown.append(nutrientName)
                                        .append(": ")
                                        .append(nutrientValue)
                                        .append(" ")
                                        .append(unitName)
                                        .append("\n");
                            }

                            // Parse meal suggestions and display them
                            String mealSuggestions = foodItem.optString("mealSuggestions", "No meal suggestions available");
                            getActivity().runOnUiThread(() -> {
                                nutrientBreakdownText.setText(nutrientBreakdown.toString());
                                mealTimingText.setText("Meal Timing Recommendations:\n"); // You can set meal timing here
                                nutritionalTipsText.setText("Nutritional Tips:\n"); // You can set nutritional tips here
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
