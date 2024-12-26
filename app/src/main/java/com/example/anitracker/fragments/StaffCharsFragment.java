package com.example.anitracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.example.anitracker.adapters.CharacterViewAdapter;
import com.example.anitracker.viewModels.EntityViewModel;

public class StaffCharsFragment extends CharactersFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EntityViewModel viewModel = new ViewModelProvider(requireActivity()).get(EntityViewModel.class);
        this.characterViewAdapter = new CharacterViewAdapter(this.context, viewModel);
        // observe updates to char list
        viewModel.observeStaffChars().observe(getViewLifecycleOwner(), this::addChars);
        // initial retrieval of characters
        viewModel.getStaffChars();
        return this.uiSetup(inflater, container);
    }
}
