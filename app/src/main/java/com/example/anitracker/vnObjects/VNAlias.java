package com.example.anitracker.vnObjects;

import com.google.gson.annotations.SerializedName;

public class VNAlias {
    @SerializedName("aid")
    private int aliasID;
    @SerializedName("name")
    private String name;
    @SerializedName("latin")
    private String latin;
    @SerializedName("ismain")
    private boolean ismain;


    public int getAliasID() {
        return aliasID;
    }

    public String getName() {
        return name;
    }

    public String getLatin() {
        return latin;
    }

    public boolean isIsmain() {
        return ismain;
    }
}
