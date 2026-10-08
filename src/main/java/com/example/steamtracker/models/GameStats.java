package com.example.steamtracker.models;

import java.time.Instant;

public class GameStats {
    private int appId;
    private String name;
    private int playTimeForever;
    private int playTime2Weeks;
    private long lastPlayed;

    public GameStats(
            int appId,
            String name,
            int playTimeForever,
            int playTime2Weeks,
            long lastPlayed
       ){
           this.name = name;
           this.appId = appId;
           this.playTimeForever = playTimeForever;
           this.playTime2Weeks = playTime2Weeks;
           this.lastPlayed = lastPlayed;
       }
        public int getPlayTimeForever() {
            return this.playTimeForever;
        }

        public String getName() {
            return this.name;
        }

        public int getAppId() {
            return this.appId;
        }

        public int getPlayTime2Weeks(){
            return this.playTime2Weeks;
        }

        public long getLastPlayed(){ return this.lastPlayed;}
    }
