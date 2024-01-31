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
        View view = inflater.inflate(R.layout.fragment_fitness, container, false);

        imageViewWeights = view.findViewById(R.id.imageView4);
        imageViewTreadmill = view.findViewById(R.id.imageView3);

        setupImageViewListeners();

        return view;
    }

    private void setupImageViewListeners() {
        imageViewWeights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WeightsActivity.class);
                startActivity(intent);
                // You can also launch WeightsActivity here if desired
                Toast.makeText(getActivity(), "Weights Section", Toast.LENGTH_SHORT).show();
            }
        });

        imageViewTreadmill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // You can also launch TreadmillActivity here if desired
                Intent intent = new Intent(getActivity(), TreadmillActivity.class);
                startActivity(intent);
                // Show a Toast message when imageViewTreadmill is clicked

                Toast.makeText(getActivity(), "Cardio Section", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
