package com.example.nutriapp;

public class MealItem {
    private String userId;
    private String mealName;
    private int calories;

    public MealItem() {
        // Empty constructor needed for Firestore
    }

    public MealItem(String userId, String mealName, int calories) {
        this.userId = userId;
        this.mealName = mealName;
        this.calories = calories;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}
