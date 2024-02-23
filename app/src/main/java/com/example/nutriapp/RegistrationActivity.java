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

        initializeUIComponents();
    }

    private void initializeUIComponents() {
        inputUserName = findViewById(R.id.editTextUserName);
        inputEmail = findViewById(R.id.editTextEmail);
        inputPassword = findViewById(R.id.editTextPassword);
        inputConfirm = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        inputSignIn = findViewById(R.id.textViewAlreadyUser);
        regFireBaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        inputSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });
    }

    private void registerUser() {
        String username = inputUserName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String confirm = inputConfirm.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            inputUserName.setError("Username can't be empty");
            inputUserName.requestFocus();
            return;
        }

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

        if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Password is required");
            inputPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            inputPassword.setError("Password should be at least 6 characters long");
            inputPassword.requestFocus();
            return;
        }

        if (!password.equals(confirm)) {
            inputConfirm.setError("Passwords do not match");
            inputConfirm.requestFocus();
            return;
        }

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
                                                    Toast.makeText(RegistrationActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(RegistrationActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(RegistrationActivity.this, "Failed to check email existence: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
