package com.example.anitracker.vnObjects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VNTagPage {
    @SerializedName("results")
    private List<VNTag> tags;

    public List<VNTag> getTags() {
        return tags;
    }
}
