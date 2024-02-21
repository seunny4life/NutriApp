package com.example.nutriapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WeightFragment extends Fragment implements ExerciseAdapter.OnExerciseClickListener {

    private List<Exercise> exercises;
    private ExerciseAdapter adapter;
    private Handler exerciseHandler = new Handler(Looper.getMainLooper());
    private int currentExerciseIndex = 0;
    private ToneGenerator toneGenerator;
    private Vibrator vibrator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weight, container, false);

        toneGenerator = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        exercises = generateWeightExercises();

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view_exercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ExerciseAdapter(getContext(), exercises, this);
        recyclerView.setAdapter(adapter);

        Button startButton = rootView.findViewById(R.id.startWeightSessionButton);
        startButton.setOnClickListener(v -> startExercises());

        return rootView;
    }

    private List<Exercise> generateWeightExercises() {
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(new WeightsExercise("Squats", "1", R.drawable.cat_cow_stretch_a, "Description here", "Benefits here"));
        exercises.add(new WeightsExercise("drt_land_swim_a", "1", R.drawable.drt_land_swim_a, "Description here", "Benefits here"));

        // Add more exercises as needed
        return exercises;
    }

    @Override
    public void onExerciseClick(Exercise exercise, int position) {
        if (exercise instanceof WeightsExercise) {
            WeightsExercise weightsExercise = (WeightsExercise) exercise;
            showAdjustmentDialog(getContext(), weightsExercise, position);
        }
    }

    private void showAdjustmentDialog(Context context, WeightsExercise exercise, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_adjust_reps_sets, null);
        builder.setView(dialogView);

        NumberPicker setsPicker = dialogView.findViewById(R.id.setsPicker);
        NumberPicker repsPicker = dialogView.findViewById(R.id.repsPicker);

        setsPicker.setMinValue(1);
        setsPicker.setMaxValue(20);
        repsPicker.setMinValue(1);
        repsPicker.setMaxValue(50);

        setsPicker.setValue(exercise.getSets());
        repsPicker.setValue(exercise.getReps());

        builder.setPositiveButton("Save", (dialog, which) -> {
            int newSets = setsPicker.getValue();
            int newReps = repsPicker.getValue();

            exercise.setSets(newSets);
            exercise.setReps(newReps);

            adapter.notifyItemChanged(position);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void startExercises() {
        if (currentExerciseIndex >= exercises.size()) {
            Toast.makeText(getContext(), "Workout session completed!", Toast.LENGTH_SHORT).show();
            currentExerciseIndex = 0; // Reset for next session
            return;
        }

        Exercise currentExercise = exercises.get(currentExerciseIndex);
        Toast.makeText(getContext(), "Starting: " + currentExercise.getName(), Toast.LENGTH_SHORT).show();
        playSoundAndVibrate();

        // Start ExerciseDetailActivity with details of the current exercise
        Intent intent = new Intent(getActivity(), ExerciseDetailActivity.class);
        intent.putExtra("EXERCISE_NAME", currentExercise.getName());
        intent.putExtra("EXERCISE_IMAGE", currentExercise.getImageResourceId());
        intent.putExtra("EXERCISE_TYPE", currentExercise.getType());
        intent.putExtra("EXERCISE_DESCRIPTION", ((WeightsExercise) currentExercise).getDescription());
        intent.putExtra("EXERCISE_BENEFITS", ((WeightsExercise) currentExercise).getBenefits());
        startActivity(intent);

        // Update adapter to highlight current exercise
        adapter.highlightItem(currentExerciseIndex);
        adapter.notifyDataSetChanged();

        // Start countdown for exercise duration
        exerciseHandler.postDelayed(() -> {
            currentExerciseIndex++;
            if (currentExerciseIndex < exercises.size()) {
                startExercises(); // Start next exercise after delay
            } else {
                Toast.makeText(getContext(), "Workout session completed!", Toast.LENGTH_SHORT).show();
            }
        }, 60000); // 60-second delay for each exercise
    }

    private void playSoundAndVibrate() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean soundEnabled = prefs.getBoolean("pref_key_sound_notifications", true);
        boolean vibrationEnabled = prefs.getBoolean("pref_key_vibration_notifications", true);

        if (soundEnabled) {
            toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
        }
        if (vibrationEnabled && vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(500);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (toneGenerator != null) {
            toneGenerator.release();
        }
        if (exerciseHandler != null) {
            exerciseHandler.removeCallbacksAndMessages(null);
        }
    }
}
