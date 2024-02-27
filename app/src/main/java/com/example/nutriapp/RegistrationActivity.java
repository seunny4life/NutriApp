package com.example.nutriapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText inputUserName, inputEmail, inputPassword, inputConfirm;
    private Button buttonRegister;
    private TextView inputSignIn;
    private FirebaseAuth regFireBaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize UI components and setup event listeners
        initializeUIComponents();
    }

    // Method to initialize UI components and setup event listeners
    private void initializeUIComponents() {
        inputUserName = findViewById(R.id.editTextUserName);
        inputEmail = findViewById(R.id.editTextEmail);
        inputPassword = findViewById(R.id.editTextPassword);
        inputConfirm = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        inputSignIn = findViewById(R.id.textViewAlreadyUser);
        regFireBaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        // Register button click listener
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call method to register user
                registerUser();
            }
        });

        // Already registered text view click listener
        inputSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to login activity
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });
    }

    // Method to register a new user
    private void registerUser() {
        String username = inputUserName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String confirm = inputConfirm.getText().toString().trim();

        // Validate username
        if (TextUtils.isEmpty(username)) {
            inputUserName.setError("Username can't be empty");
            inputUserName.requestFocus();
            return;
        }

        // Validate email
        if (TextUtils.isEmpty(email)) {
            inputEmail.setError("Email is required");
            inputEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Enter a valid email");
            inputEmail.requestFocus();
            return;
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Password is required");
            inputPassword.requestFocus();
            return;
        }

        // Check password strength
        if (password.length() < 8 || !password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*")
                || !password.matches(".*\\d.*") || !password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            inputPassword.setError("Password should be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character");
            inputPassword.requestFocus();
            return;
        }

        // Confirm password
        if (!password.equals(confirm)) {
            inputConfirm.setError("Passwords do not match");
            inputConfirm.requestFocus();
            return;
        }

        // Show progress dialog
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        // Check if email already exists
        regFireBaseAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            if (result != null && result.getSignInMethods() != null && result.getSignInMethods().size() > 0) {
                                // Email already exists
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                            } else {
                                // Email does not exist, proceed with registration
                                regFireBaseAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                progressDialog.dismiss();
                                                if (task.isSuccessful()) {
                                                    // Registration successful, store username in Firebase Database
                                                    String userId = regFireBaseAuth.getCurrentUser().getUid();
                                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                                                    userRef.child("username").setValue(username);
                                                    userRef.child("email").setValue(email);

                                                    // Set the display name
                                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                    if (user != null) {
                                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                                .setDisplayName(username)
                                                                .build();

                                                        user.updateProfile(profileUpdates)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                        }
                                                                    }
                                                                });
                                                    }

                                                    Toast.makeText(RegistrationActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                                    finish();
                                                } else {
                                                    // Registration failed
                                                    Toast.makeText(RegistrationActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            progressDialog.dismiss();
                            // Error occurred while checking email existence
                            Toast.makeText(RegistrationActivity.this, "Failed to check email existence: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
