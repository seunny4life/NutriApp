// Import necessary libraries and classes
package com.example.nutriapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

// Import Android framework classes
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// Import Firebase authentication classes
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

// Define the RegistrationActivity class that extends AppCompatActivity
public class RegistrationActivity extends AppCompatActivity {

    // Define variables for UI components and Firebase authentication
    private EditText inputUserName, inputEmail, inputPassword, inputConfirm;
    private Button buttonRegister;
    private TextView inputSignIn;
    private FirebaseAuth regFireBaseAuth;
    // Regular expression pattern for email validation
    private final Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Link UI components with layout IDs
        inputUserName = findViewById(R.id.editTextUserName);
        inputEmail = findViewById(R.id.editTextEmail);
        inputPassword = findViewById(R.id.editTextPassword);
        inputConfirm = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        inputSignIn = findViewById(R.id.textViewAlreadyUser);
        regFireBaseAuth = FirebaseAuth.getInstance();

        // Set up event listeners for buttons and text views
        buttonRegister();
        inputSignIn();
    }

    // Method to handle registration button click
    public void buttonRegister() {
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
    }

    // Method to navigate to login activity when sign-in text is clicked
    public void inputSignIn() {
        inputSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });
    }

    // Method to validate user input and handle user registration

    private void checkValidation() {
        // Extract text from input fields
        String username = inputUserName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String confirm = inputConfirm.getText().toString().trim();

        // Validate input and show errors if needed
        Matcher matcher = pattern.matcher(email);
        if (username.isEmpty()) {
            showError(inputUserName, "Username can't be Empty or Blank!");
        } else if (username.length() < 4) {
            showError(inputUserName, "Your username is too Short ");
        } else if (email.isEmpty() || !matcher.matches()) {
            showError(inputEmail, "Email is not valid!");
        } else if (password.isEmpty() || password.length() < 8) {
            showError(inputPassword, "Password must be at least 8 characters!");
        } else if (confirm.isEmpty() || (!confirm.equals(password))) {
            showError(inputConfirm, "Password not match!");
        } else {
            // Register user using Firebase authentication
            regFireBaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Handle successful registration
                                Toast.makeText(RegistrationActivity.this, "Registration Successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                // Handle registration failure
                                Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    // Utility method to show error messages on EditText fields
    private void showError(EditText input, String message) {
        input.setError(message);
        input.requestFocus();
    }
}
