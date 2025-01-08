package com.example.anitracker.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.adapters.FilterDialogAdapter;
import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.repository.AnilistFilters;
import com.example.anitracker.repository.SearchFilter;
import com.example.anitracker.repository.VNDBFilters;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.uiObjects.FilterChipGroup;
import com.example.anitracker.uiObjects.FilterSearchView;
import com.example.anitracker.uiObjects.Header;
import com.example.anitracker.viewModels.SearchViewModel;
import com.example.anitracker.vnObjects.VNTag;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public class FilterDialogFragment extends DialogFragment {
    private final List<Object> uiObjects = new ArrayList<>();
    private SearchViewModel viewModel;
    private final SearchFilter searchFilter;
    private final MutableLiveData<List<? extends MediaDetails>> searchResults;

    public FilterDialogFragment(SearchFilter searchFilter, MutableLiveData<List<? extends MediaDetails>> searchResults) {
        this.searchFilter = searchFilter;
        this.searchResults = searchResults;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Black);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);

        View view = inflater.inflate(R.layout.filter_dialog_layout, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recView);
        Toolbar toolbar = view.findViewById(R.id.filterToolbar);
        toolbar.setNavigationOnClickListener(e -> dismiss());
        FilterDialogAdapter adapter = new FilterDialogAdapter(getContext(), this.uiObjects);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        VNDBFilters.sort.remove("Relevance");
        if (StringUtils.isNotBlank(searchFilter.getUserSearch())) {
            VNDBFilters.sort.add("Relevance");
        }

        this.addFilterGroup("Sort", searchFilter.getMediaType() == MediaType.VISUAL_NOVEL ? VNDBFilters.sort : AnilistFilters.sort,
                searchFilter.getSort(), true, true);
        this.addFilterGroup("Order", searchFilter.getMediaType() == MediaType.VISUAL_NOVEL ? VNDBFilters.order : AnilistFilters.order,
                searchFilter.getOrder(), true, true);

        if (searchFilter.getMediaType() != MediaType.VISUAL_NOVEL) {
            this.addFilterGroup("Genres", Objects.requireNonNull(AnilistFilters.genres.getValue()),
                    searchFilter.getGenres(),false,false);
        }

        this.uiObjects.add(new Header("Tags"));
        FilterChipGroup tagSearchResults = new FilterChipGroup(new LinkedHashSet<>(), searchFilter.getTags(), false, false);
        FilterSearchView tagSearchView = new FilterSearchView();
        int tagGroupPos = adapter.getItemCount();
        tagSearchView.observeUserSearch().observe(getViewLifecycleOwner(), res -> {
            if (StringUtils.isNotBlank(res)) {
                if (searchFilter.getMediaType() != MediaType.VISUAL_NOVEL) {
                    tagSearchResults.setChoices(this.getAnilistTags(res, tagSearchResults.getSelected()));
                    adapter.notifyItemChanged(tagGroupPos + 1);
                } else {
                    viewModel.getVNTags(res);
                }
            } else {
                tagSearchResults.clearChoices();
                adapter.notifyItemChanged(tagGroupPos + 1);
            }
        });

        if (searchFilter.getMediaType() == MediaType.VISUAL_NOVEL) {
            VNDBFilters.tags.observe(getViewLifecycleOwner(), res -> {
                tagSearchResults.setChoices(this.getVNDBTags(tagSearchResults.getSelected()));
                adapter.notifyItemChanged(tagGroupPos + 1);
            });
        }


        this.uiObjects.add(tagSearchView);
        this.uiObjects.add(tagSearchResults);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    public void addFilterGroup(String filterGroup, List<String> chipNames, LinkedHashSet<String> selected, boolean singleSelction, boolean selectionRequired) {
        this.uiObjects.add(new Header(filterGroup));
        this.uiObjects.add(new FilterChipGroup(new LinkedHashSet<>(chipNames), selected, singleSelction, selectionRequired));
    }

    public <T> LinkedHashSet<String> getAnilistTags(String userSearch, LinkedHashSet<T> selected) {
        LinkedHashSet<String> results = new LinkedHashSet<>();
        @SuppressWarnings("unchecked")
        LinkedHashSet<String> stringSelected = (LinkedHashSet<String>) selected;
        for (String tag : Objects.requireNonNull(AnilistFilters.tags.getValue())) {
            if (!stringSelected.contains(tag) && tag.toLowerCase().contains(userSearch.toLowerCase())) {
                results.add(tag);
            }
        }

        return results;
    }

    public <T> LinkedHashSet<VNTag> getVNDBTags(LinkedHashSet<T> selected) {
        LinkedHashSet<VNTag> results = new LinkedHashSet<>();
        @SuppressWarnings("unchecked")
        LinkedHashSet<VNTag> vnSelected = (LinkedHashSet<VNTag>) selected;
        for (VNTag tag : Objects.requireNonNull(VNDBFilters.tags.getValue())) {
            if (!vnSelected.contains(tag)) {
                results.add(tag);
            }
        }

        return results;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        viewModel.getSearchPage(this.searchFilter, searchResults);
    }
}
