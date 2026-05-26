package com.example.steamtracker.entities;

public class GamePriceOffer {
    private String providerName;
    private String gameName;
    private String store; // store name
    private double originalPrice;
    private double finalPrice;
    private double discount;
    private String storeUrl;

    public GamePriceOffer() {

    }

    public GamePriceOffer(
            String gameName,
            String store,
            double originalPrice,
            double finalPrice,
            double discount,
            String storeUrl,
            String providerName
    ) {
        this.gameName = gameName;
        this.store = store;
        this.originalPrice = originalPrice;
        this.finalPrice = finalPrice;
        this.discount = discount;
        this.storeUrl = storeUrl;
        this.providerName = providerName;
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

    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = this.storeUrl;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
}
