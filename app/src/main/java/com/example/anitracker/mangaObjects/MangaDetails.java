package com.example.anitracker.mangaObjects;

import com.example.anitracker.mediaObjects.MediaDetails;

public class MangaDetails extends MediaDetails {
    private int chapters, volumes;

    public int getChapters() {
        return chapters;
    }

    public int getVolumes() {
        return volumes;
    }

    public void setChapters(int chapters) {
        this.chapters = chapters;
        infoMap.put("Chapters", String.valueOf(this.chapters));
    }

    public void setVolumes(int volumes) {
        this.volumes = volumes;
        infoMap.put("Volumes", String.valueOf(this.volumes));
    }
}
