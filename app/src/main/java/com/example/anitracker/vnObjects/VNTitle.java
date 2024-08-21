package com.example.anitracker.vnObjects;

import com.google.gson.annotations.SerializedName;

public class VNTitle {
    @SerializedName("lang")
    private String lang;
    @SerializedName("official")
    private boolean official;
    @SerializedName("title")
    private String title;
    @SerializedName("latin")
    private String latin;
    @SerializedName("main")
    private boolean main;


    public boolean isOfficial() {
        return official;
    }

    public String getLang() {
        return lang;
    }

    public String getTitle() {
        return title;
    }

    public boolean isMain() {
        return main;
    }

    public String getLatin() {
        return latin;
    }
}
