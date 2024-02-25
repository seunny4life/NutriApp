package com.example.nutriapp;

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
    private EditText searchEditText;
    private Button searchButton;
    private LinearLayout recipesLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);
        recipesLayout = view.findViewById(R.id.recipesLayout);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString();
                if (!query.isEmpty()) {
                    searchRecipe(query);
                } else {
                    Toast.makeText(getContext(), "Please enter a search term", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void searchRecipe(String query) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                .addPathSegment("recipes")
                .addPathSegment("complexSearch")
                .addQueryParameter("query", query)
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .addHeader("X-RapidAPI-Key", "8bcc326003mshbb7a46ce2fc9253p1f928bjsn53b213315eb8")
                .addHeader("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    RecipeResponse recipeResponse = new Gson().fromJson(responseData, RecipeResponse.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (recipeResponse.results != null && !recipeResponse.results.isEmpty()) {
                                updateUI(recipeResponse.results); // Pass the entire list of results
                            } else {
                                Toast.makeText(getContext(), "No results found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
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

    private void updateUI(java.util.List<Recipe> recipes) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recipesLayout.removeAllViews();
                for (Recipe recipe : recipes) {
                    View view = getLayoutInflater().inflate(R.layout.item_recipe, recipesLayout, false);
                    TextView recipeTitleTextView = view.findViewById(R.id.recipeTitleTextView);
                    ImageView recipeImageView = view.findViewById(R.id.recipeImageView);

                    recipeTitleTextView.setText(recipe.title);
                    Glide.with(RecipeFragment.this).load(recipe.image).into(recipeImageView);

                    recipesLayout.addView(view);
                }
            }
        });
    }

    // Data classes to match the JSON structure of the API response
    public static class RecipeResponse {
        java.util.List<Recipe> results;
    }
}
