package com.example.anitracker.repository;

import com.example.anitracker.type.MediaSort;
import com.example.anitracker.type.MediaType;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class SearchFilter {
    private String userSearch;
    private final LinkedHashSet<String> sort, order, genres;
    private final LinkedHashSet<?> tags;
    private MediaType mediaType;
    private int page;

    public SearchFilter() {
        this.sort = new LinkedHashSet<>(List.of("Average Score"));
        this.order = new LinkedHashSet<>(List.of("Descending"));
        this.genres = new LinkedHashSet<>();
        this.tags = new LinkedHashSet<>();
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
        if (StringUtils.isBlank(userSearch) && mediaType == MediaType.VISUAL_NOVEL) {
            this.sort.remove("Relevance");
            this.sort.add("Average Score");
        }
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

    public LinkedHashSet<String> getSort() {
        return this.sort;
    }

    public LinkedHashSet<String> getOrder() {
        return this.order;
    }

    public LinkedHashSet<String> getGenres() {
        return this.genres;
    }

    public LinkedHashSet<?> getTags() {
        return this.tags;
    }
}
