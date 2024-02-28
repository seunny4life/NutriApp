package com.example.nutriapp;

import java.io.Serializable;

public class WeightsExercise extends Exercise implements Serializable {
    private int sets;
    private int reps;

    // Constructor with updated parsing of duration string
    public WeightsExercise(String name, String duration, int imageResourceId, String description, String benefits) {
        super(name, duration, imageResourceId, description, benefits, "Weights");
        // Parse the duration string to extract sets and reps
        parseDuration(duration);
    }

    // Method to parse the duration string and set sets and reps
    private void parseDuration(String duration) {
        String[] parts = duration.split(" sets of ");
        sets = Integer.parseInt(parts[0]);
        reps = Integer.parseInt(parts[1].split(" reps")[0]);
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

    // Method to calculate the total duration in seconds
    public int getTotalDurationInSeconds() {
        // Assuming 4 seconds per repetition (adjust this value based on your requirement)
        int secondsPerRepetition = 4;
        return sets * reps * secondsPerRepetition;
    }

    // Updated method to calculate the total duration in seconds based on sets and reps
    public int getTotalDurationInSeconds(int sets, int reps) {
        // Assuming 4 seconds per repetition (adjust this value based on your requirement)
        int secondsPerRepetition = 4;
        return sets * reps * secondsPerRepetition;
    }
}
