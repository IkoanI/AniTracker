package com.example.anitracker.repository;

import java.util.Map;

public class VNDBFilters {
    public static String[] sort = {"Title", "Release Date", "Average Score", "Number Of Votes"};

    public static String[] sortWithSearch = {"Title", "Release Date", "Average Score", "Number Of Votes", "Relevance"};

    public static String[] order = {"Ascending", "Descending"};

    public static Map<String, String> stringToVNDBFilter = Map.ofEntries(
            Map.entry("Title", "title"),
            Map.entry("Release Date", "released"),
            Map.entry("Average Score", "rating"),
            Map.entry("Number Of Votes", "votecount"),
            Map.entry("Relevance", "searchrank")
    );
}
