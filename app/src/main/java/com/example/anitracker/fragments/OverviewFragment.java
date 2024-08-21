package com.example.anitracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.adapters.GenreViewAdapter;
import com.example.anitracker.adapters.OverviewViewAdapter;
import com.example.anitracker.adapters.TagsViewAdapter;
import com.example.anitracker.interfaces.RecyclerViewInterface;
import com.example.anitracker.mediaObjects.Description;
import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.uiObjects.Header;
import com.example.anitracker.viewModels.DetailsViewModel;
import com.example.anitracker.vnObjects.VNDetails;


import java.util.ArrayList;
import java.util.List;

public class OverviewFragment extends Fragment implements RecyclerViewInterface {
    private Context context;
    private DetailsViewModel detailsViewModel;
    private TagsViewAdapter tagsViewAdapter;
    private View view;
    private Boolean spoilerTagShown = false;

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
        view = inflater.inflate(R.layout.overview_fragment, container, false);
        view.setVisibility(View.INVISIBLE);
        detailsViewModel.observeMediaDetails().observe(getViewLifecycleOwner(), this::insertDetails);
        return view;
    }

    public void insertDetails(MediaDetails details){
        // setting up recyclerView displaying genres

        RecyclerView overviewView = view.findViewById(R.id.overviewView);
        List<Object> overviewViewObjects = new ArrayList<>();
        if(details.getGenres() != null){
            overviewViewObjects.add(details.getGenres());
        }
        if(details.getDesc() != null){
            overviewViewObjects.add(new Header("Synopsis"));
            overviewViewObjects.add(new Description(details.getDesc()));
        }

        if(details.getTrailer() != null){
            overviewViewObjects.add(details.getTrailer());
        }
        if(details instanceof VNDetails){
            if(!((VNDetails) details).getScreenshots().getScreenshotURLs().isEmpty()){
                overviewViewObjects.add(new Header("Screenshots"));
                overviewViewObjects.add(((VNDetails) details).getScreenshots());
            }

        }
        overviewViewObjects.add(new Header("Info"));
        overviewViewObjects.addAll(details.getInfo());
        OverviewViewAdapter overviewViewAdapter = new OverviewViewAdapter(overviewViewObjects, context);
        LinearLayoutManager overviewLayoutManager = new LinearLayoutManager(context);
        overviewView.setAdapter(overviewViewAdapter);
        overviewView.setLayoutManager(overviewLayoutManager);


        RecyclerView tagsView = view.findViewById(R.id.tagsView);
        tagsViewAdapter = new TagsViewAdapter(details.getNoSpoilerTags(), context, this);
        tagsView.setAdapter(tagsViewAdapter);
        GridLayoutManager tagsLayoutManager = new GridLayoutManager(context, 2);
        tagsView.setLayoutManager(tagsLayoutManager);

        // set up show spoiler tag button
        TextView showSpoilerTags = view.findViewById(R.id.showSpoilers);
        if(details.getAllTags().size() == details.getNoSpoilerTags().size()){
            // no spoiler tags, hide show spoiler button
            showSpoilerTags.setVisibility(View.GONE);
        }
        else{
            showSpoilerTags.setOnClickListener(view1 -> {
                if(spoilerTagShown){
                    showSpoilerTags.setText(R.string.show_spoilers);
                    spoilerTagShown = false;
                    tagsViewAdapter.setTags(details.getNoSpoilerTags());
                }
                else{
                    showSpoilerTags.setText(R.string.hide_spoilers);
                    spoilerTagShown = true;
                    tagsViewAdapter.setTags(details.getAllTags());
                }
            });
        }

        view.setVisibility(View.VISIBLE);
    }


    @Override
    public void onItemClick(int position) {
        Toast.makeText(context, tagsViewAdapter.tags.get(position).getTagName(), Toast.LENGTH_SHORT).show();
    }
}
