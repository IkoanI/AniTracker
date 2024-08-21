package com.example.anitracker.mediaObjects;

import com.example.anitracker.animeObjects.Trailer;
import com.example.anitracker.fragment.CommonDetails;
import com.example.anitracker.type.MediaType;

import java.util.ArrayList;
import java.util.List;

public class MediaDetails {
    protected String format, coverImg, desc, status, banner, hashtags, source;
    protected int id, avgScore, meanScore, favorites, popularity;
    protected List<String> synonyms;
    protected List<Tag> allTags, noSpoilerTags;
    protected Titles titles;
    protected Date startDate, endDate;
    private Trailer trailer;
    private MediaType type;
    private Genres genres;

    // only used in relations fragment
    protected String relation;

    public MediaDetails(){}

    // Getters

    public String getFormat() {
        return format;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public String getDesc() {
        return desc;
    }

    public String getStatus() {
        return status;
    }

    public String getBanner() {
        return banner;
    }

    public String getHashtags() {
        return hashtags;
    }

    public String getSource() {
        return source;
    }

    public String getRelation() {
        return relation;
    }



    public int getId() {
        return id;
    }

    public int getAvgScore() {
        return avgScore;
    }

    public int getMeanScore() {
        return meanScore;
    }

    public int getFavorites() {
        return favorites;
    }

    public int getPopularity() {
        return popularity;
    }



    public Genres getGenres() {
        return genres;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public List<Tag> getAllTags() { return allTags; }

    public List<Tag> getNoSpoilerTags() { return noSpoilerTags; }

    public Titles getTitles() {
        return titles;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Trailer getTrailer() {
        return trailer;
    }

    public List<Info> getInfo() {return new ArrayList<>();}


    // Setters

    public void setFormat(String format) {
        this.format = format;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public void setHashtags(String hashtags) {
        this.hashtags = hashtags;
    }

    public void setSource(String source) {
        this.source = source;
    }



    public void setId(int id) {
        this.id = id;
    }

    public void setAvgScore(int avgScore) {
        this.avgScore = avgScore;
    }

    public void setMeanScore(int meanScore) {
        this.meanScore = meanScore;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }



    public void setGenres(List<String> genres) {
        this.genres = new Genres(genres);
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public void setTags(List<CommonDetails.Tag> tags){
        List<Tag> newAllTags = new ArrayList<>();
        List<Tag> newNoSpoilerTags = new ArrayList<>();
        for(CommonDetails.Tag tag : tags){
            Tag toAdd = new Tag(tag.name, tag.rank, tag.isGeneralSpoiler || tag.isMediaSpoiler);
            if(!toAdd.getSpoiler()){
                newNoSpoilerTags.add(toAdd);
            }
            newAllTags.add(toAdd);
        }
        this.allTags = newAllTags;
        this.noSpoilerTags = newNoSpoilerTags;
    }

    public void setTitles(Titles titles){
        this.titles = titles;
    }

    public void setStartDate(Date date){
        this.startDate = date;
    }

    public void setEndDate(Date date) {
        this.endDate = date;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public void setTrailer(CommonDetails.Trailer trailer) {
        this.trailer = new Trailer(trailer.id, trailer.site, trailer.thumbnail);
    }

    public String getType() {
        return type.rawValue;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public void setAllTags(List<Tag> allTags) {
        this.allTags = allTags;
    }

    public void setNoSpoilerTags(List<Tag> noSpoilerTags) {
        this.noSpoilerTags = noSpoilerTags;
    }
}
