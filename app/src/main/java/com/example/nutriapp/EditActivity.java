package com.example.nutriapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditActivity extends AppCompatActivity {

    private EditText inputFirstName, inputLastName, inputPlace;
    private Button save, cancel;
    private FirebaseAuth editFirebaseAuth;
    private DatabaseReference userDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Initialize UI components and display user data
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
        editFirebaseAuth = FirebaseAuth.getInstance();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference();

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

                Log.d("Tag", "Message"); // Log message for debugging
            }
        });
    }

    private void displayUserData() {
        // Display user data in EditText fields
        FirebaseUser currentUser = editFirebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // Retrieve user data from Firebase Realtime Database
            userDatabaseReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Retrieve user data
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String lastName = dataSnapshot.child("lastName").getValue(String.class);
                        String place = dataSnapshot.child("place").getValue(String.class);

                        // Set retrieved data in EditText fields
                        inputFirstName.setText(firstName);
                        inputLastName.setText(lastName);
                        inputPlace.setText(place);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle possible errors for future use
                }
            });
        }
    }

    private void saveUserData() {
        // Save user data to Firebase Realtime Database
        String firstName = inputFirstName.getText().toString().trim();
        String lastName = inputLastName.getText().toString().trim();
        String place = inputPlace.getText().toString().trim();

        // Validate user input
        if (firstName.isEmpty() || lastName.isEmpty() || place.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            if (firstName.isEmpty()) {
                inputFirstName.setError("First name is required");
            }
            if (lastName.isEmpty()) {
                inputLastName.setError("Last name is required");
            }
            if (place.isEmpty()) {
                inputPlace.setError("Place is required");
            }
            return;
        }

        // Get current user
        FirebaseUser currentUser = editFirebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // Update user profile data in Firebase Realtime Database
            DatabaseReference userRef = userDatabaseReference.child(currentUser.getUid());
            userRef.child("firstName").setValue(firstName);
            userRef.child("lastName").setValue(lastName);
            userRef.child("place").setValue(place)
                    .addOnSuccessListener(aVoid -> {
                        // Profile update successful
                        Intent intent = new Intent();
                        intent.putExtra("updatedLocation", place);
                        setResult(Activity.RESULT_OK, intent); // Set result to return updated location data
                        Toast.makeText(EditActivity.this, "Profile Updated", Toast.LENGTH_LONG).show();
                        finish(); // Finish activity after successful update
                    })
                    .addOnFailureListener(e -> {
                        // Profile update failed
                        Toast.makeText(EditActivity.this, "Failed to update profile", Toast.LENGTH_LONG).show();
                    });
        } else {
            // Current user is null, indicating authentication issue
            Toast.makeText(EditActivity.this, "Authentication error, please sign in again", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayUserData(); // Refresh user data
    }
}
