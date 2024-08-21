package com.example.anitracker.vnObjects;

import com.google.gson.annotations.SerializedName;

public class VNTag {
    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String desc;

    @SerializedName("id")
    private String id;

    @SerializedName("rating")
    private float rating;

    @SerializedName("spoiler")
    private int spoiler;


    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getId() {
        return id;
    }

    public float getRating() {
        return rating;
    }

    public int getSpoiler() {
        return spoiler;
    }

    public boolean isSpoiler(){
        return spoiler > 0;
    }
}
