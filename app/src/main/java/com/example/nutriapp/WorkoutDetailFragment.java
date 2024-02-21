package com.example.nutriapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class WorkoutDetailFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_detail, container, false);

        // Retrieve the selected day's name passed from WorkoutFragment
        assert getArguments() != null;
        String selectedDay = getArguments().getString("selectedDay", "Default Day");

        // Use the selectedDay to fetch and display workout details for that day
        // For example, update a TextView with the selected day's name
        TextView textView = view.findViewById(R.id.selected_day_text_view);
        textView.setText(selectedDay);

        return view;
    }
}
