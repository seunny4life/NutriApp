package com.example.nutriapp;

public class CompletedExercise {
    private final String exerciseName;
    private final String description;
    private final String duration;
    private final String timestamp;

    public CompletedExercise(String exerciseName, String description, String duration, String timestamp) {
        this.exerciseName = exerciseName;
        this.description = description;
        this.duration = duration;
        this.timestamp = timestamp;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public String getDescription() {
        return description;
    }

    public String getDuration() {
        return duration;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
