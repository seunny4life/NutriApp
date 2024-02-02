package com.example.nutriapp;

public class ExerciseCatories {
    private String name;
    private int reps;
    private int sets;

    public ExerciseCatories(String name) {
        this.name = name;
        this.reps = 0;
        this.sets = 0;
    }

    public String getName() {
        return name;
    }

    public int getReps() {
        return reps;
    }

    public int getSets() {
        return sets;
    }

    public void incrementReps() {
        reps++;
    }

    public void incrementSets() {
        sets++;
    }

    public void resetData() {
        reps = 0;
        sets = 0;
    }
}
