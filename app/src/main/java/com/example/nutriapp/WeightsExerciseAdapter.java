package com.example.nutriapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.List;

public class WeightsExerciseAdapter extends RecyclerView.Adapter<WeightsExerciseAdapter.ViewHolder> {

    private final List<WeightsExercise> exercises;

    public WeightsExerciseAdapter(List<WeightsExercise> exercises) {
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weights_exercise_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeightsExercise exercise = exercises.get(position);
        holder.nameTextView.setText(exercise.getName());
        holder.setsTextView.setText(MessageFormat.format("Sets: {0}", exercise.getSets()));
        holder.repsTextView.setText(MessageFormat.format("Reps: {0}", exercise.getReps()));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView setsTextView;
        TextView repsTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            setsTextView = itemView.findViewById(R.id.setsTextView);
            repsTextView = itemView.findViewById(R.id.repsTextView);
        }
    }
}
