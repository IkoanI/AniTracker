package com.example.anitracker.vnObjects;

import com.example.anitracker.mediaObjects.Tag;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VNTag {
    @SerializedName("name")
    protected String name;

    @SerializedName("description")
    protected String desc;

    @SerializedName("id")
    protected String id;

    @SerializedName("rating")
    private float rating;

    @SerializedName("spoiler")
    protected int spoiler;

    @SerializedName("aliases")
    protected List<String> aliases;

    @SerializedName("searchable")
    protected boolean searchable;

    @SerializedName("applicable")
    protected boolean applicable;


    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getId() {
        return id;
    }

    public float getRating() {
        return rating;
    }

    public int getSpoiler() {
        return spoiler;
    }

    public boolean isSpoiler(){
        return spoiler > 0;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public boolean isApplicable() {
        return applicable;
    }

    public Tag convertToMediaTag() {
        int rating = Math.round((this.rating / 3) * 100 );
        return new Tag(this.name, rating, this.isSpoiler());
    }

    public static List<Tag> getNoSpoilerTags(List<? extends VNTag> tags) {
        List<Tag> noSpoilerTags = new ArrayList<>();
        for (VNTag vnTag : tags) {
            if (!vnTag.isSpoiler()) {
                noSpoilerTags.add(vnTag.convertToMediaTag());
            }
        }

        noSpoilerTags.sort(VNTag::compareTags);
        return noSpoilerTags;
    }

    public static List<Tag> getAllTags(List<? extends VNTag> tags) {
        List<Tag> allTags = new ArrayList<>();
        for (VNTag vnTag : tags) {
            allTags.add(vnTag.convertToMediaTag());
        }
        allTags.sort(VNTag::compareTags);
        return allTags;
    }

    private static int compareTags (Tag tag, Tag t1) {
        if (tag.getTagRanking() < t1.getTagRanking()) {
            return 1;
        } else if (tag.getTagRanking() > t1.getTagRanking()) {
            return -1;
        }
        return 0;
    }
}
