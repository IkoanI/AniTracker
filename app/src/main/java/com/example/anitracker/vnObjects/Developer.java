package com.example.anitracker.vnObjects;

import com.google.gson.annotations.SerializedName;

public class Developer {
    @SerializedName("id")
    String id;
    @SerializedName("name")
    String name;
    @SerializedName("original")
    String original;
    @SerializedName("aliases")
    String aliases;
    @SerializedName("lang")
    String lang;
    @SerializedName("type")
    String type;
    @SerializedName("description")
    String description;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOriginal() {
        return original;
    }

    public String getLang() {
        return lang;
    }

    public String getAliases() {
        return aliases;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
