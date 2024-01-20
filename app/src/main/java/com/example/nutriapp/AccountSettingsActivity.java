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
    TextView ChangePassLiink, notifSettings, dateOfBirth, userName, email;
    private FirebaseAuth accountSetFireBaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        initializeUIComponents();
        //initializeUI();
        displayUserData();

    }

    private void initializeUIComponents() {
        edit = findViewById(R.id.buttonEditPersonalInfo);
        back = findViewById(R.id.buttonBack);
        ChangePassLiink = findViewById(R.id.ChangePasswordLiink);
        notifSettings = findViewById(R.id.notificationSettings);
        dateOfBirth = findViewById(R.id.displayAge);
        userName = findViewById(R.id.displayUserName);
        email = findViewById(R.id.displayEmail);
        accountSetFireBaseAuth = FirebaseAuth.getInstance();

        edit();
        back();

    }
    private void edit(){
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountSettingsActivity.this, EditActivity.class));
            }
        });
    }
    private void back(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher();
                finish();
            }
        });
    }
    private void displayUserData() {
        FirebaseUser currentUser = accountSetFireBaseAuth.getCurrentUser();
        if (currentUser != null) {
            // Display the email from Firebase Auth
            String userEmail = currentUser.getEmail();
            email.setText(userEmail);
            String user = currentUser.getDisplayName();
            userName.setText(user);


            // Fetch username from Firebase Database
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
                    // Handle possible errors.
                }
            });
        }
    }


}