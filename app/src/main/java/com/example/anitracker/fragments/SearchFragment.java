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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.activities.Details;
import com.example.anitracker.adapters.SearchAdapter;
import com.example.anitracker.interfaces.RecyclerViewInterface;
import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.repository.AnilistFilters;
import com.example.anitracker.repository.SearchFilter;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.viewModels.SearchViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment implements RecyclerViewInterface {
    private SearchViewModel viewModel;
    private Context context;
    private MediaType mediaType;
    private SearchAdapter adapter;
    private ProgressBar loadingIndicator;
    private TextView noData;
    private SearchFilter searchFilter;

    public static SearchFragment newInstance(MediaType mediaType) {
        Bundle args = new Bundle();
        args.putString("mediaType", mediaType.rawValue);
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(args);
        return searchFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.mediaType = MediaType.safeValueOf(Objects.requireNonNull(requireArguments().getString("mediaType")));
        this.searchFilter = new SearchFilter();
        this.searchFilter.setMediaType(mediaType);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        this.adapter = new SearchAdapter(this.searchFilter, context, this, viewModel);
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
            viewModel.getSearchPage(this.searchFilter);
        }

        // observe user search
        viewModel.observeUserSearch().observe(getViewLifecycleOwner(), res -> {
            this.resetSearchPage();
            this.searchFilter.setUserSearch(res);
            viewModel.getSearchPage(this.searchFilter);
        });

        FloatingActionButton actionButton = view.findViewById(R.id.actionButton);
        actionButton.setOnClickListener(e -> {
            this.resetSearchPage();
            if (AnilistFilters.genres.getValue() == null) {
                viewModel.getMediaAttributes();
                AnilistFilters.genres.observe(getViewLifecycleOwner(), res -> this.showFilterDialog());
            } else {
                this.showFilterDialog();
            }
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

    public void showFilterDialog() {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        Fragment prev = getParentFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        FilterDialogFragment newFragment = new FilterDialogFragment(this.searchFilter);
        newFragment.show(ft, "dialog");
    }

    public void resetSearchPage() {
        this.adapter.clearItems();
        this.searchFilter.setPage(1);
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(context, Details.class);
        intent.putExtra("ID", adapter.getItem(position).getId());
        intent.putExtra("Type", adapter.getItem(position).getType().rawValue);
        startActivity(intent);
    }
}