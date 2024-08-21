package com.example.anitracker.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anitracker.R;
import com.example.anitracker.activities.Details;
import com.example.anitracker.adapters.MangaSearchPageViewAdapter;
import com.example.anitracker.interfaces.RecyclerViewInterface;
import com.example.anitracker.viewModels.MainViewModel;

public class MangaSearchFragment extends Fragment implements RecyclerViewInterface {
    MainViewModel viewModel;
    Context context;
    MangaSearchPageViewAdapter adapter;

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
        adapter = new MangaSearchPageViewAdapter(context, this, viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manga_search_fragment, container, false);

        // observe manga page list
        viewModel.observeMangaPage().observe(getViewLifecycleOwner(), adapter::addItems);
        if(adapter.getItemCount() == 0){
            viewModel.getMangaPage();
        }
        // observe user search
        viewModel.observeUserSearch().observe(getViewLifecycleOwner(), res -> {
            adapter.clearItems();
            adapter.setUserSearch(res);
            viewModel.setLoadedMangaPages(1);
            if(res.isEmpty()){
                viewModel.getMangaPage();
            }
            else{
                viewModel.getMangaPage(res);
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
        intent.putExtra("Type", adapter.getItem(position).getType());
        startActivity(intent);
    }
}