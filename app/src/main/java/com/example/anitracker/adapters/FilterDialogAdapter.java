package com.example.anitracker.adapters;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.uiObjects.ChipGroupSearch;
import com.example.anitracker.uiObjects.FilterChip;
import com.example.anitracker.uiObjects.FilterChipGroup;
import com.example.anitracker.uiObjects.Header;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class FilterDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Object> objectList;
    private final Context context;
    private LayoutInflater inflater;

    private final int headerVar = 0,
            chipGroupSearchVar = 1,
            chipGroupVar = 2;


    public FilterDialogAdapter(Context context, List<Object> uiObjects) {
        this.context = context;
        this.objectList = uiObjects;
    }

    @Override
    public int getItemViewType(int position) {
        Object object = objectList.get(position);
        if (object instanceof Header) {
            return headerVar;
        } else if (object instanceof ChipGroupSearch) {
            return chipGroupSearchVar;
        } else if (object instanceof FilterChipGroup) {
            return chipGroupVar;
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

            case chipGroupSearchVar:
                view = inflater.inflate(R.layout.searchable_filter_layout, parent, false);
                viewHolder = new ChipGroupSearch.ViewHolder(view);
                break;

            case chipGroupVar:
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

            case chipGroupVar:
                FilterChipGroup.ViewHolder chipGroupView = (FilterChipGroup.ViewHolder) holder;
                FilterChipGroup chipGroup = (FilterChipGroup) object;
                chipGroupView.setup(chipGroup);

                chipGroupView.chipGroup.removeAllViews();
                this.addFilterChips(chipGroupView.chipGroup, chipGroup);
                break;

            case chipGroupSearchVar:
                ChipGroupSearch.ViewHolder searchChipGroupView = (ChipGroupSearch.ViewHolder) holder;
                ChipGroupSearch searchableChipGroup = (ChipGroupSearch) object;

                Handler handler = new Handler();
                searchChipGroupView.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        handler.removeCallbacksAndMessages(null);
                        handler.postDelayed(() -> searchableChipGroup.getUserSearch().setValue(s), 500);
                        return true;
                    }
                });
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
        List<FilterChip> filterChips = filterChipGroup.getFilterChips();
        for (int i = 0; i < filterChips.size(); i++) {
            Chip chip = (Chip) inflater.inflate(R.layout.chip_layout, chipGroup, false);
            String name = filterChips.get(i).getText();
            chip.setText(name);
            chip.setId(i);
            chip.setOnCheckedChangeListener((e, isChecked) -> {
                if (isChecked) {
                    filterChipGroup.getSelected().add(name);
                } else {
                    filterChipGroup.getSelected().remove(name);
                }

                Log.d("Testing", filterChipGroup.getSelected().toString());
            });

            if (filterChipGroup.getSelected().contains(name)) {
                chip.setChecked(true);
            }

            chipGroup.addView(chip);
        }
    }
}