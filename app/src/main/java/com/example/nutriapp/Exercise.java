package com.example.nutriapp;

public class Exercise {
    // Fields for exercise details
    private final String name;
    private final String duration;
    private final int imageResourceId;
    private final String description;
    private final String benefits;
    private final String type; // New field for exercise type

    // Constructor to initialize Exercise object
    public Exercise(String name, String duration, int imageResourceId, String description, String benefits, String type) {
        this.name = name;
        this.duration = duration;
        this.imageResourceId = imageResourceId;
        this.description = description;
        this.benefits = benefits;
        this.type = type;
    }

    // Getter method for exercise type
    public String getType() {
        return type;
    }

    // Other getter methods for exercise details
    public String getName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getDescription() {
        return description;
    }

    public String getBenefits() {
        return benefits;
    }
}
//I want if the user clicks on the  Workout session, they should see both Cardio and Weight exercise