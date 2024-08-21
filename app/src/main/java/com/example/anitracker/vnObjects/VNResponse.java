package com.example.anitracker.vnObjects;

import com.example.anitracker.mediaObjects.CharacterDetails;
import com.example.anitracker.mediaObjects.Tag;
import com.example.anitracker.mediaObjects.Titles;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VNResponse {
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("titles")
    private List<VNTitle> titles;
    @SerializedName("image")
    private Image image;
    @SerializedName("screenshots")
    private List<Image> screenshots;
    @SerializedName("released")
    private String released;
    @SerializedName("length")
    private int length;
    @SerializedName("length_votes")
    private int lengthVotes;
    @SerializedName("length_minutes")
    private int length_minutes;
    @SerializedName("rating")
    private float rating;
    @SerializedName("average")
    private float average;
    @SerializedName("developers")
    private List<Developer> developers;
    @SerializedName("description")
    private String desc;
    @SerializedName("devstatus")
    private int devStatus;
    @SerializedName("tags")
    private List<VNTag> tags;
    @SerializedName("aliases")
    private List<String> aliases;
    @SerializedName("role")
    // only used when querying vns a character appears in
    private String role;
    @SerializedName("va")
    private List<VNVoiceActor> voiceActors;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Image getImage() {
        if(image == null){
            return new Image();
        }
        return image;
    }

    public String getLength() {
        switch(this.length){
            case 1:
                return "Very Short (< 2 hours)";

            case 2:
                return "Short (2- 10 hours)";

            case 3:
                return "Medium (10 - 30 hours)";

            case 4:
                return "Long (30 - 50 hours)";

            case 5:
                return "Very Long (> 50 hours)";


        }

        return "Unknown";
    }

    public int getLengthMinutes() {
        return length_minutes;
    }

    public List<Developer> getDevelopers() {
        return developers;
    }

    public int getRating() {
        return Math.round(rating);
    }

    public String getReleased() {
        return released;
    }

    public String getStatus(){
        switch (this.devStatus){
            case 0:
                return "Finished";
            case 1:
                return "In development";
            case 2:
                return "Cancelled";
        }
        return "Unknown";
    }

    public VNDetails convertToMediaObject(){
        VNDetails vnDetails = new VNDetails();

        String natTitle = null, romTitle = null, engTitle = null;
        List<String> synonyms = new ArrayList<>();
        for (VNTitle title : this.titles){
            if (title.isMain()){
                natTitle = title.getTitle();
                romTitle = title.getLatin();
            }
            else if (title.isOfficial()) {
                if(Objects.equals(title.getLang(), "en")){
                    engTitle = title.getTitle();
                }
                else {
                    synonyms.add(title.getTitle());
                }
            }
        }
        vnDetails.setTitles(new Titles(engTitle, natTitle, romTitle, this.title));
        vnDetails.setSynonyms(synonyms);

        if(this.aliases != null){ vnDetails.setAliases(this.aliases); }

        vnDetails.setCoverImg(this.getImage().getThumbnail());

        if(this.desc != null){vnDetails.setDesc(this.desc);}

        vnDetails.setStatus(this.getStatus());

        vnDetails.setVndbID(this.id);

        vnDetails.setAvgScore(this.getRating());

        vnDetails.setMeanScore(Math.round(this.average));

        if(this.tags != null){
            List<Tag> noSpoilerTags = new ArrayList<>();
            List<Tag> allTags = new ArrayList<>();
            for(VNTag vnTag : this.tags){
                int rating = Math.round((vnTag.getRating() / 3) * 100 );
                Tag tag = new Tag(vnTag.getName(), rating, vnTag.isSpoiler());
                if(!tag.getSpoiler()){
                    noSpoilerTags.add(tag);
                }
                allTags.add(tag);
            }
            allTags.sort(this::compareTags);
            noSpoilerTags.sort(this::compareTags);
            vnDetails.setAllTags(allTags);
            vnDetails.setNoSpoilerTags(noSpoilerTags);
        }


        if(this.developers != null){ vnDetails.setDevelopers(this.developers); }

        vnDetails.setLengthMinutes(this.length_minutes);

        vnDetails.setLength(this.getLength());

        vnDetails.setLengthVotes(lengthVotes);

        if(this.released != null){
            vnDetails.setReleaseDate(this.released);
        }

        if(this.screenshots != null){
            List<String> screenshotURLs = new ArrayList<>();
            for(Image screenshot: this.screenshots){
                screenshotURLs.add(screenshot.url);
            }
            vnDetails.setScreenshots(new Screenshots(screenshotURLs));
        }

        return vnDetails;
    }

    public int compareTags(Tag tag, Tag t1){
        if(tag.getTagRanking() < t1.getTagRanking()){
            return 1;
        }
        else if(tag.getTagRanking() > t1.getTagRanking()){
            return -1;
        }
        return 0;
    }

    public List<VNVoiceActor> getVoiceActors() {
        return voiceActors;
    }

    public List<CharacterDetails> getCharacterDetails(){
        List<CharacterDetails> characterDetails = new ArrayList<>();
        for (VNVoiceActor va : voiceActors){
            characterDetails.add(va.convertToMediaObject(this.id));
        }
        return characterDetails;
    }

    public String getRole() {
        return role;
    }
}
