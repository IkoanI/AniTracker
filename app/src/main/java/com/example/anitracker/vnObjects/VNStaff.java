package com.example.anitracker.vnObjects;

import com.example.anitracker.mediaObjects.Name;
import com.example.anitracker.mediaObjects.StaffDetails;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class VNStaff {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("lang")
    private String lang;
    @SerializedName("role")
    private String role;
    @SerializedName("eid")
    private String edition;
    @SerializedName("note")
    private String note;


    public String getLang() {
        return lang;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getEdition() {
        if (this.edition == null) {
            return -1;
        } else {
            return Integer.parseInt(this.edition);
        }

    }

    private Map<String, String> staffRole = Map.ofEntries(
            Map.entry("scenario","Scenario"),
            Map.entry("director","Director"),
            Map.entry("chardesign","Character design"),
            Map.entry("art","Artist"),
            Map.entry("music","Composer"),
            Map.entry("songs","Vocals"),
            Map.entry("translator","Translator"),
            Map.entry("editor","Editor"),
            Map.entry("qa","Quality assurance"),
            Map.entry("staff","Staff")
    );

    public StaffDetails convertToStaffDetail() {
        StaffDetails staffDetails = new StaffDetails();
        staffDetails.setImage(VNImage.defaultImage);
        if (this.note == null) {
            staffDetails.setRole(staffRole.get(this.role));
        } else {
            staffDetails.setRole(this.note);
        }
        staffDetails.setName(new Name(this.name));
        return staffDetails;
    }

}
