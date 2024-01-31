/*
package com.example.nutriapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class CompletedExerciseAdapter extends RecyclerView.Adapter<CompletedExerciseAdapter.ViewHolder> {

    private List<CompletedExercise> completedExercises;

    public CompletedExerciseAdapter(List<CompletedExercise> completedExercises) {
        this.completedExercises = completedExercises;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View exerciseView = inflater.inflate(R.layout.item_completed_exercise, parent, false);
        return new ViewHolder(exerciseView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CompletedExercise exercise = completedExercises.get(position);

        holder.imageViewExercise.setImageResource(exercise.getImageResource());
        holder.textViewExerciseName.setText(exercise.getExerciseName());
        holder.textViewDuration.setText(exercise.getDuration());
    }

    @Override
    public int getItemCount() {
        return completedExercises.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewExercise;
        public TextView textViewExerciseName;
        public TextView textViewDuration;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewExercise = itemView.findViewById(R.id.imageViewExercise);
            textViewExerciseName = itemView.findViewById(R.id.textViewExerciseName);
            textViewDuration = itemView.findViewById(R.id.textViewDuration);
        }
    }
}
*/
