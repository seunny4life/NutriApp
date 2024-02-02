package com.example.nutriapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;
import androidx.fragment.app.Fragment;

public class FitnessFragment extends Fragment {
    private ImageView imageViewWeights;
    private ImageView imageViewTreadmill;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment_fitness layout
        View view = inflater.inflate(R.layout.fragment_fitness, container, false);

        // Find the ImageView elements in the layout
        imageViewWeights = view.findViewById(R.id.imageView4);
        imageViewTreadmill = view.findViewById(R.id.imageView3);

        // Set up click listeners for the ImageViews
        setupImageViewListeners();

        return view;
    }

    private void setupImageViewListeners() {
        // Set a click listener for imageViewWeights
        imageViewWeights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch WeightsActivity when imageViewWeights is clicked
                Intent intent = new Intent(getActivity(), WeightsActivity.class);
                startActivity(intent);
                // Show a Toast message to indicate Weights Section
                Toast.makeText(getActivity(), "Weights Section", Toast.LENGTH_SHORT).show();
            }
        });

        // Set a click listener for imageViewTreadmill
        imageViewTreadmill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch TreadmillActivity when imageViewTreadmill is clicked (if desired)
                Intent intent = new Intent(getActivity(), TreadmillActivity.class);
                startActivity(intent);
                // Show a Toast message to indicate Cardio Section
                Toast.makeText(getActivity(), "Cardio Section", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
