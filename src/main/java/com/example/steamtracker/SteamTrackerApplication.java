package com.example.steamtracker;

import com.example.steamtracker.services.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SteamTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SteamTrackerApplication.class, args);
    };


}
