package com.example.anitracker.mangaObjects;

import com.example.anitracker.mediaObjects.MediaDetails;

public class MangaDetails extends MediaDetails {
    private int chapters, volumes;

    public int getChapters() {
        return this.chapters;
    }

    public int getVolumes() {
        return this.volumes;
    }

    public void setChapters(Integer chapters) {
        if (chapters != null) {
            this.chapters = chapters;
            infoMap.put("Chapters", String.valueOf(this.chapters));
        }
    }

    public void setVolumes(Integer volumes) {
        if (volumes != null) {
            this.volumes = volumes;
            infoMap.put("Volumes", String.valueOf(this.volumes));
        }
    }
}
