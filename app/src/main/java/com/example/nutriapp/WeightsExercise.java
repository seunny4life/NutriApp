package com.example.nutriapp;

// WeightsExercise.java
public class WeightsExercise {
    private final String name;
    private final int sets;
    private final int reps;

    public WeightsExercise(String name, int sets, int reps) {
        this.name = name;
        this.sets = sets;
        this.reps = reps;
    }

    public String getName() {
        return name;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }
}
