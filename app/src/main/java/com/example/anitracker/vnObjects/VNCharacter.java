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
        return vns;
    }

    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public String getRole(String vndbID){
        for(VNResponse vn : this.vns){
            if (Objects.equals(vn.getId(), vndbID)){
                return vn.getRole();
            }
        }

        return "None";
    }

    public Image getImage() {
        return image;
    }
}
