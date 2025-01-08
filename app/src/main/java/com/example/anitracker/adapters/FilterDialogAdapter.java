package com.example.anitracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.uiObjects.FilterChipGroup;
import com.example.anitracker.uiObjects.FilterSearchView;
import com.example.anitracker.uiObjects.Header;
import com.example.anitracker.uiObjects.SearchQueryListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.LinkedHashSet;
import java.util.List;

public class FilterDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Object> objectList;
    private final Context context;
    private LayoutInflater inflater;

    private final int headerVar = 0,
            searchViewVar = 1,
            filterChipGroupVar = 2;


    public FilterDialogAdapter(Context context, List<Object> uiObjects) {
        this.context = context;
        this.objectList = uiObjects;
    }

    @Override
    public int getItemViewType(int position) {
        Object object = objectList.get(position);
        if (object instanceof Header) {
            return headerVar;
        } else if (object instanceof FilterSearchView) {
            return searchViewVar;
        } else if (object instanceof FilterChipGroup) {
            return filterChipGroupVar;
        } else {
            return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.inflater = LayoutInflater.from(context);
        View view;

        switch (viewType) {
            case headerVar:
                view = inflater.inflate(R.layout.header_layout, parent, false );
                return new Header.HeaderView(view);

            case searchViewVar:
                view = inflater.inflate(R.layout.search_view_layout, parent, false);
                return new FilterSearchView.ViewHolder(view);

            case filterChipGroupVar:
                view = inflater.inflate(R.layout.chip_group_layout, parent, false);
                return new FilterChipGroup.ViewHolder(view);

            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object object = objectList.get(position);
        switch (holder.getItemViewType()) {
            case headerVar:
                Header.HeaderView headerView = (Header.HeaderView) holder;
                Header header = (Header) object;
                headerView.headerText.setText(header.getHeader());
                break;

            case searchViewVar:
                FilterSearchView.ViewHolder searchChipGroupView = (FilterSearchView.ViewHolder) holder;
                FilterSearchView searchableChipGroup = (FilterSearchView) object;
                SearchView searchView = searchChipGroupView.searchView;
                searchView.setOnQueryTextListener(new SearchQueryListener(searchView, searchableChipGroup.getUserSearch()));
                break;

            case filterChipGroupVar:
                FilterChipGroup.ViewHolder filterChipGroupView = (FilterChipGroup.ViewHolder) holder;
                FilterChipGroup filterChipGroup = (FilterChipGroup) object;
                filterChipGroupView.setup(filterChipGroup);

                filterChipGroupView.chipGroup.removeAllViews();
                this.addFilterChips(filterChipGroupView.chipGroup, filterChipGroup);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + holder.getItemViewType());
        }
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    private void addFilterChips(ChipGroup chipGroup, FilterChipGroup filterChipGroup) {
        this.insertSelected(filterChipGroup.getSelected(), chipGroup, filterChipGroup);
        this.insertChoices(filterChipGroup.getChoices(), chipGroup, filterChipGroup);
    }

    private <T> void insertSelected (LinkedHashSet<T> selected, ChipGroup chipGroup, FilterChipGroup filterChipGroup) {
        for (T choice : selected) {
            if (!filterChipGroup.getChoices().contains(choice)) {
                this.chipSetup(choice, true, chipGroup, filterChipGroup);
            }
        }
    }

    private <T> void insertChoices(LinkedHashSet<T> choices, ChipGroup chipGroup, FilterChipGroup filterChipGroup) {
        for (T choice : choices) {
            this.chipSetup(choice, filterChipGroup.getSelected().contains(choice),chipGroup, filterChipGroup);
        }
    }

    private <T> void chipSetup(T object, boolean checked, ChipGroup chipGroup, FilterChipGroup filterChipGroup) {
        Chip chip = (Chip) inflater.inflate(R.layout.filter_chip_layout, chipGroup, false);
        chip.setText(object.toString());
        chip.setOnCheckedChangeListener((e, isChecked) -> {
            @SuppressWarnings("unchecked")
            LinkedHashSet<T> selected = (LinkedHashSet<T>) filterChipGroup.getSelected();
            if (isChecked) {
                selected.add(object);
            } else {
                selected.remove(object);
                if (!filterChipGroup.getChoices().contains(object)) {
                    chipGroup.removeView(chip);
                }
            }
        });
        chip.setChecked(checked);
        chipGroup.addView(chip);
    }
}