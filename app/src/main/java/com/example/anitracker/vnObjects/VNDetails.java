package com.example.anitracker.vnObjects;

import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.mediaObjects.StaffDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class VNDetails extends MediaDetails {
    private List<Developer> developers;
    private List<String> aliases;
    private Screenshots screenshots;
    private int lengthMinutes, lengthVotes;
    private String length;
    private VNRelease release;
    private List<String> languages;
    private List<String> platforms;
    private List<MediaDetails> relations = new ArrayList<>();
    private List<StaffDetails> staffs = new ArrayList<>();
    private Map<String, StaffDetails> knownVAs = new HashMap<>();

    // character role in vn
    private String charRole;

    private List<VNLink> links;

    public List<Developer> getDevelopers() {
        return this.developers;
    }

    public int getLengthMinutes() {
        return this.lengthMinutes;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public String getLength() {
        return this.length;
    }

    public int getLengthVotes() {
        return this.lengthVotes;
    }

    public List<String> getLanguages() {
        return this.languages;
    }

    public List<String> getPlatforms() {
        return this.platforms;
    }

    public void setStaffs(List<StaffDetails> staffs) {
        if (staffs != null && !staffs.isEmpty()) {
            this.staffs = staffs;
        }
    }

    public List<StaffDetails> getStaffs() {
        return this.staffs;
    }

    public void setRelations(List<VNRelation> relations) {
        if (relations != null && !relations.isEmpty()) {
            List<MediaDetails> relationsList = new ArrayList<>();
            for (VNRelation relation : relations) {
                relationsList.add(relation.convertToMediaObject());
            }
            this.relations = relationsList;
        }
    }

    public List<MediaDetails> getRelations() {
        return this.relations;
    }

    public void setDevelopers(List<Developer> developers) {
        if (developers != null && !developers.isEmpty()) {
            this.developers = developers;

            StringBuilder developerNames = new StringBuilder();
            for (Developer developer : this.developers) {
                developerNames.append(developer.name).append("\n\n");
            }
            infoMap.put("Developers", developerNames.toString().trim());
        }
    }

    public void setLengthMinutes(int lengthMinutes, int lengthVotes) {
        if (lengthMinutes > 0) {
            this.lengthMinutes = lengthMinutes;
            int hours = lengthMinutes / 60;
            int minutes = lengthMinutes % 60;
            infoMap.put("Play Time", String.format(Locale.ENGLISH, "%dh %dm\n(from %d votes)",
                    hours, minutes, lengthVotes));
        }
    }

    public void setAliases(List<String> aliases) {
        if (aliases != null && !aliases.isEmpty()) {
            this.aliases = aliases;
            infoMap.put("Aliases", String.join("\n\n", this.aliases));
        }
    }

    public void setLength(String length) {
        this.length = length;
        infoMap.put("Length", this.length);
    }

    public void setLanguages(List<String> languages) {
        if (languages != null && !languages.isEmpty()) {
            this.languages = languages;
            infoMap.put("Languages", String.join("\n\n", this.languages));
        }
    }

    public void setPlatforms(List<String> platforms) {
        if (platforms != null && !platforms.isEmpty()) {
            this.platforms = platforms;
            infoMap.put("Platforms", String.join("\n\n", this.platforms));
        }
    }

    public Screenshots getScreenshots() {
        return this.screenshots;
    }

    public void setScreenshots(Screenshots screenshots) {
        this.screenshots = screenshots;
    }

    public void setKnownVAs(Map<String, StaffDetails> knownVAs) {
        this.knownVAs = knownVAs;
    }

    public Map<String, StaffDetails> getKnownVAs() {
        return this.knownVAs;
    }

    public List<VNLink> getLinks() {
        return links;
    }

    public void setLinks(List<VNLink> links) {
        if (links != null && !links.isEmpty()) {
            this.links = links;
        }
    }

    public VNRelease getRelease() {
        return release;
    }

    public void setRelease(VNRelease release) {
        this.release = release;
    }


    public String getCharRole() {
        return charRole;
    }

    public void setCharRole(String charRole) {
        this.charRole = charRole;
    }
}
