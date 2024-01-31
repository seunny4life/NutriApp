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

    public HistoryAdapter(List<CompletedExercise> completedExercises) {
        this.completedExercises = completedExercises;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_completed_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CompletedExercise exercise = completedExercises.get(position);
        holder.nameTextView.setText(exercise.getExerciseName());
        holder.durationTextView.setText(exercise.getDuration());
        holder.timestampTextView.setText(exercise.getTimestamp());
        // Set other exercise details as needed
    }

    @Override
    public int getItemCount() {
        return completedExercises.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView durationTextView;
        TextView timestampTextView;

        public ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.textViewExerciseName);
            durationTextView = view.findViewById(R.id.textViewDuration);
            timestampTextView = view.findViewById(R.id.textViewTimestamp);
            // Initialize other views as needed
        }
    }
}
