package com.example.anitracker.vnObjects;

import com.example.anitracker.mediaObjects.Name;
import com.example.anitracker.mediaObjects.StaffDetails;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VNStaff {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("original")
    private String original;
    @SerializedName("lang")
    private String lang;
    @SerializedName("gender")
    private String gender;
    @SerializedName("description")
    private String description;
    @SerializedName("aliases")
    private List<VNAlias> aliases;
    @SerializedName("extlinks")
    private List<VNLink> extlinks;

    // VN Staff fragment only
    @SerializedName("role")
    private String role;
    @SerializedName("eid")
    private String edition;
    @SerializedName("note")
    private String note;

    private final Map<String, String> staffRole = Map.ofEntries(
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

    public StaffDetails convertToStaffDetail() {
        StaffDetails staffDetails = new StaffDetails();
        staffDetails.setId(this.id);
        Name name = new Name(this.name);
        name.setNativeName(this.original);
        if (aliases != null && !aliases.isEmpty()) {
            List<String> alternatives = new ArrayList<>();
            for (VNAlias alias : aliases) {
                alternatives.add(alias.getName());
            }
            name.setAlternatives(alternatives);
        }
        staffDetails.setName(name);
        staffDetails.setLang(VNLanguage.languageMap.get(this.lang));
        staffDetails.setGender(this.getGender());
        staffDetails.setDescription(this.getDescription());
        staffDetails.setImage(VNImage.defaultImage);
        if (this.note == null) {
            staffDetails.setRole(staffRole.get(this.role));
        } else {
            staffDetails.setRole(String.format("%s (%s)", staffRole.get(this.role), this.note));
        }
        staffDetails.setLinks(this.extlinks);
        return staffDetails;
    }

    public String getGender() {
        if (this.gender == null) {
            return null;
        }

        if (this.gender.equals("m")) {
            return "Male";
        } else {
            return "Female";
        }
    }

    public String getDescription() {
        return this.description == null ? null : VNDBParser.parseText(this.description);
    }

    public List<VNAlias> getAliases() {
        return aliases;
    }

    public String getRole() {
        return role;
    }

    public String getNote() {
        return note;
    }

    public List<VNLink> getExtlinks() {
        return extlinks;
    }
}
