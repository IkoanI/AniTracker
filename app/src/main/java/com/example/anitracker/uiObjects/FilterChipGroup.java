package com.example.anitracker.uiObjects;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.repository.AnilistFilters;
import com.example.anitracker.repository.SearchFilter;
import com.example.anitracker.repository.VNDBFilters;
import com.example.anitracker.type.MediaType;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilterChipGroup {
    private final List<FilterChip> filterChips;
    private final MediaType mediaType;
    private final String filterGroup;
    private boolean singleSelection, selectionRequired;
    private List<Integer> checkedChipIds;
    private final SearchFilter searchFilter;

    public FilterChipGroup(String filterGroup, SearchFilter searchFilter) {
        this.searchFilter = searchFilter;
        this.filterChips = new ArrayList<>();
        this.filterGroup = filterGroup;
        this.mediaType = searchFilter.getMediaType();
        if (Objects.equals(filterGroup, "Sort")) {
            this.checkedChipIds = searchFilter.getSortIDs();
            this.setSortFilter();
        } else if (Objects.equals(filterGroup, "Order")) {
            this.checkedChipIds = searchFilter.getOrderIDs();
            this.setOrderFilter();
        }
    }

    public boolean isSingleSelection() {
        return this.singleSelection;
    }

    public boolean isSelectionRequired() {
        return this.selectionRequired;
    }

    public List<FilterChip> getFilterChips() {
        return this.filterChips;
    }

    public void setSortFilter() {
        this.singleSelection = true;
        this.selectionRequired = true;
        String[] chipNames;
        if (this.mediaType == MediaType.VISUAL_NOVEL) {
            chipNames = this.searchFilter.getUserSearch() == null ? VNDBFilters.sort : VNDBFilters.sortWithSearch;
        } else {
            chipNames = AnilistFilters.sort;
        }
        for (String sort : chipNames) {
            this.filterChips.add(new FilterChip(sort));
        }
    }

    public void setOrderFilter() {
        this.singleSelection = true;
        this.selectionRequired = true;
        String[] chipNames = this.mediaType == MediaType.VISUAL_NOVEL ? VNDBFilters.order : AnilistFilters.order;
        for (String order : chipNames) {
            this.filterChips.add(new FilterChip(order));
        }
    }

    public void setCheckedChipIds(List<Integer> checkedChipIds) {
        this.checkedChipIds = checkedChipIds;
    }

    public List<Integer> getCheckedChipIds() {
        return this.checkedChipIds;
    }

    public String getFilterGroup() {
        return filterGroup;
    }

    public static class ChipGroupView extends RecyclerView.ViewHolder {
        public ChipGroup chipGroup;
        public ChipGroupView(@NonNull View itemView) {
            super(itemView);
            this.chipGroup = itemView.findViewById(R.id.chip_group);
        }
    }
}
