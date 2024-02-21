package com.example.nutriapp;

public class CardioExercise extends Exercise {
    // Constructor for CardioExercise
    public CardioExercise(String name, String duration, int imageResourceId, String description, String benefits) {
        // Call superclass constructor with type set to "Cardio"
        super(name, duration, imageResourceId, description, benefits, "Cardio");
    }
}
