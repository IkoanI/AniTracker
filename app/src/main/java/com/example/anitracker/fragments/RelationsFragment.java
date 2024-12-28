package com.example.anitracker.fragments;

import android.content.Context;
import android.content.Intent;
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
import com.example.anitracker.activities.Details;
import com.example.anitracker.adapters.RelationsViewAdapter;
import com.example.anitracker.interfaces.RecyclerViewInterface;
import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.viewModels.DetailsViewModel;

import java.util.List;
import java.util.Objects;

public class RelationsFragment extends Fragment implements RecyclerViewInterface {
    protected Context context;
    protected ProgressBar loadingIndicator;
    protected RelationsViewAdapter relationsViewAdapter;
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
        DetailsViewModel viewModel = new ViewModelProvider(requireActivity()).get(DetailsViewModel.class);
        this.relationsViewAdapter = new RelationsViewAdapter(context, this, viewModel);
        View view = this.uiSetup(inflater, container);

        viewModel.observeRelationsPage().observe(getViewLifecycleOwner(), this::addRelations);
        if (Objects.equals(viewModel.getEntityType(), "Char")) {
            viewModel.getCharRoles();
        } else if (Objects.equals(viewModel.getEntityType(), "Staff")) {
            viewModel.getStaffRoles();
        } else {
            viewModel.getRelationsPage();
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

    public void addRelations(List<? extends MediaDetails> relationsList) {
        relationsViewAdapter.addRelations(relationsList);
        this.loadingIndicator.setVisibility(View.GONE);
        if (relationsViewAdapter.getItemCount() == 0) {
            this.noData.setVisibility(View.VISIBLE);
        } else {
            this.noData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(int position) {
        MediaDetails selected = relationsViewAdapter.getRelation(position);
        Intent intent = new Intent(context, Details.class);
        intent.putExtra("ID", selected.getId());
        intent.putExtra("Type", selected.getType().rawValue);
        startActivity(intent);
    }
}
