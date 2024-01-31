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
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = view.findViewById(R.id.historyRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // Retrieve the list of completed exercises from your data source
        List<CompletedExercise> completedExercises = retrieveCompletedExercises();

        historyAdapter = new HistoryAdapter(completedExercises);
        recyclerView.setAdapter(historyAdapter);

        return view;
    }

    // Implement a method to retrieve the list of completed exercises from your data source
    private List<CompletedExercise> retrieveCompletedExercises() {
        // Replace this with your data retrieval logic
        // You should return a list of completed exercises
        // For now, we'll create a sample list for demonstration purposes
        List<CompletedExercise> sampleData = new ArrayList<>();
        sampleData.add(new CompletedExercise("Push-ups", "Description 1", "30", "Timestamp 2"));
        sampleData.add(new CompletedExercise("Squats", "Description 2", "45", "Timestamp 2"));

        // Add more data as needed
        return sampleData;
    }
}
