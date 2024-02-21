package com.example.nutriapp;

public class Day {
    private final String name;
    private final boolean isCardioDay;
    private final String date;

    public Day(String name, boolean isCardioDay, String date) {
        this.name = name;
        this.isCardioDay = isCardioDay;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public boolean isCardioDay() {
        return isCardioDay;
    }

    public String getDate() {
        return date;
    }
}
