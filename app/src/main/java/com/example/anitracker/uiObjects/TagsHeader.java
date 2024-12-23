package com.example.anitracker.uiObjects;

import com.example.anitracker.mediaObjects.CharacterDetails;
import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.mediaObjects.Tag;

import java.util.List;

public class TagsHeader {
    private boolean spoilersShown = false;
    private final List<Tag> noSpoilerTags;
    private final List<Tag> allTags;

    public TagsHeader(MediaDetails details) {
        this.noSpoilerTags = details.getNoSpoilerTags();
        this.allTags = details.getAllTags();
    }

    public TagsHeader(CharacterDetails details) {
        this.noSpoilerTags = details.getNoSpoilerTraits();
        this.allTags = details.getAllTraits();
    }

    public boolean getSpoilersShown() {
        return this.spoilersShown;
    }

    public boolean hasSpoilers() {
        return this.noSpoilerTags.size() != this.allTags.size();
    }

    public void setSpoilersShown(boolean spoilersShown) {
        this.spoilersShown = spoilersShown;
    }

    public List<Tag> getAllTags() {
        return allTags;
    }

    public List<Tag> getNoSpoilerTags() {
        return noSpoilerTags;
    }
}
