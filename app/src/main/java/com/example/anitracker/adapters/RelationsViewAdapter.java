package com.example.anitracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.interfaces.RecyclerViewInterface;
import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.uiObjects.Image;
import com.example.anitracker.viewModels.DetailsViewModel;

import java.util.ArrayList;
import java.util.List;

public class RelationsViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final DetailsViewModel viewModel;
    private final List<MediaDetails> relationsList = new ArrayList<>();
    private final RecyclerViewInterface recyclerViewInterface;

    public RelationsViewAdapter(Context context, DetailsViewModel viewModel, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.viewModel = viewModel;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.relations_card, parent, false);
        return new RelationsViewAdapter.RelationsView(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RelationsView relationsViewItem = (RelationsView) holder;
        MediaDetails relation = this.getRelation(position);
        Image.loadImage(this.context, relation.getImage(), relationsViewItem.coverImg);
        relationsViewItem.relation.setText(relation.getRelation());
        relationsViewItem.title.setText(relation.getTitles().getUserPref());
        relationsViewItem.formatAndStatus.setText(String.format("%s · %s", relation.getFormat(), relation.getStatus()));
    }

    @Override
    public int getItemCount() {
        return relationsList.size();
    }

    public void addRelations(List<MediaDetails> newItems){
        relationsList.addAll(newItems);
        notifyItemRangeInserted(relationsList.size()-newItems.size(), newItems.size());
    }

    public MediaDetails getRelation(int pos){
        return relationsList.get(pos);
    }

    public static class RelationsView extends RecyclerView.ViewHolder{
        TextView relation, title, formatAndStatus;
        ImageView coverImg;
        public RelationsView(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
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
