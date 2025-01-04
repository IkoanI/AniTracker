package com.example.anitracker.mediaObjects;

public class Tag {
    private String tagName;
    private int tagRanking;
    private Boolean isSpoiler;
    private int id;

    public Tag(String tagName, int tagRanking, Boolean isSpoiler) {
        this.tagName = tagName;
        this.tagRanking = tagRanking;
        this.isSpoiler = isSpoiler;
    }

    public Tag(String tagName, int id) {
        this.tagName = tagName;
        this.id = id;
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

    public int getId() {return this.id;}
}
