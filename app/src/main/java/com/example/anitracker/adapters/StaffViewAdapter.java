package com.example.anitracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anitracker.R;
import com.example.anitracker.mediaObjects.StaffDetails;
import com.example.anitracker.viewModels.DetailsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StaffViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<StaffDetails> staffDetailsList = new ArrayList<>();
    Context context;
    private Boolean loading = false;
    DetailsViewModel viewModel;

    public StaffViewAdapter(Context context, DetailsViewModel viewModel){
        this.context = context;
        this.viewModel = viewModel;
    }

    public void addStaffs(List<StaffDetails> newItems){
        loading = true;
        staffDetailsList.addAll(newItems);
        notifyItemRangeInserted(staffDetailsList.size()-newItems.size(), newItems.size());
        loading = false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.staff_view_layout, parent, false);
        return new StaffViewAdapter.StaffViewItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!loading && position >= getItemCount()-1 && !Objects.equals(viewModel.getType(), "Visual Novel")){
            viewModel.getStaffPage();
        }
        StaffViewItem staffViewItem = (StaffViewItem) holder;
        Glide.with(context).load(staffDetailsList.get(position).getImage()).into(staffViewItem.staffImage);
        staffViewItem.staffName.setText(staffDetailsList.get(position).getName().getUserPref());
        staffViewItem.staffRole.setText(staffDetailsList.get(position).getRole());
    }

    @Override
    public int getItemCount() {
        return staffDetailsList.size();
    }

    public static class StaffViewItem extends RecyclerView.ViewHolder{
        TextView staffName, staffRole;
        ImageView staffImage;
        public StaffViewItem(@NonNull View itemView) {
            super(itemView);
            staffImage = itemView.findViewById(R.id.staffImage);
            staffName = itemView.findViewById(R.id.staffName);
            staffRole = itemView.findViewById(R.id.role);
        }
    }
}
