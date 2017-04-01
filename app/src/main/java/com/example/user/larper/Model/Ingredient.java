package com.example.user.larper.Model;

/**
 * Created by User on 4/1/2017.
 */

public class Ingredient {

    private String name;
    private float price;

    public Ingredient(String ingName, float ingPrice){
        this.name = ingName;
        this.price = ingPrice;
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public float getPrice() { return this.price; }
    public void setPrice(float price) {
        if (price >= 0) { this.price = price; }
    }
}
