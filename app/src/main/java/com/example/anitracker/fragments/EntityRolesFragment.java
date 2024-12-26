package com.example.anitracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.adapters.RelationsViewAdapter;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.viewModels.EntityViewModel;

public class EntityRolesFragment extends RelationsFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EntityViewModel viewModel = new ViewModelProvider(requireActivity()).get(EntityViewModel.class);
        this.relationsViewAdapter = new RelationsViewAdapter(context, this, viewModel);

        View view = this.uiSetup(inflater, container);
        if (viewModel.getMediaType() != MediaType.VISUAL_NOVEL) {
            viewModel.observeRoles().observe(getViewLifecycleOwner(), this::addRelations);
            if (viewModel.getEntityType().equals("Char")) {
                viewModel.getCharRoles();
            } else {
                viewModel.getStaffRoles();
            }
        } else {
            if (viewModel.getEntityType().equals("Char")) {
                this.addRelations(viewModel.getLastFetchedCharDetail().getVnRoles());
            } else {
                // VN STAFF ROLES
            }
        }

        return view;
    }

    public View uiSetup(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        this.loadingIndicator = view.findViewById(R.id.loadingSpinner);
        this.loadingIndicator.setVisibility(View.VISIBLE);
        this.noData = view.findViewById(R.id.noData);
        // set up recyclerview
        RecyclerView relationsView = view.findViewById(R.id.recView);
        relationsView.setAdapter(relationsViewAdapter);
        LinearLayoutManager relationsViewLayoutManager = new LinearLayoutManager(context);
        relationsView.setLayoutManager(relationsViewLayoutManager);

        return view;
    }
}
