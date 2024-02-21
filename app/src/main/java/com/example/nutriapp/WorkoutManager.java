package com.example.nutriapp;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WorkoutManager {
    private final FirebaseFirestore db;
    private final String userId;

    public WorkoutManager() {
        this.db = FirebaseFirestore.getInstance();
        this.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void recordWorkout(String type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());

        Map<String, Object> workout = new HashMap<>();
        workout.put("date", today);
        workout.put("type", type);

        db.collection("users").document(userId).collection("workouts").document(today)
                .set(workout)
                .addOnSuccessListener(aVoid -> Log.d("WorkoutManager", "Workout successfully recorded"))
                .addOnFailureListener(e -> Log.e("WorkoutManager", "Error recording workout", e));
    }

    public interface OnRestDayCheckListener {
        void onCheckCompleted(boolean isRestDay);
    }

    public void checkForRestDay(OnRestDayCheckListener listener) {
        db.collection("users").document(userId).collection("workouts")
                .orderBy("date", Query.Direction.DESCENDING).limit(5)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Simplified logic: if 4 workouts in the last 4 records, then rest
                        boolean isRestDay = task.getResult().size() >= 4;
                        listener.onCheckCompleted(isRestDay);
                    } else {
                        listener.onCheckCompleted(false);
                    }
                });
    }
}
