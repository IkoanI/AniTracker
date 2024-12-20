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
import com.example.anitracker.type.StaffLanguage;
import com.example.anitracker.uiObjects.LanguageDropdown;
import com.example.anitracker.viewModels.DetailsViewModel;

import java.util.ArrayList;
import java.util.List;

public class CharactersFragment extends Fragment {
    private Context context;
    private DetailsViewModel detailsViewModel;
    private CharacterViewAdapter characterViewAdapter;
    private TextView noData;
    private ProgressBar loadingIndicator;
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
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        this.noData = view.findViewById(R.id.noData);
        this.loadingIndicator = view.findViewById(R.id.loadingSpinner);
        this.loadingIndicator.setVisibility(View.VISIBLE);

        // creating list of objects to populate recycler view
        List<Object> objectList = new ArrayList<>();
        if (detailsViewModel.getType() == MediaType.ANIME) {
            // if not anime, no need for ability to change language of voice actor
            objectList.add(new LanguageDropdown());
        }

        this.characterViewAdapter = new CharacterViewAdapter(objectList, context, detailsViewModel);

        if (detailsViewModel.getType() == MediaType.VISUAL_NOVEL) {
            detailsViewModel.observeVNCharPage().observe(getViewLifecycleOwner(), res -> {
                characterViewAdapter.setHasMorePages(res.hasMore());
                this.addChars(res.getVNCharList(detailsViewModel.getId()));
            });
            this.detailsViewModel.getVNCharPage();
        } else {
            detailsViewModel.observeCharPage().observe(getViewLifecycleOwner(), this::addChars);
            this.detailsViewModel.getCharPage(StaffLanguage.JAPANESE);
        }

        //initializing recycler view
        RecyclerView characterView = view.findViewById(R.id.recView);
        characterView.setAdapter(characterViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        characterView.setLayoutManager(linearLayoutManager);
        return view;
    }

    public void addChars(List<CharacterDetails> characterDetailsList) {
        characterViewAdapter.addChars(characterDetailsList);
        this.loadingIndicator.setVisibility(View.GONE);
        if (characterViewAdapter.getItemCount() - (detailsViewModel.getType().equals(MediaType.ANIME) ? 1 : 0) == 0) {
            this.noData.setVisibility(View.VISIBLE);
        } else {
            this.noData.setVisibility(View.GONE);
        }
    }
}
