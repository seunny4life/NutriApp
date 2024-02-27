package com.example.nutriapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonSignIn;
    private TextView textViewSignUp, textViewForgotPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        initializeUIComponents();

        // Initialize Firebase authentication instance
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void initializeUIComponents() {
        editTextEmail = findViewById(R.id.inputEmail);
        editTextPassword = findViewById(R.id.inputPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        textViewSignUp = findViewById(R.id.textViewSignUp);
        textViewForgotPassword = findViewById(R.id.inputForgotPassword);
        progressBar = findViewById(R.id.progressBar);

        // Set click listeners for buttons and text views
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate user input and attempt login
                loginUser();
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the registration activity
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the forgot password activity
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    // Validate user input and attempt to log in
    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            showError(editTextEmail, "Email field cannot be empty");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(editTextEmail, "Please enter a valid email address");
            return;
        }

        if (password.isEmpty() || password.length() < 8) {
            showError(editTextPassword, "Password must be at least 8 characters");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Attempt to sign in with Firebase authentication
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Login successful, navigate to the main activity
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            navigateToMain();
                        } else {
                            // Login failed, display error message
                            Toast.makeText(LoginActivity.this, "User name or Password is Invalid", Toast.LENGTH_LONG).show();

                            navigateToLogin();
                        }
                    }
                });
    }

    // Display error message on an input field
    private void showError(EditText editText, String message) {
        editText.setError(message);
        editText.requestFocus();
    }

    // Navigate to the main activity
    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Navigate back to the login
    private void navigateToLogin() {
        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}