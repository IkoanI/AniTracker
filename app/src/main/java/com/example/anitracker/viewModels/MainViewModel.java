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

import java.util.Collections;
import java.util.List;

public class MainViewModel extends ViewModel {
    private final ApiRepository repository;
    private final MutableLiveData<List<AnimeDetails>> liveAnimePage;
    private final MutableLiveData<List<MangaDetails>> liveMangaPage;
    private final MutableLiveData<List<VNDetails>> liveVNPage;
    private final MutableLiveData<String> errorMsg;

    private final MutableLiveData<String> userSearch = new MutableLiveData<>();
    private int loadedAnimePages = 1;
    private int loadedMangaPages = 1;
    private int loadedVNPages = 1;

    public MainViewModel() {
        repository = new ApiRepository();
        liveAnimePage = repository.getMutableAnimePage();
        liveMangaPage = repository.getMutableMangaPage();
        liveVNPage = repository.getMutableVNPage();
        errorMsg = repository.getMutableErrorMsg();
    }

    public LiveData<List<AnimeDetails>> observeAnimePage(){
        return liveAnimePage;
    }

    public LiveData<List<MangaDetails>> observeMangaPage() { return liveMangaPage; }

    public LiveData<List<VNDetails>> observeVNPage() { return liveVNPage; }

    public void getSearchPage(MediaType mediaType, int page, String userSearch, List<MediaSort> sort) {
        repository.fetchSearchResults(mediaType, page, userSearch, sort);
        this.setLoadedPages(mediaType, this.getLoadedPages(mediaType) + 1);
    }

    // get results based on user search input
    public void getSearchPage(MediaType mediaType, String userSearch) {
        this.getSearchPage(mediaType, this.getLoadedPages(mediaType), userSearch, null);
    }

    // no user search input, get top by average score descending
    public void getSearchPage(MediaType mediaType) {
        this.getSearchPage(mediaType, this.getLoadedPages(mediaType), null, Collections.singletonList(MediaSort.SCORE_DESC));
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
