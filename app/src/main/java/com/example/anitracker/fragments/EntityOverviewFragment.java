package com.example.anitracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.anitracker.mediaObjects.CharacterDetails;
import com.example.anitracker.mediaObjects.Description;
import com.example.anitracker.mediaObjects.Entity;
import com.example.anitracker.mediaObjects.StaffDetails;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.uiObjects.Header;
import com.example.anitracker.uiObjects.TagsHeader;
import com.example.anitracker.viewModels.EntityViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EntityOverviewFragment extends OverviewFragment {
    private EntityViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(requireActivity()).get(EntityViewModel.class);
        // get and observe details from repository
        if (Objects.equals(this.viewModel.getEntityType(), "Char")) {
            this.viewModel.observeCharDetail().observe(getViewLifecycleOwner(), this::insertDetails);
        } else if (Objects.equals(this.viewModel.getEntityType(), "Staff")) {
            this.viewModel.observeStaffDetail().observe(getViewLifecycleOwner(), this::insertDetails);
        }
        return super.uiSetup(inflater, container);
    }

    private List<Object> insertDetails (Entity details) {
        List<Object> overviewViewObjects = new ArrayList<>();
        if (details.getDescription() != null && !details.getDescription().isBlank()) {
            overviewViewObjects.add(new Header("Description"));
            overviewViewObjects.add(new Description(details.getDescription()));
        }

        overviewViewObjects.add(new Header("Info"));


        return overviewViewObjects;
    }

    private void insertDetails(CharacterDetails details) {
        List<Object> overviewViewObjects = this.insertDetails((Entity) details);
        overviewViewObjects.addAll(details.getInfo());

        if (viewModel.getMediaType().equals(MediaType.VISUAL_NOVEL)) {
            overviewViewObjects.add(new TagsHeader(details));
            overviewViewObjects.addAll(details.getNoSpoilerTraits());
        }

        this.overviewViewAdapter.addObjects(overviewViewObjects);
        this.progressBar.setVisibility(View.GONE);
    }

    private void insertDetails(StaffDetails details) {
        List<Object> overviewViewObjects = this.insertDetails((Entity) details);
        overviewViewObjects.addAll(details.getInfo());

        if (details.getLinks() != null && !details.getLinks().isEmpty()) {
            overviewViewObjects.add(new Header("Links"));
            overviewViewObjects.addAll(details.getLinks());
        }

        this.overviewViewAdapter.addObjects(overviewViewObjects);
        this.progressBar.setVisibility(View.GONE);
    }


}
