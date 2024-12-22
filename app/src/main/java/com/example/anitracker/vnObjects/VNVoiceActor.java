package com.example.anitracker.vnObjects;

import com.google.gson.annotations.SerializedName;

public class VNVoiceActor {
    @SerializedName("staff")
    private VNStaff staff;
    @SerializedName("character")
    private VNCharacter character;


    public VNStaff getStaff() {
        return this.staff;
    }

    public VNCharacter getCharacter() {
        return this.character;
    }
}
