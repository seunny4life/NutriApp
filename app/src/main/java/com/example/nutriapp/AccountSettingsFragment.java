package com.example.nutriapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.appcompat.app.AlertDialog;


public class AccountSettingsFragment extends Fragment {

    private Button btLogout, btEdit;
    private ImageView imageViewProfile;
    private TextView changePassLiink, notifSettings, fullName, location, email;
    private FirebaseAuth accountSetFireBaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);

        // Initialize UI components and display user data
        initializeUIComponents(view);
        displayUserData();

        return view;
    }

    private void initializeUIComponents(View view) {
        // Link UI components with their respective IDs in the layout
        btEdit = view.findViewById(R.id.buttonEditPersonalInfo);
        btLogout = view.findViewById(R.id.logoutBack);
        changePassLiink = view.findViewById(R.id.ChangePasswordLiink);
        notifSettings = view.findViewById(R.id.notificationSettings);
        fullName = view.findViewById(R.id.displayName);
        location = view.findViewById(R.id.displayLocation);
        email = view.findViewById(R.id.displayEmail);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        accountSetFireBaseAuth = FirebaseAuth.getInstance();

        // Setup event listeners for buttons
        edit();
        logout();
        changePassLiink();
    }

    private void changePassLiink() {
        changePassLiink.setOnClickListener(v -> {
            // Redirect to the PasswordChangedActivity
            startActivity(new Intent(getActivity(), PasswordChangedActivity.class));
        });
    }

    private void edit() {
        btEdit.setOnClickListener(v -> {
            // Redirect to the EditActivity
            startActivity(new Intent(getActivity(), EditActivity.class));
        });
    }
    private void logout() {
        btLogout.setOnClickListener(v -> {
            // Create a confirmation dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure you want to log out?");

            // Add buttons to the dialog
            builder.setPositiveButton("Yes", (dialog, which) -> {
                // User clicked Yes, log out
                FirebaseAuth.getInstance().signOut();

                // Close the app or navigate to the login screen
                if(getActivity() != null) {
                    getActivity().finish(); // Closes the activity hosting this fragment
                }
            });

            builder.setNegativeButton("No", (dialog, which) -> {
                // User clicked No, do nothing
            });

            // Show the dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }


    private void displayUserData() {
        FirebaseUser currentUser = accountSetFireBaseAuth.getCurrentUser();
        if (currentUser != null) {
            email.setText(String.format("Email: %s", currentUser.getEmail()));

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String lastName = dataSnapshot.child("lastName").getValue(String.class);
                        String userLocation = dataSnapshot.child("place").getValue(String.class);

                        fullName.setText(String.format("%s %s", firstName, lastName));
                        location.setText(userLocation);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("AccountSettings", "Database Error: " + databaseError.getMessage());
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        displayUserData(); // Refresh user data
    }
}
