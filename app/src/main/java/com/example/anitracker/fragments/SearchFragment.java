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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.adapters.SearchAdapter;
import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.repository.AnilistFilters;
import com.example.anitracker.repository.SearchFilter;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.viewModels.SearchViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment {
    private SearchViewModel viewModel;
    private Context context;
    private MediaType mediaType;
    private SearchAdapter adapter;
    private ProgressBar loadingIndicator;
    private TextView noData;
    private SearchFilter searchFilter;
    private final MutableLiveData<List<? extends MediaDetails>> searchResults = new MutableLiveData<>();

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
        this.adapter = new SearchAdapter(this.searchFilter, context, viewModel, searchResults);
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        this.loadingIndicator = view.findViewById(R.id.loadingSpinner);
        this.noData = view.findViewById(R.id.noData);

        searchResults.observe(getViewLifecycleOwner(), this::addItems);

        // fetch data when list is empty
        if (adapter.getItemCount() == 0) {
            this.resetSearchPage();
            viewModel.getSearchPage(this.searchFilter, searchResults);
        }

        // observe user search
        this.searchFilter.setUserSearch(viewModel.getUserSearch().getValue());
        viewModel.observeUserSearch().observe(getViewLifecycleOwner(), res -> {
            this.resetSearchPage();
            this.searchFilter.setUserSearch(res);
            viewModel.getSearchPage(this.searchFilter, searchResults);
        });

        FloatingActionButton actionButton = view.findViewById(R.id.actionButton);
        actionButton.setOnClickListener(e -> {
            this.resetSearchPage();
            if (this.mediaType != MediaType.VISUAL_NOVEL && AnilistFilters.genres.getValue() == null) {
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

    private <T> void addItems(List<T> searchResults) {
        @SuppressWarnings("unchecked")
        List<? extends MediaDetails> mediaResults = (List<? extends MediaDetails>) searchResults;
        adapter.addItems(mediaResults);
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
        FilterDialogFragment newFragment = new FilterDialogFragment(this.searchFilter, searchResults);
        newFragment.show(ft, "dialog");
    }

    public void resetSearchPage() {
        this.adapter.clearItems();
        this.searchFilter.setPage(1);
        loadingIndicator.setVisibility(View.VISIBLE);
    }
}