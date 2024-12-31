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
import com.example.anitracker.repository.SearchFilter;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.uiObjects.FilterChipGroup;
import com.example.anitracker.uiObjects.Header;
import com.example.anitracker.viewModels.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class FilterDialogFragment extends DialogFragment {
    private final MediaType mediaType;
    private final List<Object> uiObjects = new ArrayList<>();
    private SearchViewModel viewModel;
    private final SearchFilter searchFilter;

    public FilterDialogFragment(SearchFilter searchFilter) {
        this.searchFilter = searchFilter;
        this.mediaType = searchFilter.getMediaType();
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

        this.uiObjects.add(new Header("Sort"));
        this.uiObjects.add(new FilterChipGroup("Sort", this.searchFilter));
        this.uiObjects.add(new Header("Order"));
        this.uiObjects.add(new FilterChipGroup("Order", this.searchFilter));

        FilterDialogAdapter adapter = new FilterDialogAdapter(this.mediaType, getContext(), this.uiObjects);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        this.searchFilter.setPage(1);
        for (Object ui : this.uiObjects) {
            this.updateSearchFilter(ui, this.searchFilter);
        }
        viewModel.getSearchPage(this.searchFilter);
    }

    public void updateSearchFilter(Object ui, SearchFilter searchFilter) {
        if (ui instanceof FilterChipGroup) {
            FilterChipGroup chipGroup = (FilterChipGroup) ui;
            switch (chipGroup.getFilterGroup()) {
                case "Sort":
                    searchFilter.setSortIds(chipGroup.getCheckedChipIds());
                    break;
                case "Order":
                    searchFilter.setOrderIDs(chipGroup.getCheckedChipIds());
                    break;
            }

        }
    }
}
