package com.example.anitracker.vnObjects;

import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.mediaObjects.StaffDetails;
import com.example.anitracker.type.Media;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VNDetails extends MediaDetails {
    private List<Developer> developers;
    private List<String> aliases;
    private Screenshots screenshots;
    private int lengthMinutes, lengthVotes;
    private String length;
    private List<MediaDetails> relations;
    private List<StaffDetails> staffs;

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

    public void setLengthMinutes(int lengthMinutes) {
        this.lengthMinutes = lengthMinutes;
        int hours = lengthMinutes / 60;
        int minutes = lengthMinutes % 60;
        infoMap.put("Play Time", String.format(Locale.ENGLISH, "%dh %dm\n(from %d votes)",
                hours, minutes, this.lengthVotes));
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
        infoMap.put("Aliases", String.join("\n\n", this.aliases));
    }

    public void setLength(String length) {
        this.length = length;
        infoMap.put("Length", this.length);
    }

    public void setLengthVotes(int lengthVotes) {
        this.lengthVotes = lengthVotes;
    }

    public Screenshots getScreenshots() {
        return this.screenshots;
    }

    public void setScreenshots(Screenshots screenshots) {
        this.screenshots = screenshots;
    }
}
