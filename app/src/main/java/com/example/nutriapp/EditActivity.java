package com.example.nutriapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditActivity extends AppCompatActivity {

    private EditText inputFirstName, inputLastName, inputPlace;
    private Button save, cancel;
    private FirebaseAuth editFireBaseAuth;
    private DatabaseReference userDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initializeUIComponents();
        displayUserData();

    }
    private void initializeUIComponents(){

        // Link UI components with layout IDs
        inputFirstName = findViewById(R.id.firstName);
        inputLastName = findViewById(R.id.lastName);
        inputPlace = findViewById(R.id.location);
        save = findViewById(R.id.buttonSave);
        cancel = findViewById(R.id.buttonCancel);
        editFireBaseAuth = FirebaseAuth.getInstance();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize buttonSearch and text view click listeners
        setupSaveButton();
        setupCancelButton();
    }

    private void setupCancelButton() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupSaveButton() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();

            }
        });

    }

    private void displayUserData() {
        FirebaseUser currentUser = editFireBaseAuth.getCurrentUser();
        if (currentUser != null) {
            userDatabaseReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String lastName = dataSnapshot.child("lastName").getValue(String.class);
                        String place = dataSnapshot.child("place").getValue(String.class);

                        inputFirstName.setText(firstName);
                        inputLastName.setText(lastName);
                        inputPlace.setText(place);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    // Handle possible errors
                }
            });
        }
    }
    private void saveUserData() {
        String firstName = inputFirstName.getText().toString().trim();
        String lastName = inputLastName.getText().toString().trim();
        String place = inputPlace.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || place.isEmpty()) {
            // Show error message
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();

            // Optionally, set errors on specific EditTexts
            if (firstName.isEmpty()) {
                inputFirstName.setError("First name is required");
            }
            if (lastName.isEmpty()) {
                inputLastName.setError("Last name is required");
            }
            if (place.isEmpty()) {
                inputPlace.setError("Place is required");
            }
            return; // Stop further execution
        }

        FirebaseUser currentUser = editFireBaseAuth.getCurrentUser();
        if (currentUser != null) {
            userDatabaseReference.child(currentUser.getUid()).child("firstName").setValue(firstName);
            userDatabaseReference.child(currentUser.getUid()).child("lastName").setValue(lastName);
            userDatabaseReference.child(currentUser.getUid()).child("place").setValue(place);
            Toast.makeText(EditActivity.this, "Profile Updated", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(EditActivity.this, "Updating profile failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayUserData(); // Refresh user data
    }


}
