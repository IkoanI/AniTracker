package com.example.anitracker.repository;

import com.example.anitracker.mediaObjects.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class VNDBFilters {
    public static List<String> sort = new LinkedList<>(Arrays.asList("Title", "Release Date", "Average Score", "Number Of Votes"));

    public static List<String> order = Arrays.asList("Ascending", "Descending");

    public static Map<String, String> stringToVNDBFilter = Map.ofEntries(
            Map.entry("Title", "title"),
            Map.entry("Release Date", "released"),
            Map.entry("Average Score", "rating"),
            Map.entry("Number Of Votes", "votecount"),
            Map.entry("Relevance", "searchrank")
    );

    public static List<String> genres = new ArrayList<>();

    public static List<Tag> tags = new ArrayList<>();
}
