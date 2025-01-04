package com.example.anitracker.repository;

import com.example.anitracker.type.MediaSort;
import com.example.anitracker.type.MediaType;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchFilter {
    private String userSearch;
    private List<String> sort;
    private List<String> order;
    private List<String> genres;
    private List<String> tags;
    private MediaType mediaType;
    private int page;

    public SearchFilter() {
        this.sort = List.of("Average Score");
        this.order = List.of("Descending");
        this.genres = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.page = 1;
    }
    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getUserSearch() {
        return userSearch;
    }

    public void setUserSearch(String userSearch) {
        this.userSearch = StringUtils.isBlank(userSearch) ? null : userSearch;
    }

    public List<MediaSort> getMediaSort() {
        List<MediaSort> mediaSorts = new ArrayList<>();
        for (String sort : this.sort) {
            for (String order : this.order ) {
                mediaSorts.add(AnilistFilters.stringToMediaSort.get(sort + order));
            }
        }

        return mediaSorts;
    }

    public List<String> getSort() {
        return this.sort;
    }

    public void setSort(List<String> sort) {
        if (sort == null || !sort.isEmpty()) {
            this.sort = sort;
        }
    }

    public List<String> getOrder() {
        return this.order;
    }

    public void setOrder(List<String> order) {
        if (order == null || !order.isEmpty()) {
            this.order = order;
        }
    }

    public List<String> getGenres() {
        return this.genres;
    }

    public void setGenres(List<String> genres) {
        if (genres != null && !genres.isEmpty()) {
            this.genres = genres;
        }
    }

    public List<String> getTags() {
        return this.tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
