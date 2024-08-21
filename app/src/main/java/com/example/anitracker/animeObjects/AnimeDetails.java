package com.example.anitracker.animeObjects;

import com.example.anitracker.mediaObjects.Info;
import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.type.MediaStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AnimeDetails extends MediaDetails {
    private String season;
    private int duration, episodes;
    private Studios studios;
    private AiringSchedule airingSchedule;


    public AnimeDetails(){}

    public List<Info> getInfo(){
        List<Info> infoList = new ArrayList<>();
        infoList.add(new Info("Format", this.format));

        if(Objects.equals(status, MediaStatus.FINISHED.rawValue)){
            infoList.add(new Info("Episodes", String.valueOf(this.episodes)));
        }
        else if(Objects.equals(status, MediaStatus.RELEASING.rawValue)){
            infoList.add(new Info("Episodes", this.airingSchedule.daysHoursMinutesToNextEp()));
        }

        if(this.duration != 0){
            infoList.add(new Info("Duration", String.format(Locale.ENGLISH, "%d mins", this.duration)));
        }

        infoList.add(new Info("Status", this.status));

        if(this.startDate != null){
            infoList.add(new Info("Start Date", this.startDate.toString()));
        }

        if(this.endDate != null){
            infoList.add(new Info("End Date", this.endDate.toString()));
        }

        if(this.season != null && this.startDate != null){
            infoList.add(new Info("Season", String.format(Locale.ENGLISH,"%s %d", this.season, this.startDate.getYear())));
        }
        else if(this.startDate != null){
            infoList.add(new Info("Season", String.format(Locale.ENGLISH,"%d", this.startDate.getYear())));
        }

        if(this.avgScore != 0){infoList.add(new Info("Average Score", String.format(Locale.ENGLISH,"%d%%", this.avgScore)));}

        if(this.meanScore != 0){infoList.add(new Info("Mean Score", String.format(Locale.ENGLISH,"%d%%", this.meanScore)));}

        if(this.popularity != 0){infoList.add(new Info("Popularity", String.valueOf(this.popularity)));}

        if(this.favorites != 0){infoList.add(new Info("Favorites", String.valueOf(this.favorites)));}


        if(this.studios != null && !this.studios.getAnimationStudios().isEmpty()){
            infoList.add(new Info("Studios", String.join("\n\n", this.studios.getAnimationStudios())));
        }

        if(this.studios != null && !this.studios.getProducers().isEmpty()){
            infoList.add(new Info("Producers", String.join("\n\n", this.studios.getProducers())));
        }

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


    // Getters

    public String getSeason() {
        return season;
    }

    public int getDuration() {
        return duration;
    }

    public int getEpisodes() {
        return episodes;
    }

    public Studios getStudios() {
        return studios;
    }

    public AiringSchedule getAiringSchedule() {
        return airingSchedule;
    }



    // Setters

    public void setSeason(String season) {
        this.season = season;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public void setStudios(Studios studios) {
        this.studios = studios;
    }

    public void setAiringSchedule(AiringSchedule airingSchedule) {

        this.airingSchedule = airingSchedule;
    }
}
