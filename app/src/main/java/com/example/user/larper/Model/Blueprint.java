package com.example.user.larper.Model;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by User on 4/1/2017.
 */

public class Blueprint {

    private String name;
    private ArrayList<Ingredient> ingredients;
    private String totalCost;
    private String craftingTime;

    public Blueprint(String name, ArrayList<Ingredient> ingredients, String craftDurr) {
        this.name = name;
        this.ingredients = new ArrayList<>(ingredients);
        this.totalCost = this.calcPriceSum(ingredients);
        this.craftingTime = craftDurr;
    }

    public Blueprint(BlueprintAndOwner blueprintowner) {
        this.name = blueprintowner.name;
        this.ingredients = blueprintowner.ingredients;
        this.totalCost = blueprintowner.totalCost;
        this.craftingTime = blueprintowner.craftingTime;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public ArrayList<Ingredient> getIngredients() { return ingredients; }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getTotalCost() { return this.totalCost; }

    public void setTotalCost(String totalCost) { this.totalCost = totalCost; }

    public String getCraftingTime() { return this.craftingTime; }

    public void setCraftingTime(String craftingTime) { this.craftingTime = craftingTime; }

    private String calcPriceSum(ArrayList<Ingredient> ingredients) {
        int sum = 0;
        for (Ingredient ing: ingredients) {
            sum += Integer.parseInt(ing.getPriceSum());
        }
        return Integer.toString(sum);
    }

    @Override
    public boolean equals(Object obj) {
        return this.name.equals(((Blueprint)obj).getName());
    }
}
