package com.example.anitracker.mediaObjects;

import com.example.anitracker.animeObjects.Trailer;
import com.example.anitracker.fragment.Detail;
import com.example.anitracker.repository.AnilistObjectMappings;
import com.example.anitracker.type.CharacterRole;
import com.example.anitracker.type.MediaRelation;
import com.example.anitracker.type.MediaStatus;
import com.example.anitracker.type.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MediaDetails {
    protected String format, coverImg, desc, status, banner, hashtags, source, id;
    protected int avgScore, meanScore, favorites, popularity;
    protected List<String> synonyms;
    protected List<Tag> allTags, noSpoilerTags;
    protected Titles titles;
    protected Date startDate, endDate;
    protected Map<String, String> infoMap = new HashMap<>();
    private Trailer trailer;
    private MediaType type;
    private Genres genres;

    // only used in relations fragment
    protected String relation;

    // Getters

    public String getFormat() {
        return format;
    }

    public String getImage() {
        return coverImg;
    }

    public String getDesc() {
        return desc;
    }

    public String getStatus() {return this.status;}

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

    public String getId() {
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

    public List<Info> getInfo() {
        List<Info> infoList = new ArrayList<>();
        String[] infoOrder = {"Format", "Duration", "Chapters", "Volumes", "Length", "Play Time", "Status", "Released", "Finished",
                "Season", "Average Score", "Mean Score", "Popularity", "Favorites", "Studios", "Producers", "Developers",
                "Sources", "Hashtag", "Romaji", "English", "Native", "Aliases", "Synonyms", "Languages", "Platforms"};

        for (String infoName: infoOrder) {
            if (infoMap.containsKey(infoName)) {
                infoList.add(new Info(infoName, infoMap.get(infoName)));
            }
        }

        return infoList;
    }

    // Setters

    public void setFormat(String format) {
        this.format = format;
        infoMap.put("Format",  this.format);
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setStatus(String status) {
        this.status = status;
        infoMap.put("Status", this.status);
    }

    public void setStatus(MediaStatus status) {
        this.setStatus(AnilistObjectMappings.mediaStatusToString.get(status.rawValue));
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public void setHashtags(String hashtags) {
        this.hashtags = hashtags;
        infoMap.put("Hashtag", this.hashtags);
    }

    public void setSource(String source) {
        this.source = source;
        infoMap.put("Sources", this.source);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAvgScore(int avgScore) {
        this.avgScore = avgScore;
        infoMap.put("Average Score", String.format(Locale.ENGLISH,"%d%%", this.avgScore));
    }

    public void setMeanScore(int meanScore) {
        this.meanScore = meanScore;
        infoMap.put("Mean Score", String.format(Locale.ENGLISH,"%d%%", this.meanScore));
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
        infoMap.put("Favorites", String.valueOf(this.favorites));
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
        infoMap.put("Popularity", String.valueOf(this.popularity));
    }

    public void setGenres(List<String> genres) {
        this.genres = new Genres(genres);
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
        infoMap.put("Synonyms", String.join("\n\n", this.synonyms));
    }

    public void setTags(List<Detail.Tag> tags){
        List<Tag> newAllTags = new ArrayList<>();
        List<Tag> newNoSpoilerTags = new ArrayList<>();
        for(Detail.Tag tag : tags){
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

        if (this.titles.getRomTitle() != null) {infoMap.put("Romaji", this.titles.getRomTitle());}

        if (this.titles.getEngTitle() != null) {infoMap.put("English", this.titles.getEngTitle());}

        if (this.titles.getNatTitle() != null) {infoMap.put("Native", this.titles.getNatTitle());}
    }

    public void setStartDate(Date date){
        this.startDate = date;
        infoMap.put("Released", this.startDate.toString());
    }

    public void setEndDate(Date date) {
        this.endDate = date;
        infoMap.put("Finished", this.endDate.toString());
    }

    public void setRelation(MediaRelation relation) {
        this.setRelation(AnilistObjectMappings.mediaRelationsToString.get(relation));
    }

    public void setRelation(CharacterRole relation) {
        this.setRelation(AnilistObjectMappings.characterRoleToString.get(relation));
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public void setTrailer(Detail.Trailer trailer) {
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
