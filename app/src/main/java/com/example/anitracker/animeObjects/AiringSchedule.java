package com.example.anitracker.animeObjects;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AiringSchedule {
    private final int airingEp, timeUntilAiring;

    public AiringSchedule(int airingEp, int timeUntilAiring) {
        this.airingEp = airingEp;
        this.timeUntilAiring = timeUntilAiring;
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
