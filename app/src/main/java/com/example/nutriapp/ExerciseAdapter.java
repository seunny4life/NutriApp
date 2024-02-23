package com.example.nutriapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private final List<Exercise> exercises;
    private final OnExerciseClickListener listener;
    private final Context context;
    private int highlightedPosition = -1; // Field to store the highlighted position

    public interface OnExerciseClickListener {
        void onExerciseClick(Exercise exercise, int position);
    }

    public ExerciseAdapter(Context context, List<Exercise> exercises, OnExerciseClickListener listener) {
        this.context = context;
        this.exercises = exercises;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        final Exercise exercise = exercises.get(position);
        holder.bind(exercise);

        // Highlight the current exercise
        if (position == highlightedPosition) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.red)); // Adjust with your highlight color
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent)); // Default color
        }

        setAnimation(holder.itemView);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void highlightItem(int position) {
        highlightedPosition = position;
        notifyDataSetChanged();
    }

    private void setAnimation(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_fade_in);
        viewToAnimate.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {
        ImageView exerciseImage;
        TextView exerciseName, exerciseDuration, exerciseDescription, exerciseBenefits, exerciseRepsSets;

        public ExerciseViewHolder(@NonNull View itemView, OnExerciseClickListener listener) {
            super(itemView);
            exerciseImage = itemView.findViewById(R.id.exerciseImage);
            exerciseName = itemView.findViewById(R.id.exerciseName);
            exerciseDuration = itemView.findViewById(R.id.exerciseDuration);
            exerciseDescription = itemView.findViewById(R.id.exerciseDescription);
            exerciseBenefits = itemView.findViewById(R.id.exerciseBenefits);
            exerciseRepsSets = itemView.findViewById(R.id.exerciseRepsSets);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onExerciseClick(exercises.get(position), position);
                }
            });
        }

        void bind(Exercise exercise) {
            exerciseName.setText(exercise.getName());
            exerciseDuration.setText(exercise.getDuration());
            // Ensure you have a valid drawable resource or handle it accordingly
            exerciseImage.setImageResource(exercise.getImageResourceId());
            exerciseDescription.setText(exercise.getDescription());
            exerciseBenefits.setText(exercise.getBenefits());

            // Hide or show reps/sets info based on the type of exercise
            exerciseRepsSets.setVisibility(exercise instanceof WeightsExercise ? View.VISIBLE : View.GONE);
            if (exercise instanceof WeightsExercise) {
                WeightsExercise weightsExercise = (WeightsExercise) exercise;
                exerciseRepsSets.setText(weightsExercise.getRepsSets());
            }
        }
    }
}
