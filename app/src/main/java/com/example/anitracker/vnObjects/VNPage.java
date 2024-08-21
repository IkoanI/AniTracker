package com.example.anitracker.vnObjects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VNPage {
    @SerializedName("more")
    boolean more;
    @SerializedName("results")
    List<VNResponse> vnDetailsList;


    public boolean hasMore() {
        return more;
    }

    public List<VNResponse> getVnDetailsList() {
        return vnDetailsList;
    }
}
