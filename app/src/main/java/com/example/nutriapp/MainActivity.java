package com.example.nutriapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.MessageFormat;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ScrollView scrollView;
    private FrameLayout fragmentContainer;
    private Button bmiButton;
    private TextView greetingTextView;

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
            // User is authenticated, set the greeting
            String username = currentUser.getDisplayName();
            if (username != null && !username.isEmpty()) {
                greetingTextView.setText(MessageFormat.format("Welcome, {0}!", username));
            }
        }
    }

    private void initializeUIComponents() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        scrollView = findViewById(R.id.scrollView2);
        fragmentContainer = findViewById(R.id.fragment_container);
        bmiButton = findViewById(R.id.bmi);
        greetingTextView = findViewById(R.id.greeting); // Initialize greeting TextView

        bmiButton.setOnClickListener(v -> {
            // Replace the current fragment with the BMIFragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new BMIFragment())
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
}
