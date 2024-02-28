package com.example.nutriapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutriapp.Exercise;
import com.example.nutriapp.WeightsExercise;
import com.example.nutriapp.R;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private final List<Exercise> exercises;
    private final OnExerciseClickListener listener;
    private final Context context;
    private int highlightedPosition = -1; // Field to store the highlighted positionWeigt
    private long doubleClickLastTime = 0;

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
                    long clickTime = System.currentTimeMillis();
                    if (clickTime - doubleClickLastTime < 500) {
                        // Double click detected
                        doubleClickLastTime = 0;
                        showAdjustDialog(exercises.get(position));
                    } else {
                        doubleClickLastTime = clickTime;
                        listener.onExerciseClick(exercises.get(position), position);
                    }
                }
            });
        }

        void bind(Exercise exercise) {
            exerciseName.setText(exercise.getName());
            exerciseDuration.setText(exercise.getDuration());
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

        @SuppressLint("NotifyDataSetChanged")
        private void showAdjustDialog(Exercise exercise) {
            if (exercise instanceof WeightsExercise) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Adjust Sets and Reps");

                View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_adjust_reps_sets, null);
                builder.setView(dialogView);

                NumberPicker setsPicker = dialogView.findViewById(R.id.setsPicker);
                NumberPicker repsPicker = dialogView.findViewById(R.id.repsPicker);

                // Set the range and initial value for setsPicker
                setsPicker.setMaxValue(20); // Maximum value for sets
                setsPicker.setMinValue(1); // Minimum value for sets
                setsPicker.setValue(((WeightsExercise) exercise).getSets()); // Initial value for sets

                // Set the range and initial value for repsPicker
                repsPicker.setMaxValue(50); // Maximum value for reps
                repsPicker.setMinValue(1); // Minimum value for reps
                repsPicker.setValue(((WeightsExercise) exercise).getReps()); // Initial value for reps

                builder.setPositiveButton("OK", (dialog, which) -> {
                    int sets = setsPicker.getValue();
                    int reps = repsPicker.getValue();

                    ((WeightsExercise) exercise).setSets(sets);
                    ((WeightsExercise) exercise).setReps(reps);

                    // Update the duration based on sets and reps
                    int seconds = sets * reps * 4; // Assuming 4 seconds per repetition
                    String duration = seconds + " seconds";
                    exercise.setDuration(duration);

                    notifyDataSetChanged();
                });

                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

                builder.create().show();
            } else {
                Toast.makeText(context, "Adjustments are only available for weight exercises.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
