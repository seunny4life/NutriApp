package com.example.nutriapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WorkoutHistoryAdapter extends RecyclerView.Adapter<WorkoutHistoryAdapter.WorkoutHistoryViewHolder> {

    private final List<WorkoutHistoryItem> workoutHistoryItems;

    public WorkoutHistoryAdapter(List<WorkoutHistoryItem> workoutHistoryItems) {
        this.workoutHistoryItems = workoutHistoryItems;
    }

    @NonNull
    @Override
    public WorkoutHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_history_item, parent, false);
        return new WorkoutHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutHistoryViewHolder holder, int position) {
        WorkoutHistoryItem item = workoutHistoryItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return workoutHistoryItems.size();
    }

    static class WorkoutHistoryViewHolder extends RecyclerView.ViewHolder {

        private final TextView exerciseTypeTextView;
        private final TextView durationTextView;
        private final TextView caloriesBurnedTextView;
        private final TextView distanceTextView;
        private final TextView heartRateTextView;
        private final TextView timestampTextView;

        WorkoutHistoryViewHolder(View itemView) {
            super(itemView);
            exerciseTypeTextView = itemView.findViewById(R.id.exerciseTypeTextView);
            durationTextView = itemView.findViewById(R.id.durationTextView);
            caloriesBurnedTextView = itemView.findViewById(R.id.caloriesBurnedTextView);
            distanceTextView = itemView.findViewById(R.id.distanceTextView);
            heartRateTextView = itemView.findViewById(R.id.heartRateTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
        }

        void bind(WorkoutHistoryItem item) {
            exerciseTypeTextView.setText(item.getExerciseType());
            durationTextView.setText(String.format("%d mins", item.getDuration()));
            caloriesBurnedTextView.setText(String.format("%d kcal", item.getCaloriesBurned()));
            distanceTextView.setText(String.format("%.2f km", item.getDistance()));
            heartRateTextView.setText(String.format("%d bpm", item.getAverageHeartRate()));
            timestampTextView.setText(item.getTimestamp());
        }
    }
}
