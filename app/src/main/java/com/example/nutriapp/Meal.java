package com.example.nutriapp;

// Meal.java
public class Meal {
    private String foodName;
    private String servingSize;
    private String calories;

    public Meal(String foodName, String servingSize, String calories) {
        this.foodName = foodName;
        this.servingSize = servingSize;
        this.calories = calories;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getServingSize() {
        return servingSize;
    }

    public void setServingSize(String servingSize) {
        this.servingSize = servingSize;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }
}
