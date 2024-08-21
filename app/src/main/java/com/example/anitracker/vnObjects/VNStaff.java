package com.example.anitracker.vnObjects;

import com.google.gson.annotations.SerializedName;

public class VNStaff {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("lang")
    private String lang;
    private Image image;

    public String getLang() {
        return lang;
    }

    public String getId() {
        return id;
    }

    public Image getImage(){
        if(this.image == null){
            return new Image();
        }
         return image;
    }

    public String getName() {
        return name;
    }

}
