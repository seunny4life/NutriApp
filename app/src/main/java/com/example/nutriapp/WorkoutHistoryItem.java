package com.example.nutriapp;

public class WorkoutHistoryItem {
    private String exerciseType;
    private int duration; // minutes
    private int caloriesBurned; // kcal
    private double distance; // km
    private int averageHeartRate; // bpm
    private String timestamp; // Consider using a Date type for real applications

    // Default constructor is needed for Firestore data retrieval
    public WorkoutHistoryItem() {}

    // Parametrized constructor
    public WorkoutHistoryItem(String exerciseType, int duration, int caloriesBurned, double distance, int averageHeartRate, String timestamp) {
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
        this.distance = distance;
        this.averageHeartRate = averageHeartRate;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getExerciseType() { return exerciseType; }
    public void setExerciseType(String exerciseType) { this.exerciseType = exerciseType; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public int getCaloriesBurned() { return caloriesBurned; }
    public void setCaloriesBurned(int caloriesBurned) { this.caloriesBurned = caloriesBurned; }
    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }
    public int getAverageHeartRate() { return averageHeartRate; }
    public void setAverageHeartRate(int averageHeartRate) { this.averageHeartRate = averageHeartRate; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
