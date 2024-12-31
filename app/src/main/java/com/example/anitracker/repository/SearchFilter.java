package com.example.anitracker.repository;

import com.example.anitracker.type.MediaSort;
import com.example.anitracker.type.MediaType;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchFilter {
    private String userSearch;
    private List<Integer> sortIDs, orderIDs;
    private MediaType mediaType;
    private int page;

    public SearchFilter() {
        this.sortIDs = List.of(2);
        this.orderIDs = List.of(1);
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
        for (int sortID : this.sortIDs) {
            for (int orderID : this.orderIDs ) {
                mediaSorts.add(AnilistFilters.stringToMediaSort.get(AnilistFilters.sort[sortID] + AnilistFilters.order[orderID]));
            }
        }

        return mediaSorts;
    }

    public List<Integer> getSortIDs() {
        return this.sortIDs;
    }

    public void setSortIds(List<Integer> sortIDs) {
        if (sortIDs == null || !sortIDs.isEmpty()) {
            this.sortIDs = sortIDs;
        }
    }

    public List<Integer> getOrderIDs() {
        return orderIDs;
    }

    public void setOrderIDs(List<Integer> orderIDs) {
        if (orderIDs == null || !orderIDs.isEmpty()) {
            this.orderIDs = orderIDs;
        }
    }
}
