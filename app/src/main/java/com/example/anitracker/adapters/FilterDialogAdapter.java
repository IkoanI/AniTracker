package com.example.anitracker.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.uiObjects.FilterChip;
import com.example.anitracker.uiObjects.FilterChipGroup;
import com.example.anitracker.uiObjects.Header;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class FilterDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Object> objectList;
    private final MediaType mediaType;
    private final Context context;
    private LayoutInflater inflater;

    private final int headerVar = 0,
            chipGroupVar = 1;

    public FilterDialogAdapter(MediaType mediaType, Context context, List<Object> uiObjects) {
        this.mediaType = mediaType;
        this.context = context;
        this.objectList = uiObjects;
    }

    @Override
    public int getItemViewType(int position) {
        Object object = objectList.get(position);
        if (object instanceof Header) {
            return headerVar;
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

            case chipGroupVar:
                view = inflater.inflate(R.layout.chip_group_layout, parent, false);
                viewHolder = new FilterChipGroup.ChipGroupView(view);
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
                FilterChipGroup.ChipGroupView chipGroupView = (FilterChipGroup.ChipGroupView) holder;
                FilterChipGroup chipGroup = (FilterChipGroup) object;
                chipGroupView.chipGroup.setSingleSelection(chipGroup.isSingleSelection());
                chipGroupView.chipGroup.setSelectionRequired(chipGroup.isSelectionRequired());
                this.addChips(chipGroup.getFilterChips(), chipGroupView.chipGroup, chipGroup);
                for (int id : chipGroup.getCheckedChipIds()) {
                    ((Chip) chipGroupView.chipGroup.getChildAt(id)).setChecked(true);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    private void addChips(List<FilterChip> filterChips, ChipGroup chipGroup, FilterChipGroup filterChipGroup) {
        for (int i = 0; i < filterChips.size(); i++) {
            Chip chip = (Chip) inflater.inflate(R.layout.chip_layout, chipGroup, false);
            chip.setText(filterChips.get(i).getText());
            chip.setId(i);
            chip.setOnCheckedChangeListener((e, isChecked) -> filterChipGroup.setCheckedChipIds(chipGroup.getCheckedChipIds()));
            chipGroup.addView(chip);
        }
    }
}
