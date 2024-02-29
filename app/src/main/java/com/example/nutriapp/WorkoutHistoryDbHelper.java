package com.example.nutriapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WorkoutHistoryDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WorkoutHistory.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WorkoutHistoryContract.WorkoutEntry.TABLE_NAME + " (" +
                    WorkoutHistoryContract.WorkoutEntry._ID + " INTEGER PRIMARY KEY," +
                    WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_EXERCISE_TYPE + " TEXT," +
                    WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_DURATION + " INTEGER," +
                    WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_CALORIES_BURNED + " INTEGER," +
                    WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_DISTANCE + " REAL," +
                    WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_AVERAGE_HEART_RATE + " INTEGER," +
                    WorkoutHistoryContract.WorkoutEntry.COLUMN_NAME_TIMESTAMP + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WorkoutHistoryContract.WorkoutEntry.TABLE_NAME;

    public WorkoutHistoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
