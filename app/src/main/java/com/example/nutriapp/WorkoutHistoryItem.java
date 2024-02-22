package com.example.nutriapp;

public class WorkoutHistoryItem {
    String exerciseType;
    int duration; // minutes
    int caloriesBurned; // kcal
    double distance; // km
    int averageHeartRate; // bpm
    String timestamp;

    public WorkoutHistoryItem(String exerciseType, int duration, int caloriesBurned, double distance, int averageHeartRate, String timestamp) {
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
        this.distance = distance;
        this.averageHeartRate = averageHeartRate;
        this.timestamp = timestamp;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setAverageHeartRate(int averageHeartRate) {
        this.averageHeartRate = averageHeartRate;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public String getExerciseType() {
        return exerciseType;
    }

    public int getDuration() {
        return duration;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public double getDistance() {
        return distance;
    }

    public int getAverageHeartRate() {
        return averageHeartRate;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
