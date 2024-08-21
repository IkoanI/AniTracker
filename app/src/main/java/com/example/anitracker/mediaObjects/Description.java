package com.example.anitracker.mediaObjects;

import org.jsoup.Jsoup;

public class Description {
    private final String description;
    public Description(String description){
        this.description = Jsoup.parse(description).text();
    }

    public String getDescription() {
        return description;
    }
}
