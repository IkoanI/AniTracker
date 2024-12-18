package com.example.anitracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private Context context;
    private DetailsViewModel detailsViewModel;
    private View view;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.detailsViewModel = new ViewModelProvider(requireActivity()).get(DetailsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recycler_view, container, false);
        view.setVisibility(View.INVISIBLE);
        detailsViewModel.observeMediaDetails().observe(getViewLifecycleOwner(), this::insertDetails);
        return view;
    }

    public void insertDetails(MediaDetails details){
        // setting up recyclerView displaying genres

        RecyclerView overviewView = view.findViewById(R.id.recView);
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

        overviewViewObjects.add(new TagsHeader());

        overviewViewObjects.addAll(details.getNoSpoilerTags());

        OverviewViewAdapter overviewViewAdapter = new OverviewViewAdapter(details, overviewViewObjects, context);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        layoutManager.setFlexWrap(FlexWrap.WRAP);

        overviewView.setAdapter(overviewViewAdapter);
        overviewView.setLayoutManager(layoutManager);


        view.setVisibility(View.VISIBLE);
    }
}
