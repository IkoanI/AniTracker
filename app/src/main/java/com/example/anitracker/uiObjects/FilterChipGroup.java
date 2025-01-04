package com.example.anitracker.uiObjects;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FilterChipGroup {
    private List<FilterChip> filterChips = new ArrayList<>();
    private final boolean singleSelection, selectionRequired;
    private final HashSet<String> selected = new HashSet<>();

    public FilterChipGroup(List<String> chipNames, List<String> selected, boolean singleSelection, boolean selectionRequired) {
        this.singleSelection = singleSelection;
        this.selectionRequired = selectionRequired;
        for (String name : chipNames) {
            filterChips.add(new FilterChip(name));
        }

        this.selected.addAll(selected);
    }

    public FilterChipGroup(boolean singleSelection, boolean selectionRequired) {
        this(new ArrayList<>(), new ArrayList<>(), singleSelection, selectionRequired);
    }

    public List<FilterChip> getFilterChips() {
        return this.filterChips;
    }

    public void setFilterChips(List<FilterChip> filterChips) {
        this.filterChips = filterChips;
    }

    public HashSet<String> getSelected() {
        return selected;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ChipGroup chipGroup;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.chipGroup = itemView.findViewById(R.id.chip_group);
        }

        public void setup(FilterChipGroup filterChipGroup) {
            this.chipGroup.setSelectionRequired(filterChipGroup.selectionRequired);
            this.chipGroup.setSingleSelection(filterChipGroup.singleSelection);
        }
    }
}
