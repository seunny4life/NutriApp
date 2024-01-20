package com.example.nutriapp;

// Import necessary Android classes
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

// LoginActivity class, inheriting from AppCompatActivity for basic app functionality
public class LoginActivity extends AppCompatActivity {
    // Declare UI components
    private EditText userEmail;
    private EditText userPassword;
    private Button buttonLoginIn;
    TextView forgotPassword;
    TextView textViewSignUp;
    private FirebaseAuth signInFireBaseAuth;

    // onCreate method called when activity starts
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to our layout
        setContentView(R.layout.activity_login);

        initializeUIComponents();
    }
    private void initializeUIComponents(){

        // Link UI components with their respective IDs in the layout
        userEmail = findViewById(R.id.inputEmail);
        userPassword = findViewById(R.id.inputPassword);
        buttonLoginIn = findViewById(R.id.buttonSignIn);
        textViewSignUp = findViewById(R.id.textViewSignUp);
        forgotPassword = findViewById(R.id.inputForgotPassword);
        signInFireBaseAuth = FirebaseAuth.getInstance();

        // Initialize button and text view click listeners
        buttonLoginIn();
        textViewSignUp();
        forgotPassword();

    }
    // Set an OnClickListener on the Login button
    public void buttonLoginIn(){
        buttonLoginIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call validation method when button is clicked
                checkValidation();
            }
        });
    }

    // Set an OnClickListener on the Sign Up text view
    public void textViewSignUp(){
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start RegistrationActivity when text view is clicked
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }

    // Set an OnClickListener on the Forgot Password text view
    public void forgotPassword(){
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ForgotPasswordActivity when text view is clicked
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    // Method to validate user input in the Login form
    private void checkValidation() {
        // Retrieve text from EditTexts
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

        // Check if email field is empty
        if (email.isEmpty()) {
            // Show error message if email field is empty
            showError(userEmail, "Invalid UserName");
        } else if (password.isEmpty() || password.length() < 8) {
            // Check if password is empty or less than 8 characters
            showError(userPassword, "Invalid Password!");
        } else {
            // Attempt Firebase sign in with email and password
            signInFireBaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        // Show success message and navigate to MainActivity
                        Toast.makeText(LoginActivity.this, "Login In Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, AccountSettingsActivity.class);
                        startActivity(intent);
                    }else {
                        // Show error message for failed login attempt
                        Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    // Utility method to show an error message on a specific EditText
    private void showError(EditText input, String message) {
        input.setError(message); // Set error message on EditText
        input.requestFocus(); // Focus on EditText to highlight error
    }
}
