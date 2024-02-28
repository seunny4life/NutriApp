package com.example.nutriapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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

        initializeUIComponents();
        displayUserData();
    }

    private void initializeUIComponents(){
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
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the activity
            }
        });
    }

    private void setupSaveButton() {
        // Set up click listener for save button
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData(); // Call method to save user data
            }
        });
    }

    private void displayUserData() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String firstName = sharedPreferences.getString("firstName", "");
            String lastName = sharedPreferences.getString("lastName", "");

            inputFirstName.setText(firstName);
            inputLastName.setText(lastName);
        }
    }

    private void saveUserData() {
        String firstName = inputFirstName.getText().toString().trim();
        String lastName = inputLastName.getText().toString().trim();
        String place = inputPlace.getText().toString().trim();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // Save first name and last name in SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("firstName", firstName);
            editor.putString("lastName", lastName);
            editor.apply();

            // Update display name
            String displayName = firstName + " " + lastName;
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build();

            currentUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("updatedLocation", place);
                            setResult(RESULT_OK, intent); // Set result to return updated location data
                            finish(); // Finish activity after successful update
                        } else {
                            Toast.makeText(EditActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
