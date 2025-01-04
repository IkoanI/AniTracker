package com.example.anitracker.uiObjects;

import android.widget.SearchView;
import android.os.Handler;

import androidx.lifecycle.MutableLiveData;

public class SearchQueryListener implements SearchView.OnQueryTextListener {
    private final SearchView searchView;
    private final Handler handler;
    private final MutableLiveData<String> liveUserSearch;

    public SearchQueryListener(SearchView searchView, MutableLiveData<String> liveUserSearch) {
        this.searchView = searchView;
        this.handler = new Handler();
        this.liveUserSearch = liveUserSearch;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // clear focus from search bar after submitting
        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // delay search until user stops typing
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(() -> liveUserSearch.setValue(newText), 500);
        return true;
    }
}
