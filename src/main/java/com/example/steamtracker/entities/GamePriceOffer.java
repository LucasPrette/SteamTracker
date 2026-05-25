package com.example.steamtracker.entities;

public class GamePriceOffer {
    private String gameName;
    private String store; // store name
    private double originalPrice;
    private double finalPrice;
    private double discount;
    private String url;

    public GamePriceOffer() {
    }

    public GamePriceOffer(
            String gameName,
            String store,
            double originalPrice,
            double finalPrice,
            double discount,
            String url
    ) {
        this.gameName = gameName;
        this.store = store;
        this.originalPrice = originalPrice;
        this.finalPrice = finalPrice;
        this.discount = discount;
        this.url = url;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
