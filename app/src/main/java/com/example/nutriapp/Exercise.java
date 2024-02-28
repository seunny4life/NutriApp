package com.example.nutriapp;

import java.io.Serializable;

public class Exercise implements Serializable {
    private final String name;
    private String duration; // Remove the final keyword here
    private final int imageResourceId;
    private final String description;
    private final String benefits;
    private final String type;

    public Exercise(String name, String duration, int imageResourceId, String description, String benefits, String type) {
        this.name = name;
        this.duration = duration;
        this.imageResourceId = imageResourceId;
        this.description = description;
        this.benefits = benefits;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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

    public String getType() {
        return type;
    }
}
