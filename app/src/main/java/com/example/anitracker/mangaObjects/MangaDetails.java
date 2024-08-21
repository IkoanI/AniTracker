package com.example.anitracker.mangaObjects;

import com.example.anitracker.mediaObjects.Info;
import com.example.anitracker.mediaObjects.MediaDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MangaDetails extends MediaDetails {
    private int chapters, volumes;

    public MangaDetails(){}

    public int getChapters() {
        return chapters;
    }

    public void setChapters(int chapters) {
        this.chapters = chapters;
    }

    public int getVolumes() {
        return volumes;
    }

    public void setVolumes(int volumes) {
        this.volumes = volumes;
    }

    public List<Info> getInfo(){
        List<Info> infoList = new ArrayList<>();
        infoList.add(new Info("Format", this.format));

        if(this.chapters != 0){
            infoList.add(new Info("Chapters", String.valueOf(this.chapters)));
        }

        if(this.volumes != 0){
            infoList.add(new Info("Volumes", String.valueOf(this.volumes)));
        }

        infoList.add(new Info("Status", this.status));
        infoList.add(new Info("Start Date", this.startDate.toString()));

        if(this.endDate != null){
            infoList.add(new Info("End Date", this.endDate.toString()));
        }

        if(this.avgScore != 0){infoList.add(new Info("Average Score", String.format(Locale.ENGLISH,"%d%%", this.avgScore)));}

        if(this.meanScore != 0){infoList.add(new Info("Mean Score", String.format(Locale.ENGLISH,"%d%%", this.meanScore)));}

        if(this.popularity != 0){infoList.add(new Info("Popularity", String.valueOf(this.popularity)));}

        if(this.favorites != 0){infoList.add(new Info("Favorites", String.valueOf(this.favorites)));}

        infoList.add(new Info("Sources", this.source));

        if(this.hashtags != null){
            infoList.add(new Info("Hashtag", this.hashtags));
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

        if(this.synonyms != null && !this.synonyms.isEmpty()){
            infoList.add(new Info("Synonyms", String.join("\n\n", this.synonyms)));
        }

        return infoList;
    }
}
