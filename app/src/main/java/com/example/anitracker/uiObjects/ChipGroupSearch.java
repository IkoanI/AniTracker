package com.example.anitracker.uiObjects;

import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.google.android.material.chip.ChipGroup;

public class ChipGroupSearch {
    private final MutableLiveData<String> userSearch = new MutableLiveData<>();

    public MutableLiveData<String> getUserSearch() {
        return userSearch;
    }

    public LiveData<String> observeUserSearch() {
        return userSearch;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public SearchView searchView;
        public ChipGroup selected;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.searchView = itemView.findViewById(R.id.search_bar);
            this.selected = itemView.findViewById(R.id.selected);
        }
    }
}
