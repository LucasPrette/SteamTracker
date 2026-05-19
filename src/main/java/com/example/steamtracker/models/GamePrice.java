package com.example.steamtracker.models;

public class GamePrice {
    private String name;
    private double finalPrice;
    private double originalPrice;
    private Double discount;

    public GamePrice(String name, double finalPrice, double originalPrice, Double discount){
        this.name = name;
        this.finalPrice = finalPrice;
        this.originalPrice = originalPrice;
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public Double getDiscount() {
        return discount;
    }
}
