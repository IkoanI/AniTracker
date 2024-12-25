package com.example.anitracker.animeObjects;

import com.example.anitracker.fragment.AnimeShortDetail;
import com.example.anitracker.mediaObjects.MediaDetails;

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
        if (season != null) {
            this.season = season;
            infoMap.put("Season", String.format(Locale.ENGLISH,"%s %d",
                    this.season, this.startDate.getYear()));
        }
    }

    public void setDuration(Integer duration) {
        if (duration != null) {
            this.duration = duration;
            infoMap.put("Duration", String.format(Locale.ENGLISH, "%d mins",
                    this.duration));
        }
    }

    public void setEpisodes(Integer episodes, AnimeShortDetail.NextAiringEpisode nextAiringEpisode) {
        this.episodes = episodes == null ? 0 : episodes;
        if (nextAiringEpisode != null) {
            this.setAiringSchedule(new AiringSchedule(nextAiringEpisode.episode, nextAiringEpisode.timeUntilAiring));
            infoMap.put("Episodes", this.airingSchedule.daysHoursMinutesToNextEp());
        } else {
            infoMap.put("Episodes", String.valueOf(this.episodes));
        }
    }

    public void setStudios(Studios studios) {
        this.studios = studios;

        if (!this.studios.getAnimationStudios().isEmpty()) {infoMap.put("Studios", String.join("\n\n", this.studios.getAnimationStudios()));}

        if (!this.studios.getProducers().isEmpty()) {infoMap.put("Producers", String.join("\n\n", this.studios.getProducers()));}

    }

    public void setAiringSchedule(AiringSchedule airingSchedule) {this.airingSchedule = airingSchedule;}
}
