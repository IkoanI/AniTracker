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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.adapters.FilterDialogAdapter;
import com.example.anitracker.repository.AnilistFilters;
import com.example.anitracker.repository.SearchFilter;
import com.example.anitracker.repository.VNDBFilters;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.uiObjects.FilterChipGroup;
import com.example.anitracker.uiObjects.FilterSearchView;
import com.example.anitracker.uiObjects.Header;
import com.example.anitracker.viewModels.SearchViewModel;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
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
        FilterChipGroup tagSearchResults = new FilterChipGroup(new ArrayList<>(), searchFilter.getTags(), false, false);
        FilterSearchView tagSearchView = new FilterSearchView();
        int tagGroupPos = adapter.getItemCount();
        tagSearchView.observeUserSearch().observe(getViewLifecycleOwner(), res -> {
            LinkedHashSet<String> results = new LinkedHashSet<>();
            if (StringUtils.isNotBlank(res)) {
                for (String tag : Objects.requireNonNull(AnilistFilters.tags.getValue())) {
                    if (!tagSearchResults.getSelected().contains(tag) && tag.toLowerCase().contains(res.toLowerCase())) {
                        results.add(tag);
                    }
                }
            }

            tagSearchResults.setChoices(results);
            adapter.notifyItemChanged(tagGroupPos + 1);
        });

        this.uiObjects.add(tagSearchView);
        this.uiObjects.add(tagSearchResults);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    public void addFilterGroup(String filterGroup, List<String> chipNames, LinkedHashSet<String> selected, boolean singleSelction, boolean selectionRequired) {
        this.uiObjects.add(new Header(filterGroup));
        this.uiObjects.add(new FilterChipGroup(chipNames, selected, singleSelction, selectionRequired));
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        viewModel.getSearchPage(this.searchFilter);
    }
}
