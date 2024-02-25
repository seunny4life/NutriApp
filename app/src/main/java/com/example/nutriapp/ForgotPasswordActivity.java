package com.example.nutriapp;

// Necessary Android and Firebase imports
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    // UI component declarations
    private EditText userEmail;
    private Button buttonReset;
    private ImageButton back;
    private TextView signIn, signUp;
    private FirebaseAuth passForgotFireBaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setting the content view to our layout
        setContentView(R.layout.activity_forgot_password);

        // Initialize UI components
        initializeUIComponents();
    }

    private void initializeUIComponents() {
        // Link UI components with their respective IDs in the layout
        userEmail = findViewById(R.id.editTextFPMail);
        buttonReset = findViewById(R.id.buttonForgotPassword);
        signIn = findViewById(R.id.textViewSignInLink);
        signUp = findViewById(R.id.textViewSignUpLink);
        back = findViewById(R.id.imageButtonBack);
        passForgotFireBaseAuth = FirebaseAuth.getInstance();

        // Setup event listeners for buttons and text views
        buttonReset();
        signIn();
        signUp();
        back();
    }

    private void buttonReset() {
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate and process password reset when buttonSearch is clicked
                checkValidation();
            }
        });
    }

    private void signIn() {
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginActivity when clicked
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            }
        });
    }

    private void signUp() {
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RegistrationActivity when clicked
                startActivity(new Intent(ForgotPasswordActivity.this, RegistrationActivity.class));
            }
        });
    }

    private void checkValidation() {
        // Retrieve and trim email text from EditText
        String email = userEmail.getText().toString().trim();
        // Check if email is not empty
        if (!TextUtils.isEmpty(email)){
            // Send password reset email using Firebase Auth
            passForgotFireBaseAuth.sendPasswordResetEmail(email)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // Notify user of successful password reset email and navigate to LoginActivity
                            Toast.makeText(ForgotPasswordActivity.this, "Password reset link sent", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Notify user if email is invalid
                            Toast.makeText(ForgotPasswordActivity.this, "Email is Invalid", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void back(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the activity and return to the previous screen
                finish();
            }
        });
    }
}
