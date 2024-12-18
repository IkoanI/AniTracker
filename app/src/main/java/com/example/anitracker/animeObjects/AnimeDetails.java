package com.example.anitracker.animeObjects;

import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.type.MediaStatus;

import java.util.Locale;

public class AnimeDetails extends MediaDetails {
    private String season;
    private int duration, episodes;
    private Studios studios;
    private AiringSchedule airingSchedule;

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

        // set start date first before setting season
        if (this.startDate != null) {infoMap.put("Season", String.format(Locale.ENGLISH,"%s %d", this.season, this.startDate.getYear()));}
    }

    public void setDuration(int duration) {
        this.duration = duration;
        infoMap.put("Duration", String.format(Locale.ENGLISH, "%d mins", this.duration));
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;

        if (this.status.equals(MediaStatus.FINISHED.rawValue)) {
            infoMap.put("Episodes", String.valueOf(this.episodes));
        } else if (this.airingSchedule != null && this.status.equals(MediaStatus.RELEASING.rawValue)) {
            infoMap.put("Episodes", this.airingSchedule.daysHoursMinutesToNextEp());
        }
    }

    public void setStudios(Studios studios) {
        this.studios = studios;

        if (!this.studios.getAnimationStudios().isEmpty()) {infoMap.put("Studios", String.join("\n\n", this.studios.getAnimationStudios()));}

        if (!this.studios.getProducers().isEmpty()) {infoMap.put("Producers", String.join("\n\n", this.studios.getProducers()));}

    }

    public void setAiringSchedule(AiringSchedule airingSchedule) {this.airingSchedule = airingSchedule;}
}
