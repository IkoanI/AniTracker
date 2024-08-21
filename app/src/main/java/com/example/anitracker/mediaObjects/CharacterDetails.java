package com.example.anitracker.mediaObjects;

import com.example.anitracker.CharacterPageQuery;

import java.util.List;

public class CharacterDetails {
    String image, role, notes;
    Name name;
    int id;
    StaffDetails voiceActor;

    public CharacterDetails(){}

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public StaffDetails getVoiceActor() {
        return voiceActor;
    }

    public void setVoiceActor(StaffDetails voiceActor) {
        this.voiceActor = voiceActor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
