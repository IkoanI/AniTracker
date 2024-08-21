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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.adapters.CharacterViewAdapter;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.type.StaffLanguage;
import com.example.anitracker.uiObjects.LanguageDropdown;
import com.example.anitracker.viewModels.DetailsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CharactersFragment extends Fragment {
    private Context context;
    private DetailsViewModel detailsViewModel;
    private CharacterViewAdapter characterViewAdapter;

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
        // creating list of objects to populate recycler view
        List<Object> objectList = new ArrayList<>();
        if(Objects.equals(detailsViewModel.getType(), MediaType.ANIME.rawValue)){
            // if not anime, no need for ability to change language of voice actor
            objectList.add(new LanguageDropdown());
        }
        this.characterViewAdapter = new CharacterViewAdapter(objectList, context, detailsViewModel);
        if(Objects.equals(detailsViewModel.getType(), "Visual Novel")){
            detailsViewModel.observeVNCharPage().observe(getViewLifecycleOwner(), res -> {
                characterViewAdapter.setHasMorePages(res.hasMore());
                characterViewAdapter.addChars(res.getVNCharList(detailsViewModel.getVndbId()));
            });
        }
        else{
            detailsViewModel.observeCharPage().observe(getViewLifecycleOwner(), res -> characterViewAdapter.addChars(res));
        }
        View view = inflater.inflate(R.layout.characters_fragment, container, false);
        //initializing recycler view
        RecyclerView characterView = view.findViewById(R.id.characterView);
        characterView.setAdapter(characterViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        characterView.setLayoutManager(linearLayoutManager);
        return view;
    }
}
