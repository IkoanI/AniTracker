package com.example.anitracker.animeObjects;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AiringSchedule {
    private final int airingEp, timeUntilAiring;

    public AiringSchedule(Integer airingEp, Integer timeUntilAiring) {
        this.airingEp = airingEp == null ? 0 : airingEp;
        this.timeUntilAiring = timeUntilAiring == null ? 0 : timeUntilAiring;
    }

    public int getAiringEp() {
        return airingEp;
    }

    public long daysToNextEp(){
        return TimeUnit.SECONDS.toDays(this.timeUntilAiring);
    }

    public String daysHoursMinutesToNextEp(){
        long days = TimeUnit.SECONDS.toDays(this.timeUntilAiring);
        long hours = TimeUnit.SECONDS.toHours(this.timeUntilAiring) - (days * 24);
        long minutes = TimeUnit.SECONDS.toMinutes(this.timeUntilAiring) - (TimeUnit.SECONDS.toHours(this.timeUntilAiring)* 60);
        return String.format(Locale.ENGLISH,"Ep %s: %dd %dh %dm", this.airingEp, days, hours, minutes);
    }
}
