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
import com.example.anitracker.type.MediaType;
import com.example.anitracker.uiObjects.Header;
import com.example.anitracker.uiObjects.TagsHeader;
import com.example.anitracker.viewModels.EntityViewModel;

import java.util.ArrayList;
import java.util.List;

public class EntityOverviewFragment extends OverviewFragment {
    private EntityViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(requireActivity()).get(EntityViewModel.class);
        // get and observe details from repository
        if (this.viewModel.getMediaType() != MediaType.VISUAL_NOVEL) {
            this.viewModel.observeEntityDetail().observe(getViewLifecycleOwner(), this::insertDetails);
        } else {
            this.viewModel.observeVNEntityDetail().observe(getViewLifecycleOwner(), res-> this.insertDetails(res.getVNCharList(viewModel.getId()).get(0)));
        }

        return super.uiSetup(inflater, container);
    }

    private void insertDetails(CharacterDetails details) {
        // setting up recyclerView displaying genres
        List<Object> overviewViewObjects = new ArrayList<>();

        if (details.getDescription() != null && !details.getDescription().isBlank()) {
            overviewViewObjects.add(new Header("Description"));
            overviewViewObjects.add(new Description(details.getDescription()));
        }

        overviewViewObjects.add(new Header("Info"));
        overviewViewObjects.addAll(details.getInfo());

        if (viewModel.getMediaType().equals(MediaType.VISUAL_NOVEL)) {
            overviewViewObjects.add(new TagsHeader(details));
            overviewViewObjects.addAll(details.getNoSpoilerTraits());
        }

        this.overviewViewAdapter.addObjects(overviewViewObjects);
        this.progressBar.setVisibility(View.GONE);
    }
}
