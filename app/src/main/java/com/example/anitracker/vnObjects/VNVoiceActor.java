package com.example.anitracker.vnObjects;

import com.example.anitracker.mediaObjects.CharacterDetails;
import com.example.anitracker.mediaObjects.Name;
import com.example.anitracker.mediaObjects.StaffDetails;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class VNVoiceActor {
    @SerializedName("staff")
    private VNStaff staff;
    @SerializedName("character")
    private VNCharacter character;
    @SerializedName("note")
    private String note;

    public VNCharacter getCharacter() {
        return character;
    }

    public VNStaff getStaff() {
        return staff;
    }

    public CharacterDetails convertToMediaObject(String vndbID){
        CharacterDetails characterDetails = new CharacterDetails();
        StaffDetails staffDetails = new StaffDetails();
        staffDetails.setName(new Name(this.staff.getName()));
        staffDetails.setLang(this.staff.getLang());
        staffDetails.setImage(staff.getImage().getUrl());
        characterDetails.setVoiceActor(staffDetails);
        characterDetails.setImage(this.character.getImage().getUrl());
        characterDetails.setName(new Name(this.character.getName()));
        for (VNResponse vn : this.character.getVns()){
            if(Objects.equals(vn.getId(), vndbID)){
                characterDetails.setRole(vn.getRole());
                break;
            }
        }
        characterDetails.setNotes(this.note);
        return characterDetails;
    }
}
