package com.example.user.larper.Model;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by event on 05/04/2017.
 */

public class BlueprintAndOwner {
    public String name;
    public ArrayList<Ingredient> ingredients;
    public String totalCost;
    public String craftingTime;
    public String owner;

    public BlueprintAndOwner() {
    }

    public BlueprintAndOwner(Blueprint blueprint, String owner) {
        this.name = blueprint.getName();
        this.ingredients = blueprint.getIngredients();
        this.totalCost = blueprint.getTotalCost();
        this.craftingTime = blueprint.getCraftingTime();
        this.owner = owner;
    }
}
