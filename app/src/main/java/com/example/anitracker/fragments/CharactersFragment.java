package com.example.anitracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.adapters.CharacterViewAdapter;
import com.example.anitracker.mediaObjects.CharacterDetails;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.uiObjects.LanguageDropdown;
import com.example.anitracker.viewModels.DetailsViewModel;

import java.util.ArrayList;
import java.util.List;

public class CharactersFragment extends Fragment {
    protected Context context;
    protected ProgressBar loadingIndicator;
    protected CharacterViewAdapter characterViewAdapter;
    protected TextView noData;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DetailsViewModel detailsViewModel = new ViewModelProvider(requireActivity()).get(DetailsViewModel.class);
        this.characterViewAdapter = new CharacterViewAdapter(this.context, detailsViewModel);
        // observe updates to char list
        detailsViewModel.observeCharPage().observe(getViewLifecycleOwner(), this::addChars);
        // initial retrieval of characters
        detailsViewModel.getCharPage();
        View view = this.uiSetup(inflater, container);



        // creating list of objects to populate recycler view
        List<Object> objectList = new ArrayList<>();
        if (detailsViewModel.getType() == MediaType.ANIME) {
            // if not anime, no need for ability to change language of voice actor
            objectList.add(new LanguageDropdown(this.context));
        }
        this.characterViewAdapter.addObjects(objectList);
        return view;
    }

    public View uiSetup(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        this.noData = view.findViewById(R.id.noData);
        this.loadingIndicator = view.findViewById(R.id.loadingSpinner);
        this.loadingIndicator.setVisibility(View.VISIBLE);
        //initializing recycler view
        RecyclerView characterView = view.findViewById(R.id.recView);
        characterView.setAdapter(this.characterViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);
        characterView.setLayoutManager(linearLayoutManager);
        return view;
    }

    public void addChars(List<CharacterDetails> characterDetailsList) {
        this.characterViewAdapter.addObjects(characterDetailsList);
        this.loadingIndicator.setVisibility(View.GONE);
        if (this.characterViewAdapter.getItemCount() == 0) {
            this.noData.setVisibility(View.VISIBLE);
        } else {
            this.noData.setVisibility(View.GONE);
        }
    }
}
