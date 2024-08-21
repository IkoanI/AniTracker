package com.example.anitracker.mediaObjects;

public class Titles {
    private final String engTitle, natTitle, romTitle, userPref;

    public Titles(String engTitle, String natTitle, String romTitle, String userPref) {
        this.engTitle = engTitle;
        this.natTitle = natTitle;
        this.romTitle = romTitle;
        this.userPref = userPref;
    }

    public String getEngTitle() {
        return engTitle;
    }

    public String getNatTitle() {
        return natTitle;
    }

    public String getRomTitle() {
        return romTitle;
    }

    public String getUserPref() {
        return userPref;
    }
}
