package com.example.anitracker.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.anitracker.activities.Details;
import com.example.anitracker.activities.MainActivity;
import com.example.anitracker.adapters.RelationsViewAdapter;
import com.example.anitracker.interfaces.RecyclerViewInterface;
import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.viewModels.DetailsViewModel;

public class RelationsFragment extends Fragment implements RecyclerViewInterface {
    private Context context;
    private DetailsViewModel detailsViewModel;
    RelationsViewAdapter relationsViewAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.detailsViewModel = new ViewModelProvider(requireActivity()).get(DetailsViewModel.class);
        this.relationsViewAdapter = new RelationsViewAdapter(context, detailsViewModel, this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // set up recyclerview
        View view = inflater.inflate(R.layout.relations_fragment, container, false);
        RecyclerView relationsView = view.findViewById(R.id.relationsView);
        relationsView.setAdapter(relationsViewAdapter);
        LinearLayoutManager relationsViewLayoutManager = new LinearLayoutManager(context);
        relationsView.setLayoutManager(relationsViewLayoutManager);
        // observe change in staff page
        detailsViewModel.getRelationsPage();
        detailsViewModel.observeRelationsPage().observe(getViewLifecycleOwner(), relationsViewAdapter::addRelations);
        return view;
    }

    @Override
    public void onItemClick(int position) {
        MediaDetails selected = relationsViewAdapter.getRelation(position);
        Log.d("RELATION SELECTION", selected.getRelation() + selected.getId());
        Intent intent = new Intent(context, Details.class);
        intent.putExtra("ID", selected.getId());
        intent.putExtra("Type", selected.getType());
        startActivity(intent);
    }
}
