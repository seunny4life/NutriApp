package com.example.nutriapp;

public class Exercise {
    private String name;
    private int reps;
    private boolean isCompleted;

    public Exercise() {
        // Default constructor required for Firebase
    }

    public Exercise(String name, int reps, boolean isCompleted) {
        this.name = name;
        this.reps = reps;
        this.isCompleted = isCompleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
