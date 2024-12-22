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
    private List<MediaDetails> relations = new ArrayList<>();
    private List<StaffDetails> staffs = new ArrayList<>();
    private Map<String, StaffDetails> knownVAs = new HashMap<>();

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

    public void setStaffs(List<StaffDetails> staffs) {
        this.staffs = staffs;
    }

    public List<StaffDetails> getStaffs() {
        return this.staffs;
    }

    public void setRelations(List<MediaDetails> relations) {
        this.relations = relations;
    }

    public List<MediaDetails> getRelations() {
        return this.relations;
    }

    public void setDevelopers(List<Developer> developers) {
        this.developers = developers;

        StringBuilder developerNames = new StringBuilder();
        for (Developer developer : this.developers) {
            developerNames.append(developer.name).append("\n\n");
        }
        infoMap.put("Developers", developerNames.toString().trim());
    }

    public void setLengthMinutes(int lengthMinutes, int lengthVotes) {
        this.lengthMinutes = lengthMinutes;
        int hours = lengthMinutes / 60;
        int minutes = lengthMinutes % 60;
        infoMap.put("Play Time", String.format(Locale.ENGLISH, "%dh %dm\n(from %d votes)",
                hours, minutes, lengthVotes));
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
        infoMap.put("Aliases", String.join("\n\n", this.aliases));
    }

    public void setLength(String length) {
        this.length = length;
        infoMap.put("Length", this.length);
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
}
