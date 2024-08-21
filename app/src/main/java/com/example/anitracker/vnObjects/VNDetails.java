package com.example.anitracker.vnObjects;

import com.example.anitracker.mediaObjects.Info;
import com.example.anitracker.mediaObjects.MediaDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VNDetails extends MediaDetails {
    private List<Developer> developers;
    private List<String> aliases;
    private Screenshots screenshots;
    private int lengthMinutes, lengthVotes;
    private String vndbID, length, releaseDate;

    public String getVndbID() {
        return vndbID;
    }

    public void setVndbID(String vndbID) {
        this.vndbID = vndbID;
    }

    public List<Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<Developer> developers) {
        this.developers = developers;
    }

    public int getLengthMinutes() {
        return lengthMinutes;
    }

    public void setLengthMinutes(int lengthMinutes) {
        this.lengthMinutes = lengthMinutes;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getLengthVotes() {
        return lengthVotes;
    }

    public void setLengthVotes(int lengthVotes) {
        this.lengthVotes = lengthVotes;
    }

    public List<Info> getInfo(){
        List<Info> infoList = new ArrayList<>();

        if(this.length != null && !this.length.equals("Unknown")){
            infoList.add(new Info("Length", this.length));
        }

        if(this.lengthMinutes != 0){
            int hours = lengthMinutes / 60;
            int minutes = lengthMinutes % 60;
            infoList.add(new Info("Play Time", String.format(Locale.ENGLISH, "%dh %dm\n(from %d votes)",
                    hours, minutes, this.lengthVotes)));
        }

        infoList.add(new Info("Status", this.status));

        if(this.startDate != null){
            infoList.add(new Info("Release Date", this.releaseDate));
        }


        if(this.avgScore != 0){infoList.add(new Info("Average Score", String.format(Locale.ENGLISH,"%d%%", this.avgScore)));}

        if(this.meanScore != 0){infoList.add(new Info("Mean Score", String.format(Locale.ENGLISH,"%d%%", this.meanScore)));}


        if(this.developers != null && !this.developers.isEmpty()){
            StringBuilder developerNames = new StringBuilder();
            for(Developer developer : this.developers){
                developerNames.append(developer.name).append("\n\n");
            }
            infoList.add(new Info("Developers", developerNames.toString().trim()));
        }

        if(this.titles.getRomTitle() != null){
            infoList.add(new Info("Romaji", this.titles.getRomTitle()));
        }

        if(this.titles.getEngTitle() != null){
            infoList.add(new Info("English", this.titles.getEngTitle()));
        }

        if(this.titles.getNatTitle() != null){
            infoList.add(new Info("Native", this.titles.getNatTitle()));
        }

        if(this.aliases != null && !this.aliases.isEmpty()){
            infoList.add(new Info("Aliases", String.join("\n\n", this.aliases)));
        }

        if(this.synonyms != null && !this.synonyms.isEmpty()){
            infoList.add(new Info("Synonyms", String.join("\n\n", this.synonyms)));
        }

        return infoList;
    }

    public Screenshots getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(Screenshots screenshots) {
        this.screenshots = screenshots;
    }
}
