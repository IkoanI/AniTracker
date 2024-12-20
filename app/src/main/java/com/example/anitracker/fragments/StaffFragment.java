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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.adapters.StaffViewAdapter;
import com.example.anitracker.mediaObjects.StaffDetails;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.viewModels.DetailsViewModel;

import java.util.List;

public class StaffFragment extends Fragment {
    private Context context;
    private DetailsViewModel detailsViewModel;
    private StaffViewAdapter staffViewAdapter;
    private TextView noData;
    private ProgressBar loadingIndicator;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.detailsViewModel = new ViewModelProvider(requireActivity()).get(DetailsViewModel.class);
        this.staffViewAdapter = new StaffViewAdapter(context, detailsViewModel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        this.noData = view.findViewById(R.id.noData);
        this.loadingIndicator = view.findViewById(R.id.loadingSpinner);
        this.loadingIndicator.setVisibility(View.VISIBLE);

        // set up recyclerview
        RecyclerView staffView = view.findViewById(R.id.recView);
        staffView.setAdapter(staffViewAdapter);
        LinearLayoutManager staffViewLayoutManager = new LinearLayoutManager(context);
        staffView.setLayoutManager(staffViewLayoutManager);

        if (detailsViewModel.getType() != MediaType.VISUAL_NOVEL) {
            detailsViewModel.observeStaffPage().observe(getViewLifecycleOwner(), this::addStaffs);
            detailsViewModel.getStaffPage();
        } else if (detailsViewModel.getType() == MediaType.VISUAL_NOVEL) {
            this.addStaffs(detailsViewModel.getVnStaffsList());
        }

        return view;
    }

    public void addStaffs(List<StaffDetails> staffList) {
        staffViewAdapter.addStaffs(staffList);
        this.loadingIndicator.setVisibility(View.GONE);
        if (staffViewAdapter.getItemCount() == 0) {
            this.noData.setVisibility(View.VISIBLE);
        } else {
            this.noData.setVisibility(View.GONE);
        }
    }
}
