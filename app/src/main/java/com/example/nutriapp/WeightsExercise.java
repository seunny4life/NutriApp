package com.example.nutriapp;

public class WeightsExercise extends Exercise {
    private int sets;
    private int reps;

    // Constructors
    public WeightsExercise(String name, String duration, int imageResourceId, String description, String benefits) {
        super(name, duration, imageResourceId, description, benefits, "Weights");
        this.sets = 3; // Default value for sets
        this.reps = 10; // Default value for reps
    }

    // Returns formatted sets x reps
    public String getRepsSets() {
        return sets + "x" + reps;
    }

    // Getter for sets
    public int getSets() {
        return sets;
    }

    // Setter for sets with validation
    public void setSets(int sets) {
        if (sets > 0) {
            this.sets = sets;
        } else {
            throw new IllegalArgumentException("Number of sets must be greater than zero.");
        }
    }

    // Getter for reps
    public int getReps() {
        return reps;
    }

    // Setter for reps with validation
    public void setReps(int reps) {
        if (reps > 0) {
            this.reps = reps;
        } else {
            throw new IllegalArgumentException("Number of reps must be greater than zero.");
        }
    }

    // Override toString() method
    @Override
    public String toString() {
        return "WeightsExercise{" +
                "name='" + getName() + '\'' +
                ", sets=" + sets +
                ", reps=" + reps +
                '}';
    }
}
