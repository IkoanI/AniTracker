package com.example.anitracker.vnObjects;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VNPage {
    @SerializedName("more")
    boolean more;
    @SerializedName("results")
    List<VNResponse> vnResponseList;

    public boolean hasMore() {
        return more;
    }

    public List<VNDetails> getVnDetailsList() {
        List<VNDetails> vnDetailsList = new ArrayList<>();

        for (VNResponse vnResponse :  this.vnResponseList) {
            vnDetailsList.add(vnResponse.convertToMediaObject());
        }
        return vnDetailsList;
    }

}
