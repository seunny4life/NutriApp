package com.example.nutriapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // Initialize the RecyclerView from the layout
        recyclerView = view.findViewById(R.id.historyRecyclerView);

        // Create a LinearLayoutManager for the RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // Retrieve the list of completed exercises from your data source
        List<CompletedExercise> completedExercises = retrieveCompletedExercises();

        // Create a new HistoryAdapter and set it to the RecyclerView
        historyAdapter = new HistoryAdapter(completedExercises);
        recyclerView.setAdapter(historyAdapter);

        return view;
    }

    // Implement a method to retrieve the list of completed exercises from your data source
    private List<CompletedExercise> retrieveCompletedExercises() {
        // Create an empty list to store completed exercises
        List<CompletedExercise> sampleData = new ArrayList<>();

        // Add sample completed exercises to the list
        sampleData.add(new CompletedExercise("Push-ups", "Description 1", "30", "Timestamp 1"));
        sampleData.add(new CompletedExercise("Squats", "Description 2", "45", "Timestamp 2"));
        // You can add more exercises here as needed

        return sampleData;
    }
}
