package com.example.user.larper.Model;

/**
 * Created by User on 4/1/2017.
 */

public class Ingredient {

    private String name;
    private String price;
    private String quantity;
    private String priceSum;

    public Ingredient() {
        this.price = "0";
        this.quantity = "0";
    }

    public Ingredient(String ingName, String ingPrice, String ingQuantity){
        this.name = ingName;
        this.price = ingPrice;
        this.quantity = ingQuantity;
        this.priceSum = this.calcPriceSum();
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public String getPrice() { return this.price; }
    public void setPrice(String price) {
        if (Integer.parseInt(price) >= 0) { this.price = price; }
    }
    public String getQuantity() { return this.quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }
    public String getPriceSum() { return this.calcPriceSum(); }
    private String calcPriceSum() {
        int price = (Integer)Integer.parseInt(this.getPrice());
        int quantity = (Integer)Integer.parseInt(this.getQuantity());
        return Integer.toString(price*quantity);
    }

}
