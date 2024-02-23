package com.example.nutriapp;

public class FoodItem {
    private String name;
    private double calories;
    private double servingSizeG;
    private double fatTotalG;
    private double fatSaturatedG;
    private double proteinG;
    private double sodiumMg;
    private double potassiumMg;
    private double cholesterolMg;
    private double carbohydratesTotalG;
    private double fiberG;
    private double sugarG;

    // Constructor with all parameters
    public FoodItem(String name, double calories, double servingSizeG, double fatTotalG, double fatSaturatedG, double proteinG, double sodiumMg, double potassiumMg, double cholesterolMg, double carbohydratesTotalG, double fiberG, double sugarG) {
        this.name = name;
        this.calories = calories;
        this.servingSizeG = servingSizeG;
        this.fatTotalG = fatTotalG;
        this.fatSaturatedG = fatSaturatedG;
        this.proteinG = proteinG;
        this.sodiumMg = sodiumMg;
        this.potassiumMg = potassiumMg;
        this.cholesterolMg = cholesterolMg;
        this.carbohydratesTotalG = carbohydratesTotalG;
        this.fiberG = fiberG;
        this.sugarG = sugarG;
    }

    // Empty constructor for flexibility
    public FoodItem() {
    }

    // Getter methods for all fields
    public String getName() {
        return name;
    }

    public double getCalories() {
        return calories;
    }

    public double getServingSizeG() {
        return servingSizeG;
    }

    public double getFatTotalG() {
        return fatTotalG;
    }

    public double getFatSaturatedG() {
        return fatSaturatedG;
    }

    public double getProteinG() {
        return proteinG;
    }

    public double getSodiumMg() {
        return sodiumMg;
    }

    public double getPotassiumMg() {
        return potassiumMg;
    }

    public double getCholesterolMg() {
        return cholesterolMg;
    }

    public double getCarbohydratesTotalG() {
        return carbohydratesTotalG;
    }

    public double getFiberG() {
        return fiberG;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setServingSizeG(double servingSizeG) {
        this.servingSizeG = servingSizeG;
    }

    public void setFatTotalG(double fatTotalG) {
        this.fatTotalG = fatTotalG;
    }

    public void setFatSaturatedG(double fatSaturatedG) {
        this.fatSaturatedG = fatSaturatedG;
    }

    public void setProteinG(double proteinG) {
        this.proteinG = proteinG;
    }

    public void setSodiumMg(double sodiumMg) {
        this.sodiumMg = sodiumMg;
    }

    public void setPotassiumMg(double potassiumMg) {
        this.potassiumMg = potassiumMg;
    }

    public void setCholesterolMg(double cholesterolMg) {
        this.cholesterolMg = cholesterolMg;
    }

    public void setCarbohydratesTotalG(double carbohydratesTotalG) {
        this.carbohydratesTotalG = carbohydratesTotalG;
    }

    public void setFiberG(double fiberG) {
        this.fiberG = fiberG;
    }

    public void setSugarG(double sugarG) {
        this.sugarG = sugarG;
    }

    public double getSugarG() {
        return sugarG;
    }

    // Setter methods for all fields (optional, based on your needs)
    public void setName(String name) {
        this.name = name;
    }

}
