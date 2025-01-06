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
        View view;
        RecyclerView.ViewHolder viewHolder = null;
        this.inflater = LayoutInflater.from(context);

        switch (viewType) {
            case headerVar:
                view = inflater.inflate(R.layout.header_layout, parent, false );
                viewHolder = new Header.HeaderView(view);
                break;

            case searchViewVar:
                view = inflater.inflate(R.layout.search_view_layout, parent, false);
                viewHolder = new FilterSearchView.ViewHolder(view);
                break;

            case filterChipGroupVar:
                view = inflater.inflate(R.layout.chip_group_layout, parent, false);
                viewHolder = new FilterChipGroup.ViewHolder(view);
                break;
        }

        assert viewHolder != null;
        return viewHolder;
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
        for (String choice : filterChipGroup.getSelected()) {
            if (!filterChipGroup.getChoices().contains(choice)) {
                this.chipSetup(choice, true, chipGroup, filterChipGroup);
            }
        }

        for (String choice : filterChipGroup.getChoices()) {
            this.chipSetup(choice, filterChipGroup.getSelected().contains(choice),chipGroup, filterChipGroup);
        }
    }

    private void chipSetup(String name, boolean checked, ChipGroup chipGroup, FilterChipGroup filterChipGroup) {
        Chip chip = (Chip) inflater.inflate(R.layout.filter_chip_layout, chipGroup, false);
        chip.setText(name);
        chip.setOnCheckedChangeListener((e, isChecked) -> {
            LinkedHashSet<String> selected = filterChipGroup.getSelected();
            if (isChecked) {
                selected.add(name);
            } else {
                selected.remove(name);
                if (!filterChipGroup.getChoices().contains(name)) {
                    chipGroup.removeView(chip);
                }
            }
        });
        chip.setChecked(checked);
        chipGroup.addView(chip);
    }
}