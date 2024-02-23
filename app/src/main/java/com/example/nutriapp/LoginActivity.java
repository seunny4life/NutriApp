package com.example.nutriapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText userEmail, userPassword;
    private Button buttonLoginIn;
    private TextView forgotPassword, textViewSignUp;
    public FirebaseAuth signInFireBaseAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components when the activity is created
        initializeUIComponents();
    }

    private void initializeUIComponents() {
        userEmail = findViewById(R.id.inputEmail);
        userPassword = findViewById(R.id.inputPassword);
        buttonLoginIn = findViewById(R.id.buttonSignIn);
        textViewSignUp = findViewById(R.id.textViewSignUp);
        forgotPassword = findViewById(R.id.inputForgotPassword);
        signInFireBaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        // Set click listeners for buttons and text views
        buttonLoginIn();
        textViewSignUp();
        forgotPassword();
    }

    // Handle login button click
    public void buttonLoginIn() {
        buttonLoginIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check user input for validation
                checkValidation();
            }
        });
    }

    // Handle sign-up text view click
    public void textViewSignUp() {
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the registration activity
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }

    // Handle forgot password text view click
    public void forgotPassword() {
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the forgot password activity
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    // Validate user input and attempt to log in
    private void checkValidation() {
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

        if (email.isEmpty()) {
            showError(userEmail, "Email field cannot be empty");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(userEmail, "Please enter a valid email address");
        } else if (password.isEmpty() || password.length() < 8) {
            showError(userPassword, "Password must be at least 8 characters");
        } else {
            progressBar.setVisibility(View.VISIBLE);

            // Attempt to sign in with Firebase authentication
            signInFireBaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        // Login successful, navigate to the main activity
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        navigateToMain();
                    } else {
                        // Login failed, display error message
                        String errorMessage = "Login Failed: " + (task.getException() != null ? task.getException().getMessage() : "Please check your credentials");
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    // Display error message on an input field
    private void showError(EditText input, String message) {
        input.setError(message);
        input.requestFocus();
    }

    // Navigate to the main activity
    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
