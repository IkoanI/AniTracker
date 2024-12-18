package com.example.anitracker.vnObjects;

import com.google.gson.annotations.SerializedName;

public class VNEdition {
    @SerializedName("name")
    private String name;

    public String getName() {
        return this.name;
    }
}
