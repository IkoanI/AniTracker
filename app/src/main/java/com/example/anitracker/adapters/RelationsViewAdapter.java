package com.example.anitracker.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anitracker.R;
import com.example.anitracker.interfaces.RecyclerViewInterface;
import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.viewModels.DetailsViewModel;

import java.util.ArrayList;
import java.util.List;

public class RelationsViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    DetailsViewModel viewModel;
    List<MediaDetails> relationsList = new ArrayList<>();
    RecyclerViewInterface recyclerViewInterface;
    private boolean loading = false;

    public RelationsViewAdapter(Context context, DetailsViewModel viewModel, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.viewModel = viewModel;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void addRelations(List<MediaDetails> newItems){
        loading = true;
        relationsList.addAll(newItems);
        notifyItemRangeInserted(relationsList.size()-newItems.size(), newItems.size());
        loading = false;
    }

    public MediaDetails getRelation(int pos){
        return relationsList.get(pos);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.relations_view_layout, parent, false);
        return new RelationsViewAdapter.RelationsViewItem(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RelationsViewItem relationsViewItem = (RelationsViewItem) holder;
        Glide.with(context).load(relationsList.get(position).getCoverImg()).into(relationsViewItem.coverImg);
        relationsViewItem.relation.setText(relationsList.get(position).getRelation());
        relationsViewItem.title.setText(relationsList.get(position).getTitles().getUserPref());
        relationsViewItem.formatAndStatus.setText(String.format("%s · %s", relationsList.get(position).getFormat(),
                relationsList.get(position).getStatus()));
    }

    @Override
    public int getItemCount() {
        return relationsList.size();
    }

    public static class RelationsViewItem extends RecyclerView.ViewHolder{
        TextView relation, title, formatAndStatus;
        ImageView coverImg;
        public RelationsViewItem(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            relation = itemView.findViewById(R.id.relation);
            title = itemView.findViewById(R.id.mediaTitle);
            formatAndStatus = itemView.findViewById(R.id.formatAndStatus);
            coverImg = itemView.findViewById(R.id.mediaImage);
            itemView.setOnClickListener(view -> {
                if(recyclerViewInterface != null){
                    int pos = getBindingAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(pos);
                    }
                }
            });
        }
    }
}
