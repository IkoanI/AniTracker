package com.example.anitracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.adapters.OverviewViewAdapter;
import com.example.anitracker.mediaObjects.Description;
import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.uiObjects.Header;
import com.example.anitracker.uiObjects.TagsHeader;
import com.example.anitracker.viewModels.DetailsViewModel;
import com.example.anitracker.vnObjects.VNDetails;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;


import java.util.ArrayList;
import java.util.List;

public class OverviewFragment extends Fragment {
    protected Context context;
    protected ProgressBar progressBar;
    protected OverviewViewAdapter overviewViewAdapter;

    private DetailsViewModel detailsViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overviewViewAdapter = new OverviewViewAdapter(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.detailsViewModel = new ViewModelProvider(requireActivity()).get(DetailsViewModel.class);
        this.detailsViewModel.observeMediaDetails().observe(getViewLifecycleOwner(), this::insertDetails);
        return this.uiSetup(inflater, container);
    }

    public View uiSetup(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        this.progressBar = view.findViewById(R.id.loadingSpinner);
        this.progressBar.setVisibility(View.VISIBLE);
        RecyclerView overviewView = view.findViewById(R.id.recView);
        overviewView.setAdapter(overviewViewAdapter);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        overviewView.setLayoutManager(layoutManager);
        return view;
    }

    public void insertDetails(MediaDetails details) {
        // relations list for vn is fetched in the big api call in the overview fragment and kept in viewmodel for usage in relations fragment
        if (details instanceof VNDetails) {
            detailsViewModel.setVnRelationsList(((VNDetails) details).getRelations());
            detailsViewModel.setVnStaffsList(((VNDetails) details).getStaffs());
            detailsViewModel.setKnownVAs(((VNDetails) details).getKnownVAs());
        }
        // setting up recyclerView displaying genres
        List<Object> overviewViewObjects = new ArrayList<>();

        if (details.getGenres() != null) {
            overviewViewObjects.add(details.getGenres());
        }
        if (details.getDesc() != null) {
            overviewViewObjects.add(new Header("Synopsis"));
            overviewViewObjects.add(new Description(details.getDesc()));
        }

        if (details.getTrailer() != null) {
            overviewViewObjects.add(details.getTrailer());
        }

        if(details instanceof VNDetails){
            if (!((VNDetails) details).getScreenshots().getScreenshotURLs().isEmpty()) {
                overviewViewObjects.add(new Header("Screenshots"));
                overviewViewObjects.add(((VNDetails) details).getScreenshots());
            }
        }
        overviewViewObjects.add(new Header("Info"));
        overviewViewObjects.addAll(details.getInfo());

        overviewViewObjects.add(new TagsHeader(details));

        overviewViewObjects.addAll(details.getNoSpoilerTags());

        overviewViewAdapter.addObjects(overviewViewObjects);
        this.progressBar.setVisibility(View.GONE);
    }
}
