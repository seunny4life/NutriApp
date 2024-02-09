package com.example.nutriapp;

public class CardioExercise {
    private final String name;
    private int duration; // In minutes
    private final int imageResourceId; // Drawable resource ID for the exercise image

    public CardioExercise(String name, int duration, int imageResourceId) {
        this.name = name;
        this.duration = duration;
        this.imageResourceId = imageResourceId;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
