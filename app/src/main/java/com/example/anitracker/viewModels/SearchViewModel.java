package com.example.anitracker.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.anitracker.animeObjects.AnimeDetails;
import com.example.anitracker.mangaObjects.MangaDetails;
import com.example.anitracker.repository.ApiRepository;
import com.example.anitracker.repository.SearchFilter;
import com.example.anitracker.vnObjects.VNDetails;

import java.util.List;

public class SearchViewModel extends ViewModel {
    private final ApiRepository repository;
    private final MutableLiveData<List<AnimeDetails>> animeSearchPage;
    private final MutableLiveData<List<MangaDetails>> mangaSearchPage;
    private final MutableLiveData<List<VNDetails>> vnSearchPage;
    private final MutableLiveData<String> errorMsg;

    private final MutableLiveData<String> userSearch = new MutableLiveData<>();

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

    public void getSearchPage(SearchFilter searchFilter) {
        repository.fetchSearchResults(searchFilter);
    }

    public void getMediaAttributes() {
        repository.fetchMediaAttributes();
    }


    // get error message from repository
    public LiveData<String> getErrorMsg() { return errorMsg; }

    public MutableLiveData<String> getUserSearch() {
        return userSearch;
    }

    public LiveData<String> observeUserSearch() { return userSearch; }
}
