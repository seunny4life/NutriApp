package com.example.nutriapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordChangedActivity extends AppCompatActivity {

    private EditText changePassword, confirmedPassword;
    private Button saved, back;
    private FirebaseAuth passwordChangedFireBaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_changed);

        // Initialize UI components and event listeners
        initializeUIComponents();
    }

    private void initializeUIComponents() {
        // Link UI components with their respective IDs in the layout
        changePassword = findViewById(R.id.newPasswordChange);
        confirmedPassword = findViewById(R.id.newPassswordChangeConfirmed);
        back = findViewById(R.id.back2Settings);
        saved = findViewById(R.id.savePasswordChanged);
        passwordChangedFireBaseAuth = FirebaseAuth.getInstance();

        // Setup event listeners for buttons
        saved();
        back();
    }

    private void checkValidation() {
        // Extract text from input fields
        String newPassword = changePassword.getText().toString().trim();
        String newPasswordConfirm = confirmedPassword.getText().toString().trim();
        FirebaseUser user = passwordChangedFireBaseAuth.getCurrentUser();

        // Validate input and show errors if needed
        if (newPassword.isEmpty() || newPassword.length() < 8) {
            // Show an error message for the new password field
            showError(changePassword, "Password must be at least 8 characters!");
        } else if (newPasswordConfirm.isEmpty() || (!newPasswordConfirm.equals(newPassword))) {
            // Show an error message for the confirmed password field
            showError(confirmedPassword, "Passwords do not match!");
        } else {
            assert user != null;
            // Update the user's password in Firebase Authentication
            user.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                // Password changed successfully
                                Toast.makeText(PasswordChangedActivity.this, "Password Changed Successfully", Toast.LENGTH_LONG).show();

                                // Redirect to the Account Settings activity
                                Intent intent = new Intent(PasswordChangedActivity.this, AccountSettingsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                // Handle password change failure
                                Toast.makeText(PasswordChangedActivity.this, "Failed to change password", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void saved(){
        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the activity and return to the previous screen after checking validation
                checkValidation();
            }
        });
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

    private void showError(EditText input, String message) {
        // Display an error message on the specified input field
        input.setError(message);
        // Request focus on the input field to bring it into view
        input.requestFocus();
    }
}
