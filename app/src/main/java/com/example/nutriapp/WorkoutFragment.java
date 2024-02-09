package com.example.nutriapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast; // Import Toast

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class WorkoutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_workout, container, false);

        // Reference to the "Weights" CardView
        CardView cardWeights = rootView.findViewById(R.id.cardWeights);

        // Reference to the "Cardio" CardView
        CardView cardCardio = rootView.findViewById(R.id.cardCardio);

        // Click listener for the "Weights" CardView
        cardWeights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle "Weights" click event
                // Example: Open a Weights activity
                Intent intent = new Intent(getActivity(), WeightsActivity.class);
                startActivity(intent);
                showToast("You are in the Weights section"); // Show Toast message
            }
        });

        // Click listener for the "Cardio" CardView
        cardCardio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle "Cardio" click event
                // Example: Open a Cardio activity
                Intent intent = new Intent(getActivity(), CardioActivity.class);
                startActivity(intent);
                showToast("You are in the Cardio section"); // Show Toast message
            }
        });

        return rootView;
    }

    // Helper method to show Toast message
    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
