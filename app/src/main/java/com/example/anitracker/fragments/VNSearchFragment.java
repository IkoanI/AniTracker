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
import com.example.anitracker.adapters.VNSearchViewAdapter;
import com.example.anitracker.interfaces.RecyclerViewInterface;
import com.example.anitracker.viewModels.MainViewModel;

public class VNSearchFragment extends Fragment implements RecyclerViewInterface {
    MainViewModel viewModel;
    Context context;
    VNSearchViewAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        // create recycler view adapter
        adapter = new VNSearchViewAdapter(context, this, viewModel);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vn_search_fragment, container, false);

        // observe vn page list
        String fields = "title, image{thumbnail}, developers{name}, released, length, length_minutes, rating, id";
        viewModel.observeVNPage().observe(getViewLifecycleOwner(), res -> {
            adapter.addItems(res.getVnDetailsList());
            adapter.setHasMore(res.hasMore());
        });
        if(adapter.getItemCount() == 0){
            viewModel.getVNPage("rating", fields);
        }

        // observe user search
        viewModel.observeUserSearch().observe(getViewLifecycleOwner(), res -> {
            adapter.clearItems();
            adapter.setUserSearch(res);
            viewModel.setLoadedVNPages(1);
            if(res.isEmpty()){
                viewModel.getVNPage("rating", fields);
            }
            else{
                viewModel.getVNPage(res, "searchrank", fields);
            }

        });

        // set up recycler view
        RecyclerView recyclerView = view.findViewById(R.id.recView);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(context, Details.class);
        intent.putExtra("ID", adapter.getItem(position).getId());
        intent.putExtra("Type", "Visual Novel");
        startActivity(intent);
    }
}
