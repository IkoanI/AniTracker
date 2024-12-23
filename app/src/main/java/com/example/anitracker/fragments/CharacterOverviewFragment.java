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
import com.example.anitracker.mediaObjects.CharacterDetails;
import com.example.anitracker.mediaObjects.Description;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.uiObjects.Header;
import com.example.anitracker.uiObjects.TagsHeader;
import com.example.anitracker.viewModels.EntityViewModel;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class CharacterOverviewFragment extends Fragment {
    private Context context;
    private EntityViewModel viewModel;
    private View view;
    private ProgressBar progressBar;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewModel = new ViewModelProvider(requireActivity()).get(EntityViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.recycler_view, container, false);
        this.progressBar = view.findViewById(R.id.loadingSpinner);
        this.progressBar.setVisibility(View.VISIBLE);
        // get and observe details from repository
        if (this.viewModel.getMediaType() != MediaType.VISUAL_NOVEL) {
            this.viewModel.observeCharacterDetail().observe(getViewLifecycleOwner(), this::insertDetails);
        } else {
            this.viewModel.observeVNCharDetail().observe(getViewLifecycleOwner(), res-> {
                this.insertDetails(res.getVNCharList(viewModel.getId()).get(0));
            });
        }
        return view;
    }

    private void insertDetails(CharacterDetails details) {
        // setting up recyclerView displaying genres
        RecyclerView overviewView = view.findViewById(R.id.recView);
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

        OverviewViewAdapter overviewViewAdapter = new OverviewViewAdapter(overviewViewObjects, context);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        layoutManager.setFlexWrap(FlexWrap.WRAP);

        overviewView.setAdapter(overviewViewAdapter);
        overviewView.setLayoutManager(layoutManager);
        this.progressBar.setVisibility(View.GONE);
    }
}
