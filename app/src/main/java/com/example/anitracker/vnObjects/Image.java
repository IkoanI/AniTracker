package com.example.anitracker.vnObjects;

import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("id")
    String id;
    @SerializedName("url")
    String url;
    @SerializedName("dims")
    int[] dims;
    @SerializedName("sexual")
    float sexual;
    @SerializedName("violence")
    float violence;
    @SerializedName("votecount")
    int votecount;
    @SerializedName("thumbnail")
    String thumbnail;
    @SerializedName("thumbnail_dims")
    int[] thumbnail_dims;


    public String getId() {
        return id;
    }

    public String getUrl() {
        if(url == null){
            return "https://s4.anilist.co/file/anilistcdn/staff/large/default.jpg";
        }
        return url;
    }

    public int[] getDims() {
        // Pixel dimensions of the image, array with two integer elements indicating the width and height.
        return dims;
    }

    public int getVotecount() {
        // Integer, number of image flagging votes.
        return votecount;
    }

    public float getSexual() {
        // Number between 0 and 2 (inclusive), average image flagging vote for sexual conte
        return sexual;
    }

    public float getViolence() {
        // Number between 0 and 2 (inclusive), average image flagging vote for violence.
        return violence;
    }

    public int[] getThumbnail_dims() {
        // Pixel dimensions of the thumbnail, array with two integer elements.
        return thumbnail_dims;
    }

    public String getThumbnail() {
        // String, URL to the thumbnail.
        if(thumbnail == null){
            return "https://s4.anilist.co/file/anilistcdn/staff/large/default.jpg";
        }
        return thumbnail;
    }
}
