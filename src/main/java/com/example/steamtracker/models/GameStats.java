package com.example.steamtracker.models;


import com.example.steamtracker.enums.CompletionTier;

public class GameStats {
    private int appId;
    private String name;
    private int playTimeForever;
    private int playTime2Weeks;

    public GameStats(
            int appId,
            String name,
            int playTimeForever,
            int playTime2Weeks
       ){
           this.name = name;
           this.appId = appId;
           this.playTimeForever = playTimeForever;
           this.playTime2Weeks = playTime2Weeks;
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
    }
