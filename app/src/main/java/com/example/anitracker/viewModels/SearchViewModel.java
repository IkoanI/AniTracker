package com.example.anitracker.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.anitracker.animeObjects.AnimeDetails;
import com.example.anitracker.mangaObjects.MangaDetails;
import com.example.anitracker.repository.ApiRepository;
import com.example.anitracker.type.MediaSort;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.vnObjects.VNDetails;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

public class SearchViewModel extends ViewModel {
    private final ApiRepository repository;
    private final MutableLiveData<List<AnimeDetails>> animeSearchPage;
    private final MutableLiveData<List<MangaDetails>> mangaSearchPage;
    private final MutableLiveData<List<VNDetails>> vnSearchPage;
    private final MutableLiveData<String> errorMsg;

    private final MutableLiveData<String> userSearch = new MutableLiveData<>();
    private String lastUserSearch;
    private int loadedAnimePages = 1;
    private int loadedMangaPages = 1;
    private int loadedVNPages = 1;

    public SearchViewModel() {
        repository = new ApiRepository();
        animeSearchPage = repository.getMutableAnimeSearch();
        mangaSearchPage = repository.getMutableMangaSearch();
        vnSearchPage = repository.getMutableVNSearch();
        errorMsg = repository.getMutableErrorMsg();
    }

    public LiveData<List<AnimeDetails>> observeAnimeSearchPage(){
        return animeSearchPage;
    }

    public LiveData<List<MangaDetails>> observeMangaSearchPage(){
        return mangaSearchPage;
    }

    public LiveData<List<VNDetails>> observeVNSearchPage(){
        return vnSearchPage;
    }

    public void getSearchPage(MediaType mediaType, int page, String userSearch, List<MediaSort> sort) {
        repository.fetchSearchResults(mediaType, page, userSearch, sort);
        this.setLoadedPages(mediaType, this.getLoadedPages(mediaType) + 1);
    }

    // get results based on user search input
    public void getSearchPage(MediaType mediaType, String userSearch) {
        this.lastUserSearch = userSearch;
        this.getSearchPage(mediaType);
    }

    public void getSearchPage(MediaType mediaType) {
        if (StringUtils.isBlank(this.lastUserSearch)) {
            this.getSearchPage(mediaType, this.getLoadedPages(mediaType), null, Collections.singletonList(MediaSort.SCORE_DESC));
        } else {
            this.getSearchPage(mediaType, this.getLoadedPages(mediaType), this.lastUserSearch, null);
        }
    }

    // get error message from repository
    public LiveData<String> getErrorMsg() { return errorMsg; }

    public void setUserSearch(String userSearch) { this.userSearch.setValue(userSearch); }

    public LiveData<String> observeUserSearch() { return userSearch; }

    public void setLoadedPages(MediaType mediaType, int page) {
        if (mediaType == MediaType.ANIME) {
            this.loadedAnimePages = page;
        } else if (mediaType == MediaType.MANGA) {
            this.loadedMangaPages = page;
        } else if (mediaType == MediaType.VISUAL_NOVEL) {
            this.loadedVNPages = page;
        }
    }

    public int getLoadedPages(MediaType mediaType) {
        if (mediaType == MediaType.ANIME) {
            return this.loadedAnimePages;
        } else if (mediaType == MediaType.MANGA) {
            return this.loadedMangaPages;
        } else if (mediaType == MediaType.VISUAL_NOVEL) {
            return this.loadedVNPages;
        }
        return 0;
    }
}
