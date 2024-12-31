package com.example.anitracker.repository;

import com.example.anitracker.type.MediaSort;

import java.util.Map;

public class AnilistFilters {
    public static String[] sort = {"Title", "Popularity", "Average Score", "Trending",
            "Favorites", "Date Added", "Release Date"};

    public static String[] order = {"Ascending", "Descending"};

    public static Map<String, MediaSort> stringToMediaSort = Map.ofEntries(
            Map.entry("TitleDescending", MediaSort.TITLE_ROMAJI_DESC),
            Map.entry("PopularityDescending", MediaSort.POPULARITY_DESC),
            Map.entry("Average ScoreDescending", MediaSort.SCORE_DESC),
            Map.entry("TrendingDescending", MediaSort.TRENDING_DESC),
            Map.entry("FavoritesDescending", MediaSort.FAVOURITES_DESC),
            Map.entry("Date AddedDescending", MediaSort.UPDATED_AT_DESC),
            Map.entry("Release DateDescending", MediaSort.START_DATE_DESC),
            Map.entry("TitleAscending", MediaSort.TITLE_ROMAJI),
            Map.entry("PopularityAscending", MediaSort.POPULARITY),
            Map.entry("Average ScoreAscending", MediaSort.SCORE),
            Map.entry("TrendingAscending", MediaSort.TRENDING),
            Map.entry("FavoritesAscending", MediaSort.FAVOURITES),
            Map.entry("Date AddedAscending", MediaSort.UPDATED_AT),
            Map.entry("Release DateAscending", MediaSort.START_DATE)
    );
}
