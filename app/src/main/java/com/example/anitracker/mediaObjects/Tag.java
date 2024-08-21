package com.example.anitracker.mediaObjects;

public class Tag {
    private final String tagName;
    private final int tagRanking;
    private final Boolean isSpoiler;

    public Tag(String tagName, int tagRanking, Boolean isSpoiler) {
        this.tagName = tagName;
        this.tagRanking = tagRanking;
        this.isSpoiler = isSpoiler;
    }

    public String getTagName() {
        return tagName;
    }

    public int getTagRanking(){
        return tagRanking;
    }

    public Boolean getSpoiler() {
        return isSpoiler;
    }
}
