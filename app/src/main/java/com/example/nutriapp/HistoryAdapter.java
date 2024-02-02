package com.example.nutriapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final List<CompletedExercise> completedExercises;

    // Constructor to initialize the adapter with a list of completed exercises
    public HistoryAdapter(List<CompletedExercise> completedExercises) {
        this.completedExercises = completedExercises;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_completed_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the ViewHolder when it's displayed
        CompletedExercise exercise = completedExercises.get(position);

        // Set the exercise name in the corresponding TextView
        holder.nameTextView.setText(exercise.getExerciseName());

        // Set the exercise duration in the corresponding TextView
        holder.durationTextView.setText(exercise.getDuration());

        // Set the exercise timestamp in the corresponding TextView
        holder.timestampTextView.setText(exercise.getTimestamp());

        // You can add code here to set other exercise details if needed
    }

    @Override
    public int getItemCount() {
        // Return the total number of items in the RecyclerView, which is the size of the completedExercises list
        return completedExercises.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView durationTextView;
        TextView timestampTextView;

        public ViewHolder(View view) {
            super(view);

            // Initialize the TextViews in the ViewHolder
            nameTextView = view.findViewById(R.id.textViewExerciseName);
            durationTextView = view.findViewById(R.id.textViewDuration);
            timestampTextView = view.findViewById(R.id.textViewTimestamp);

            // You can add code here to initialize other views as needed
        }
    }
}
