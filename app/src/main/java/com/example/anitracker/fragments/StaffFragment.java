package com.example.anitracker.fragments;

import android.content.Context;
import android.os.Bundle;
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
import com.example.anitracker.adapters.StaffViewAdapter;
import com.example.anitracker.viewModels.DetailsViewModel;

import java.util.Objects;

public class StaffFragment extends Fragment {
    private Context context;
    private DetailsViewModel detailsViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.detailsViewModel = new ViewModelProvider(requireActivity()).get(DetailsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // set up recyclerview
        View view = inflater.inflate(R.layout.staff_fragment, container, false);
        RecyclerView staffView = view.findViewById(R.id.staffView);
        StaffViewAdapter staffViewAdapter = new StaffViewAdapter(context, detailsViewModel);
        staffView.setAdapter(staffViewAdapter);
        LinearLayoutManager staffViewLayoutManager = new LinearLayoutManager(context);
        staffView.setLayoutManager(staffViewLayoutManager);
        // observe change in staff page
        detailsViewModel.observeStaffPage().observe(getViewLifecycleOwner(), staffViewAdapter::addStaffs);
        if(!Objects.equals(detailsViewModel.getType(), "Visual Novel")){
            detailsViewModel.getStaffPage();
        }
        return view;
    }
}
