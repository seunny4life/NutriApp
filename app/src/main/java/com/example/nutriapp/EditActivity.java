package com.example.nutriapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class EditActivity extends AppCompatActivity {

    private EditText inputFirstName, inputLastName, inputPlace;
    private Button save, cancel;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Initialize UI components and retrieve user data
        initializeUIComponents();
        displayUserData();
    }

    private void initializeUIComponents() {
        // Initialize UI components
        inputFirstName = findViewById(R.id.firstName);
        inputLastName = findViewById(R.id.lastName);
        inputPlace = findViewById(R.id.location);
        save = findViewById(R.id.buttonSave);
        cancel = findViewById(R.id.buttonCancel);
        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        // Set up click listeners for buttons
        setupSaveButton();
        setupCancelButton();
    }

    private void setupCancelButton() {
        // Set up click listener for cancel button
        cancel.setOnClickListener(v -> finish()); // Close the activity
    }

    private void setupSaveButton() {
        // Set up click listener for save button
        save.setOnClickListener(v -> saveUserData());
    }

    private void displayUserData() {
        // Display user data in the EditText fields
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String firstName = sharedPreferences.getString("firstName", "");
            String lastName = sharedPreferences.getString("lastName", "");

            inputFirstName.setText(firstName);
            inputLastName.setText(lastName);
        }
    }

    private void saveUserData() {
        // Save user data to SharedPreferences and update Firebase user profile
        String firstName = inputFirstName.getText().toString().trim();
        String lastName = inputLastName.getText().toString().trim();
        String location = inputPlace.getText().toString().trim(); // Updated variable name

        if (firstName.isEmpty() || lastName.isEmpty() || location.isEmpty()) {
            // Validate input fields
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // Save first name, last name, and location in SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("firstName", firstName);
            editor.putString("lastName", lastName);
            editor.putString("location", location); // Updated key name
            editor.apply();

            // Update display name
            String displayName = firstName + " " + lastName;
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build();

            currentUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Show success message and return updated location data
                            Toast.makeText(EditActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("updatedLocation", location);
                            setResult(RESULT_OK, intent);
                            finish(); // Finish activity after successful update
                        } else {
                            // Show error message if profile update fails
                            Toast.makeText(EditActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
