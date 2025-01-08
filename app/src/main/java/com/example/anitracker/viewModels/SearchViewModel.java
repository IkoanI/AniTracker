package com.example.anitracker.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.repository.ApiRepository;
import com.example.anitracker.repository.SearchFilter;

import java.util.List;

public class SearchViewModel extends ViewModel {
    private final ApiRepository repository;
    private final MutableLiveData<String> errorMsg;

    private final MutableLiveData<String> userSearch = new MutableLiveData<>();

    public SearchViewModel() {
        repository = new ApiRepository();
        errorMsg = repository.getMutableErrorMsg();
    }

    public void getSearchPage(SearchFilter searchFilter, MutableLiveData<List<? extends MediaDetails>> searchResults) {
        repository.fetchSearchResults(searchFilter, searchResults);
    }

    public void getMediaAttributes() {
        repository.fetchMediaAttributes();
    }

    public void getVNTags(String userSearch) {
        repository.fetchVNTags(userSearch);
    }


    // get error message from repository
    public LiveData<String> getErrorMsg() { return errorMsg; }

    public MutableLiveData<String> getUserSearch() {
        return userSearch;
    }

    public LiveData<String> observeUserSearch() { return userSearch; }
}
