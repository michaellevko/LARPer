package com.example.user.larper.Model;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by User on 4/1/2017.
 */

public class Blueprint {

    private String uuid;
    private String name;
    private ArrayList<Ingredient> ingredients;
    private float priceSum;
    private int craftingTimeHours;
    private int craftingTimeMinutes;

    public Blueprint(String name, ArrayList<Ingredient> ingredients,
                     int craftingHours, int craftingMinutes) {
        this.name = name;
        this.ingredients = new ArrayList<>(ingredients);
        this.priceSum = this.calcPriceSum(ingredients);
        this.craftingTimeHours = craftingHours;
        this.craftingTimeMinutes = craftingMinutes;
        this.uuid = UUID.randomUUID().toString();
    }

    public String getName() { return name; }

    public String getUUID() {return this.uuid;}

    public void setName(String name) { this.name = name; }

    public ArrayList<Ingredient> getIngredients() { return ingredients; }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public float getPriceSum() { return priceSum; }

    public void setPriceSum(float priceSum) { this.priceSum = priceSum; }

    public int getCraftingTimeHours() { return craftingTimeHours; }

    public void setCraftingTimeHours(int craftingTimeHours) {
        this.craftingTimeHours = craftingTimeHours;
    }

    public int getCraftingTimeMinutes() { return craftingTimeMinutes; }

    public void setCraftingTimeMinutes(int craftingTimeMinutes) {
        this.craftingTimeMinutes = craftingTimeMinutes;
    }

    private float calcPriceSum(ArrayList<Ingredient> ingredients) {
        float sum = 0;
        for (Ingredient ing: ingredients) {
            sum += ing.getPrice();
        }
        return sum;
    }

    @Override
    public boolean equals(Object obj) {
        return this.name.equals(((Blueprint)obj).getName());
    }
}
