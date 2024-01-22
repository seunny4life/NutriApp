package com.example.nutriapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountSettingsActivity extends AppCompatActivity {
    private Button back, edit;
    TextView changePassLiink, notifSettings, dateOfBirth, userName, email;
    private FirebaseAuth accountSetFireBaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        // Initialize UI components and display user data
        initializeUIComponents();
        displayUserData();
    }

    private void initializeUIComponents() {
        // Link UI components with their respective IDs in the layout
        edit = findViewById(R.id.buttonEditPersonalInfo);
        back = findViewById(R.id.buttonBack);
        changePassLiink = findViewById(R.id.ChangePasswordLiink);
        notifSettings = findViewById(R.id.notificationSettings);
        dateOfBirth = findViewById(R.id.displayAge);
        userName = findViewById(R.id.displayUserName);
        email = findViewById(R.id.displayEmail);
        accountSetFireBaseAuth = FirebaseAuth.getInstance();

        // Setup event listeners for buttons
        edit();
        back();
        changePassLiink();
    }

    private void changePassLiink() {
        // Handle click event for changing password link
        changePassLiink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the PasswordChangedActivity
                startActivity(new Intent(AccountSettingsActivity.this, PasswordChangedActivity.class));
            }
        });
    }

    private void edit(){
        // Handle click event for edit button
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the EditActivity
                startActivity(new Intent(AccountSettingsActivity.this, EditActivity.class));
            }
        });
    }

    private void back(){
        // Handle click event for back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the activity and return to the previous screen
                finish();
            }
        });
    }

    private void displayUserData() {
        FirebaseUser currentUser = accountSetFireBaseAuth.getCurrentUser();
        if (currentUser != null) {
            // Display the email from Firebase Auth
            String userEmail = currentUser.getEmail();
            email.setText(String.format("Email: %s", userEmail));

            // Fetch and display username from Firebase Database
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Assuming 'username' is a field in your database
                        String username = dataSnapshot.child("username").getValue(String.class);
                        userName.setText(username);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle possible errors when fetching data from the database.
                }
            });
        }
    }
}
