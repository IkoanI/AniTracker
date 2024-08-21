package com.example.anitracker.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.anitracker.animeObjects.AnimeDetails;
import com.example.anitracker.mangaObjects.MangaDetails;
import com.example.anitracker.repository.ApiRepository;
import com.example.anitracker.vnObjects.VNDetails;
import com.example.anitracker.vnObjects.VNPage;

import java.util.List;

public class MainViewModel extends ViewModel {
    private final ApiRepository repository;
    private final MutableLiveData<List<AnimeDetails>> liveAnimePage;
    private final MutableLiveData<List<MangaDetails>> liveMangaPage;
    private final MutableLiveData<VNPage> liveVNPage;
    private final MutableLiveData<String> errorMsg;

    private final MutableLiveData<String> userSearch = new MutableLiveData<>();
    private int loadedAnimePages = 1;
    private int loadedMangaPages = 1;
    private int loadedVNPages = 1;

    public MainViewModel(){
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

    public LiveData<VNPage> observeVNPage() { return liveVNPage; }

    public void getAnimePage(){
        repository.fetchTopAnimePage(this.loadedAnimePages);
        this.loadedAnimePages++;
    }

    public void getAnimePage(String userSearch){
        repository.fetchUserAnimeSearch(this.loadedAnimePages, userSearch);
        this.loadedAnimePages++;
    }

    public void getMangaPage(){
        repository.fetchTopMangaPage(this.loadedMangaPages);
        this.loadedMangaPages++;
    }

    public void getMangaPage(String userSearch){
        repository.fetchUserMangaSearch(this.loadedMangaPages, userSearch);
        loadedMangaPages++;
    }

    public void getVNPage(String sort, String fields){
        repository.fetchDefaultVNPage(sort, fields, this.loadedVNPages);
        this.loadedVNPages++;
    }

    public void getVNPage(String search, String sort, String fields){
        repository.fetchVNSearchPage(search, sort, fields, this.loadedVNPages);
        this.loadedVNPages++;
    }

    public LiveData<String> getErrorMsg() { return errorMsg; }

    public void setUserSearch(String userSearch) { this.userSearch.setValue(userSearch); }

    public LiveData<String> observeUserSearch() { return userSearch; }

    public void setLoadedAnimePages(int page){
        this.loadedAnimePages = page;
    }

    public void setLoadedMangaPages(int page){
        this.loadedMangaPages = page;
    }

    public void setLoadedVNPages(int page){
        this.loadedVNPages = page;
    }


}
