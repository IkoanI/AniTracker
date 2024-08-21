package com.example.anitracker.animeObjects;

import java.util.Objects;

public class Trailer {
    private final String id, site, thumbnail;

    public Trailer(String id, String site, String thumbnail) {
        this.id = id;
        this.site = site;
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTrailerLink() {
        String link = "";
        if(Objects.equals(this.site, "youtube")){
            link = String.format("https://www.youtube.com/watch?v=%s", this.id);
        }
        else if(Objects.equals(this.site, "dailymotion")){
            link = String.format("https://www.dailymotion.com/video/%s", this.id);
        }
        return link;
    }

}
