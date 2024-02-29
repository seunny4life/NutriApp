package com.example.nutriapp;

import android.provider.BaseColumns;

public final class WorkoutHistoryContract {

    private WorkoutHistoryContract() {
    }

    public static class WorkoutEntry implements BaseColumns {
        public static final String TABLE_NAME = "workout_history";
        public static final String COLUMN_NAME_EXERCISE_TYPE = "exercise_type";
        public static final String COLUMN_NAME_DURATION = "duration";
        public static final String COLUMN_NAME_CALORIES_BURNED = "calories_burned";
        public static final String COLUMN_NAME_DISTANCE = "distance";
        public static final String COLUMN_NAME_AVERAGE_HEART_RATE = "average_heart_rate";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    }
}
