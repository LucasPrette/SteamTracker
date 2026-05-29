package com.example.steamtracker.entities;

public class WishListEntry {

    private Game game;

    public WishListEntry(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
