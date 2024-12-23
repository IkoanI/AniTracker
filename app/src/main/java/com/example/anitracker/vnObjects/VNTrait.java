package com.example.anitracker.vnObjects;

import com.example.anitracker.mediaObjects.Tag;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VNTrait  {
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String desc;
    @SerializedName("id")
    private String id;
    @SerializedName("spoiler")
    private int spoiler;
    @SerializedName("aliases")
    private List<String> aliases;
    @SerializedName("searchable")
    private boolean searchable;
    @SerializedName("applicable")
    private boolean applicable;
    @SerializedName("group_id")
    private String groupId;
    @SerializedName("group_name")
    private String groupName;
    @SerializedName("char_count")
    private int charCount;


    public String getGroupName() {
        return groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public int getCharCount() {
        return charCount;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getId() {
        return id;
    }

    public int getSpoiler() {
        return spoiler;
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
        return new Tag(String.format("%s: %s", this.groupName, this.name), 0, this.spoiler > 0);
    }
}