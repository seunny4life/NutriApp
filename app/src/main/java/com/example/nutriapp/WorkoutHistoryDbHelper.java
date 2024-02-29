package com.example.nutriapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WorkoutHistoryDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "workout_history.db";
    private static final int DATABASE_VERSION = 1;

    public WorkoutHistoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WORKOUT_HISTORY_TABLE = "CREATE TABLE " +
                WorkoutHistoryContract.WorkoutEntry.TABLE_NAME + " (" +
                WorkoutHistoryContract.WorkoutEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_EXERCISE_TYPE + " TEXT NOT NULL, " +
                WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_DURATION + " INTEGER NOT NULL, " +
                WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_CALORIES_BURNED + " INTEGER NOT NULL, " +
                WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_DISTANCE + " REAL NOT NULL, " +
                WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_AVERAGE_HEART_RATE + " INTEGER NOT NULL, " +
                WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(SQL_CREATE_WORKOUT_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WorkoutHistoryContract.WorkoutEntry.TABLE_NAME);
        onCreate(db);
    }

    public void deleteWorkout(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(WorkoutHistoryContract.WorkoutEntry.TABLE_NAME,
                WorkoutHistoryContract.WorkoutEntry._ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}
