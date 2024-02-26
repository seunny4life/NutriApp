package com.example.nutriapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ScrollView scrollView;
    private FrameLayout fragmentContainer;
    private Button bmiButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUIComponents();

        // Check if the user is authenticated
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            // User is not authenticated, redirect to LoginActivity
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            // User is authenticated, display personalized content
            displayPersonalizedContent();
        }
    }

    private void initializeUIComponents() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        scrollView = findViewById(R.id.scrollView2);
        fragmentContainer = findViewById(R.id.fragment_container);
        bmiButton = findViewById(R.id.bmi);

        bmiButton.setOnClickListener(v -> {
            // Replace the current fragment with the BMIFragment
            Log.d("MainActivity", "BMI Button clicked");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new BMIFragment()) // Replace BMIFragment() with your actual fragment class
                    .addToBackStack(null)  // Allows users to go back to the previous fragment/state
                    .commit();
        });

        bottomNavigation();
    }

    private void bottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            // Toggle visibility
            if (itemId == R.id.mainActivity) {
                // Main Activity content is selected
                scrollView.setVisibility(View.VISIBLE);
                fragmentContainer.setVisibility(View.GONE);
            } else {
                // Any fragment is selected
                scrollView.setVisibility(View.GONE);
                fragmentContainer.setVisibility(View.VISIBLE);

                if (itemId == R.id.accountActivity) {
                    selectedFragment = new AccountFragment();
                } else if (itemId == R.id.fragmentNutrition) {
                    selectedFragment = new NutritionFragment();
                } else if (itemId == R.id.fragmentRecipe) {
                    selectedFragment = new RecipeFragment();
                } else if (itemId == R.id.fragment_workout) {
                    selectedFragment = new WorkoutFragment();
                }

                // Perform the fragment transaction
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });
    }

    private void displayPersonalizedContent() {
        // Implement logic to display personalized content
        // This can include loading user profile data, recent workouts, nutrition tracking, etc.
        // Update the bottom navigation menu based on the user's authentication status
        // For example, if the user is logged in, display options for profile settings, workout tracking, etc.
        // If the user is not logged in, display options for logging in or signing up.
    }
}
