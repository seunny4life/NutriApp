package com.example.nutriapp;

import java.io.Serializable;

public class WeightsExercise extends Exercise implements Serializable {
    private int sets;
    private int reps;

    // Constructors
    public WeightsExercise(String name, String duration, int imageResourceId, String description, String benefits) {
        super(name, duration, imageResourceId, description, benefits, "Weights");
        this.sets = 3; // Default value for sets
        this.reps = 10; // Default value for reps
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

    // Method to get a formatted string of reps and sets
    public String getRepsSets() {
        return sets + " sets of " + reps + " reps";
    }
}
