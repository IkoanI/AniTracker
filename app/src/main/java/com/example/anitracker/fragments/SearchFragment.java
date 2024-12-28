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
import com.example.anitracker.adapters.SearchAdapter;
import com.example.anitracker.interfaces.RecyclerViewInterface;
import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.viewModels.SearchViewModel;

import java.util.List;

public class SearchFragment extends Fragment implements RecyclerViewInterface {
    private SearchViewModel viewModel;
    private Context context;
    private final MediaType mediaType;
    private SearchAdapter adapter;
    private ProgressBar loadingIndicator;
    private TextView noData;

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
        this.viewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        this.adapter = new SearchAdapter(this.mediaType, context, this, viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        this.loadingIndicator = view.findViewById(R.id.loadingSpinner);
        this.noData = view.findViewById(R.id.noData);

        // observe search result
        if (mediaType == MediaType.ANIME) {
            viewModel.observeAnimeSearchPage().observe(getViewLifecycleOwner(), this::addItems);
        } else if (mediaType == MediaType.MANGA) {
            viewModel.observeMangaSearchPage().observe(getViewLifecycleOwner(), this::addItems);
        } else {
            viewModel.observeVNSearchPage().observe(getViewLifecycleOwner(), this::addItems);
        }


        // fetch data when list is empty
        if (adapter.getItemCount() == 0) {
            this.loadingIndicator.setVisibility(View.VISIBLE);
            viewModel.getSearchPage(this.mediaType);
        }

        // observe user search
        viewModel.observeUserSearch().observe(getViewLifecycleOwner(), res -> {
            adapter.clearItems();
            viewModel.setLoadedPages(this.mediaType, 1);
            loadingIndicator.setVisibility(View.VISIBLE);
            viewModel.getSearchPage(this.mediaType, res);
        });

        // set up recycler view
        RecyclerView recyclerView = view.findViewById(R.id.recView);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

    private void addItems(List<? extends MediaDetails> mediaDetailsList) {
        adapter.addItems(mediaDetailsList);
        loadingIndicator.setVisibility(View.GONE);
        if (adapter.getItemCount() == 0) {
            this.noData.setVisibility(View.VISIBLE);
        } else {
            this.noData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(context, Details.class);
        intent.putExtra("ID", adapter.getItem(position).getId());
        intent.putExtra("Type", adapter.getItem(position).getType().rawValue);
        startActivity(intent);
    }
}