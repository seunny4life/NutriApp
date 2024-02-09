package com.example.nutriapp;

import static java.lang.String.format;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CardioExercisesAdapter extends RecyclerView.Adapter<CardioExercisesAdapter.ViewHolder> {
    private final List<CardioExercise> exercises;
    private final Context context;
    private final OnItemClickListener mListener;

    public CardioExercisesAdapter(Context context, List<CardioExercise> exercises, OnItemClickListener listener) {
        this.context = context;
        this.exercises = exercises;
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(CardioExercise exercise);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardio_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardioExercise exercise = exercises.get(position);
        holder.bind(exercise, mListener);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView exerciseImage;
        private final TextView exerciseName;
        private final TextView exerciseDuration;

        ViewHolder(View itemView) {
            super(itemView);
            exerciseImage = itemView.findViewById(R.id.image_cardio_exercise);
            exerciseName = itemView.findViewById(R.id.text_cardio_exercise_name);
            exerciseDuration = itemView.findViewById(R.id.text_cardio_exercise_duration);
        }

        void bind(final CardioExercise exercise, final OnItemClickListener listener) {
            exerciseName.setText(exercise.getName());
            exerciseDuration.setText(format("%d mins", exercise.getDuration()));
            exerciseImage.setImageResource(exercise.getImageResourceId());
            itemView.setOnClickListener(v -> listener.onItemClick(exercise));
        }
    }
}

