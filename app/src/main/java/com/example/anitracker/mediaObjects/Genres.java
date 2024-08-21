package com.example.anitracker.mediaObjects;

import java.util.List;

public class Genres {
    private List<String> genreList;

    public Genres(List<String> genreList){
        this.genreList = genreList;
    }

    public List<String> getGenreList() {
        return genreList;
    }

    public void setGenreList(List<String> genreList) {
        this.genreList = genreList;
    }
}
