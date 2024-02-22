package com.example.nutriapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class WorkoutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout, container, false);

        ImageView imageViewCardio = rootView.findViewById(R.id.imageViewCardio);
        ImageView imageViewWeight = rootView.findViewById(R.id.imageViewWeight);

        imageViewCardio.setOnClickListener(v -> navigateToCardioSession());
        imageViewWeight.setOnClickListener(v -> navigateToWeightSession());

        return rootView;
    }

    private void navigateToCardioSession() {
        // Replace the current fragment with CardioFragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new CardioFragment())
                .addToBackStack(null) // Consider handling back stack appropriately
                .commit();
    }

    private void navigateToWeightSession() {
        // Replace the current fragment with WeightFragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new WeightFragment())
                .addToBackStack(null) // Consider handling back stack appropriately
                .commit();
    }
}
