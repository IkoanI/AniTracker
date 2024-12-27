package com.example.anitracker.mediaObjects;

public class Titles {
    private String engTitle, natTitle, romTitle, userPref;

    public Titles(String engTitle, String natTitle, String romTitle, String userPref) {
        this.engTitle = engTitle;
        this.natTitle = natTitle;
        this.romTitle = romTitle;
        this.userPref = userPref;
    }

    public Titles(String userPref) {
        this(null, null, null, userPref);
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

    public void setEngTitle(String engTitle) {
        this.engTitle = engTitle;
    }

    public void setNatTitle(String natTitle) {
        this.natTitle = natTitle;
    }

    public void setRomTitle(String romTitle) {
        this.romTitle = romTitle;
    }

    public void setUserPref(String userPref) {
        this.userPref = userPref;
    }
}
