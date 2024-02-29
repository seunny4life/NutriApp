package com.example.nutriapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class RecipeFragment extends Fragment {
    // Declaring UI elements
    private EditText searchEditText;
    private Button searchButton;
    private LinearLayout recipesLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        // Initializing UI elements
        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);
        recipesLayout = view.findViewById(R.id.recipesLayout);

        // Set click listener for search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get search query from EditText
                String query = searchEditText.getText().toString();
                if (!query.isEmpty()) {
                    // Call method to search for recipes
                    searchRecipe(query);
                } else {
                    // Show toast if search query is empty
                    Toast.makeText(getContext(), "Please enter a search term", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    // Method to search for recipes
    private void searchRecipe(String query) {
        OkHttpClient client = new OkHttpClient();

        // Build URL for the API request
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                .addPathSegment("recipes")
                .addPathSegment("complexSearch")
                .addQueryParameter("query", query)
                .build();

        // Create request object
        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .addHeader("X-RapidAPI-Key", "8bcc326003mshbb7a46ce2fc9253p1f928bjsn53b213315eb8")
                .addHeader("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                .build();

        // Asynchronous call to execute the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure in fetching data
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle response from the API
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    RecipeResponse recipeResponse = new Gson().fromJson(responseData, RecipeResponse.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (recipeResponse.results != null && !recipeResponse.results.isEmpty()) {
                                // Update UI with recipe results
                                updateUI(recipeResponse.results);
                            } else {
                                // Show toast if no results found
                                Toast.makeText(getContext(), "No results found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // Show toast if response is not successful
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Response not successful: " + response.message(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    // Method to update UI with recipe results
    private void updateUI(java.util.List<Recipe> recipes) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Remove all views from recipes layout
                recipesLayout.removeAllViews();
                for (Recipe recipe : recipes) {
                    // Inflate item_recipe layout for each recipe
                    View view = getLayoutInflater().inflate(R.layout.item_recipe, recipesLayout, false);
                    TextView recipeTitleTextView = view.findViewById(R.id.recipeTitleTextView);
                    ImageView recipeImageView = view.findViewById(R.id.recipeImageView);

                    // Set text size and color for recipe title
                    recipeTitleTextView.setTextSize(20);
                    recipeTitleTextView.setTextColor(Color.BLACK);
                    // Set recipe title and image using Glide library
                    recipeTitleTextView.setText(recipe.title);
                    Glide.with(RecipeFragment.this).load(recipe.image).into(recipeImageView);

                    // Add view to recipes layout
                    recipesLayout.addView(view);
                }
            }
        });
    }

    // Data class to match the JSON structure of the API response
    public static class RecipeResponse {
        java.util.List<Recipe> results;
    }
}
