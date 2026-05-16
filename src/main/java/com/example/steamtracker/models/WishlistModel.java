package com.example.steamtracker.models;


public class WishlistModel {
    private int appId;
    private String priority;
    private String dateAdded;

    public WishlistModel(int appId, String priority, String dateAdded) {
        this.appId = appId;
        this.priority = priority;
        this.dateAdded = dateAdded;
    }

    public int getAppId() {
        return appId;
    }

    public String getPriority() {
        return priority;
    }

    public String getDateAdded() {
        return dateAdded;
    }
}
