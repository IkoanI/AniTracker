package com.example.anitracker.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.adapters.FilterDialogAdapter;
import com.example.anitracker.repository.AnilistFilters;
import com.example.anitracker.repository.SearchFilter;
import com.example.anitracker.repository.VNDBFilters;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.uiObjects.ChipGroupSearch;
import com.example.anitracker.uiObjects.FilterChip;
import com.example.anitracker.uiObjects.FilterChipGroup;
import com.example.anitracker.uiObjects.Header;
import com.example.anitracker.viewModels.SearchViewModel;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilterDialogFragment extends DialogFragment {
    private final List<Object> uiObjects = new ArrayList<>();
    private SearchViewModel viewModel;
    private final SearchFilter searchFilter;

    public FilterDialogFragment(SearchFilter searchFilter) {
        this.searchFilter = searchFilter;
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
        this.addFilterGroup("Genres", Objects.requireNonNull(AnilistFilters.genres.getValue()),
                searchFilter.getGenres(),false,false);

        this.uiObjects.add(new Header("Tags"));

        int tagsPosition = adapter.getItemCount() + 1;
        FilterChipGroup tagSearchResults = new FilterChipGroup(false, false);
        ChipGroupSearch tagGroup = new ChipGroupSearch();
        tagGroup.observeUserSearch().observe(getViewLifecycleOwner(), res -> {
            List<FilterChip> results = new ArrayList<>();
            if (StringUtils.isNotBlank(res)) {
                for (String tag : Objects.requireNonNull(AnilistFilters.tags.getValue())) {
                    if (tag.toLowerCase().contains(res.toLowerCase())) {
                        results.add(new FilterChip(tag));
                    }
                }
            }

            tagSearchResults.setFilterChips(results);
            adapter.notifyItemChanged(tagsPosition);
        });
        this.uiObjects.add(tagGroup);
        this.uiObjects.add(tagSearchResults);



        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    public void addFilterGroup(String filterGroup, List<String> chipNames, List<String> selected, boolean singleSelction, boolean selectionRequired) {
        this.uiObjects.add(new Header(filterGroup));
        this.uiObjects.add(new FilterChipGroup(chipNames, selected, singleSelction, selectionRequired));
    }
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        for (int i = 0; i < this.uiObjects.size(); i++) {
            Object object = this.uiObjects.get(i);
            if (object instanceof Header) {
                this.updateSearchFilter(this.uiObjects.get(i+1), ((Header) object).getHeader(), searchFilter);
            }
        }
        viewModel.getSearchPage(this.searchFilter);
    }

    public void updateSearchFilter(Object ui, String fiterGroup, SearchFilter searchFilter) {
        if (ui instanceof FilterChipGroup) {
            FilterChipGroup chipGroup = (FilterChipGroup) ui;
            List<String> selected = new ArrayList<>(chipGroup.getSelected());
            switch (fiterGroup) {
                case "Sort":
                    searchFilter.setSort(selected);
                    break;
                case "Order":
                    searchFilter.setOrder(selected);
                    break;
                case "Genres":
                    searchFilter.setGenres(selected);
                    break;
                case "Tags":
                    searchFilter.setTags(selected);
                    break;
            }

        }
    }
}
