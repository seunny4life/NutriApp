package com.example.nutriapp;

public class FoodItem {
    private String name;
    private int calories;
    // Other fields if necessary

    // Constructor
    public FoodItem(String name, int calories) {
        this.name = name;
        this.calories = calories;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    // Other getters and setters if you have more fields
}
