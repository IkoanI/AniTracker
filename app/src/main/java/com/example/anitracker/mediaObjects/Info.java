package com.example.anitracker.mediaObjects;

public class Info {
    String description, value;

    public Info(String description, String value) {
        this.value = value;
        this.description = description;
    }


    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
