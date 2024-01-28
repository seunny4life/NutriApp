package com.example.nutriapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private  BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     initializeUIComponents();

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.mainActivity); // Set as per your default fragment
    }
    private void initializeUIComponents(){
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigation();

    }
    private void bottomNavigation(){
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.mainActivity) {
                selectedFragment = new Fragment();
            } else if (itemId == R.id.accountActivity) {
                selectedFragment = new AccountSettingsFragment();
            } else if (itemId == R.id.fragmentNutrition) {
                selectedFragment = new NutritionFragment();
            } else if (itemId == R.id.fragmentCalories) {
            selectedFragment = new CaloriesFragment();

        }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });
    }

}
