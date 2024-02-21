package com.example.nutriapp;

public class WeightsExercise extends Exercise {
    private int sets;
    private int reps;

    // Constructor
    public WeightsExercise(String name, String duration, int imageResourceId, String description, String benefits) {
        super(name, duration, imageResourceId, description, benefits, "Weights");
    }

    // Returns formatted sets x reps
    public String getRepsSets() {
        return sets + "x" + reps;
    }

    // Getter for sets
    public int getSets() {
        return sets;
    }

    // Setter for sets
    public void setSets(int sets) {
        this.sets = sets;
    }

    // Getter for reps
    public int getReps() {
        return reps;
    }

    // Setter for reps
    public void setReps(int reps) {
        this.reps = reps;
    }
}
