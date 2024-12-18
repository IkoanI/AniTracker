package com.example.anitracker.vnObjects;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class VNCharacter {
    @SerializedName("id")
    private String id;
    @SerializedName("vns")
    private List<VNResponse> vns;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private Image image;

    public List<VNResponse> getVns() {
        return this.vns;
    }

    public String getId() {
        return this.id;
    }


    public String getName() {
        return this.name;
    }

    public Image getImage() {
        if (this.image == null) {
            return new Image();
        }
        return this.image;
    }
}
