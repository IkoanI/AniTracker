package com.example.anitracker.uiObjects;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.google.android.material.chip.ChipGroup;

import java.util.LinkedHashSet;
import java.util.List;

public class FilterChipGroup {
    private final boolean singleSelection, selectionRequired;
    private final LinkedHashSet<String> selected;
    private LinkedHashSet<String> choices;

    public FilterChipGroup(List<String> chipNames, LinkedHashSet<String> selected, boolean singleSelection, boolean selectionRequired) {
        this.singleSelection = singleSelection;
        this.selectionRequired = selectionRequired;
        this.choices = new LinkedHashSet<>(chipNames);
        this.selected = selected;
    }

    public LinkedHashSet<String> getChoices() {
        return choices;
    }

    public void setChoices(LinkedHashSet<String> choices) {
        this.choices = choices;
    }

    public LinkedHashSet<String> getSelected() {
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
