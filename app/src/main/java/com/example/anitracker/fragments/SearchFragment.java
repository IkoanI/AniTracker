package com.example.anitracker.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.activities.Details;
import com.example.anitracker.adapters.SearchAdapter;
import com.example.anitracker.interfaces.RecyclerViewInterface;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.viewModels.MainViewModel;

public class SearchFragment extends Fragment implements RecyclerViewInterface {
    private MainViewModel viewModel;
    private Context context;
    private final MediaType mediaType;
    private SearchAdapter adapter;

    public SearchFragment(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        this.adapter = new SearchAdapter(this.mediaType, context, this, viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        ProgressBar loadingIndicator = view.findViewById(R.id.loadingSpinner);

        // observe search result
        if (this.mediaType == MediaType.ANIME) {
            viewModel.observeAnimePage().observe(getViewLifecycleOwner(), res -> {
                adapter.addItems(res);
                loadingIndicator.setVisibility(View.GONE);
            });
        } else if (this.mediaType == MediaType.MANGA) {
            viewModel.observeMangaPage().observe(getViewLifecycleOwner(), res -> {
                adapter.addItems(res);
                loadingIndicator.setVisibility(View.GONE);
            });
        } else if (this.mediaType == MediaType.VISUAL_NOVEL) {
            viewModel.observeVNPage().observe(getViewLifecycleOwner(), res -> {
                adapter.addItems(res);
                loadingIndicator.setVisibility(View.GONE);
            });
        }

        // fetch data when list is empty
        if(adapter.getItemCount() == 0){
            loadingIndicator.setVisibility(View.VISIBLE);
            viewModel.getSearchPage(this.mediaType);
        }

        // observe user search
        viewModel.observeUserSearch().observe(getViewLifecycleOwner(), res ->{
            adapter.clearItems();
            adapter.setUserSearch(res);
            viewModel.setLoadedPages(this.mediaType, 1);
            if (res.isEmpty()) {
                loadingIndicator.setVisibility(View.VISIBLE);
                viewModel.getSearchPage(this.mediaType);
            }
            else {
                loadingIndicator.setVisibility(View.VISIBLE);
                viewModel.getSearchPage(this.mediaType, res);
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