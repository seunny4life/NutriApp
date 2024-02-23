package com.example.nutriapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NutritionFragment extends Fragment {
    private TextView foodInfoTextView;
    private FoodItem lastSearchedFoodItem = null;
    private ArrayList<FoodItem> addedFoodList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);

        EditText searchEditText = view.findViewById(R.id.searchEditText);
        foodInfoTextView = view.findViewById(R.id.foodInfoTextView);
        Button addButton = view.findViewById(R.id.addButton);
        Button viewSummaryButton = view.findViewById(R.id.viewSummaryButton);
        Button searchButton = view.findViewById(R.id.searchButton);

        searchEditText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchFood(textView.getText().toString());
                return true;
            }
            return false;
        });

        addButton.setOnClickListener(v -> {
            if (lastSearchedFoodItem != null) {
                addedFoodList.add(lastSearchedFoodItem);
                Toast.makeText(getContext(), lastSearchedFoodItem.getName() + " added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "No food item to add", Toast.LENGTH_SHORT).show();
            }
        });

        searchButton.setOnClickListener(v -> searchFood(searchEditText.getText().toString()));

        viewSummaryButton.setOnClickListener(v -> {
            SummaryFragment summaryFragment = new SummaryFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("addedFoods", addedFoodList); // Ensure FoodItem implements Serializable
            summaryFragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, summaryFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void searchFood(String query) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            String url = "https://nutrition-by-api-ninjas.p.rapidapi.com/v1/nutrition?query=" + query;

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("X-RapidAPI-Key", "8bcc326003mshbb7a46ce2fc9253p1f928bjsn53b213315eb8")
                    .addHeader("X-RapidAPI-Host", "nutrition-by-api-ninjas.p.rapidapi.com")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseData);
                    if (jsonArray.length() > 0) {
                        JSONObject firstItem = jsonArray.getJSONObject(0);

                        lastSearchedFoodItem = new FoodItem(
                                firstItem.getString("name"),
                                firstItem.getDouble("calories"),
                                firstItem.getDouble("serving_size_g"),
                                firstItem.getDouble("fat_total_g"),
                                firstItem.getDouble("fat_saturated_g"),
                                firstItem.getDouble("protein_g"),
                                firstItem.getDouble("sodium_mg"),
                                firstItem.getDouble("potassium_mg"),
                                firstItem.getDouble("cholesterol_mg"),
                                firstItem.getDouble("carbohydrates_total_g"),
                                firstItem.getDouble("fiber_g"),
                                firstItem.getDouble("sugar_g")
                        );

                        updateUIWithFoodItem(firstItem);
                    } else {
                        showToast("No results found");
                    }
                } else {
                    showToast("Response not successful: " + response.message());
                }
            } catch (IOException | JSONException e) {
                showToast("Error fetching data: " + e.getMessage());
            }
        }).start();
    }

    @SuppressLint("DefaultLocale")
    private void updateUIWithFoodItem(JSONObject foodItem) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            try {
                foodInfoTextView.setText(String.format("Name: %s\nCalories: %.2f\nServing size (g): %.2f\nTotal fat (g): %.2f\nSaturated fat (g): %.2f\nProtein (g): %.2f\nSodium (mg): %.2f\nPotassium (mg): %.2f\nCholesterol (mg): %.2f\nTotal carbohydrates (g): %.2f\nDietary fiber (g): %.2f\nSugars (g): %.2f",
                        foodItem.getString("name"), foodItem.getDouble("calories"), foodItem.getDouble("serving_size_g"), foodItem.getDouble("fat_total_g"), foodItem.getDouble("fat_saturated_g"), foodItem.getDouble("protein_g"), foodItem.getDouble("sodium_mg"), foodItem.getDouble("potassium_mg"), foodItem.getDouble("cholesterol_mg"), foodItem.getDouble("carbohydrates_total_g"), foodItem.getDouble("fiber_g"), foodItem.getDouble("sugar_g")
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void showToast(String message) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show());
    }
}
