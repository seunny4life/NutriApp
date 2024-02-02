package com.example.nutriapp;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ScrollView scrollView;
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUIComponents();

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.mainActivity); // Assuming this ID is correctly pointing to a menu item.
    }

    private void initializeUIComponents() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        scrollView = findViewById(R.id.scrollView2);
        fragmentContainer = findViewById(R.id.fragment_container);
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
                    selectedFragment = new AccountSettingsFragment();
                } else if (itemId == R.id.fragmentNutrition) {
                    selectedFragment = new NutritionFragment();
                } else if (itemId == R.id.fragmentCalories) {
                    selectedFragment = new CaloriesFragment();
                } else if (itemId == R.id.fragmentFitness) {
                    selectedFragment = new FitnessFragment();
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
