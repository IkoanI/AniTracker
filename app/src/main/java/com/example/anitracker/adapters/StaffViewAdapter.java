package com.example.anitracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.activities.EntityDetails;
import com.example.anitracker.mediaObjects.StaffDetails;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.uiObjects.Image;
import com.example.anitracker.viewModels.DetailsViewModel;

import java.util.ArrayList;
import java.util.List;

public class StaffViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<StaffDetails> staffDetailsList = new ArrayList<>();
    private final Context context;
    private Boolean loading = false;
    private final DetailsViewModel viewModel;

    public StaffViewAdapter(Context context, DetailsViewModel viewModel){
        this.context = context;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.staff_card, parent, false);
        return new StaffViewAdapter.StaffViewItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!loading && position >= getItemCount()-1 && viewModel.getType() != MediaType.VISUAL_NOVEL) {
            viewModel.getStaffPage();
        }
        StaffViewItem staffViewItem = (StaffViewItem) holder;
        StaffDetails staff = staffDetailsList.get(position);

        Image.loadImage(this.context, staff.getImage(), staffViewItem.staffImage);
        staffViewItem.staffImage.setOnClickListener(e -> {
            Intent intent = new Intent(context, EntityDetails.class);
            intent.putExtra("ID", staff.getId());
            intent.putExtra("Type", viewModel.getType().rawValue);
            intent.putExtra("Entity", "Staff");
            context.startActivity(intent);
        });
        staffViewItem.staffName.setText(staff.getName().getUserPref());
        staffViewItem.staffRole.setText(staff.getRole());
    }

    @Override
    public int getItemCount() {
        return staffDetailsList.size();
    }

    public void addStaffs(List<StaffDetails> newItems){
        loading = true;
        staffDetailsList.addAll(newItems);
        notifyItemRangeInserted(staffDetailsList.size()-newItems.size(), newItems.size());
        loading = false;
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
