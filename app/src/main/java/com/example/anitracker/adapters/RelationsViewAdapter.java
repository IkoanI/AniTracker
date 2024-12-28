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
import com.example.anitracker.mediaObjects.StaffDetails;
import com.example.anitracker.repository.AnilistObjectMappings;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.uiObjects.Image;
import com.example.anitracker.viewModels.DetailsViewModel;
import com.example.anitracker.vnObjects.VNDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RelationsViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<MediaDetails> relationsList = new ArrayList<>();
    private final RecyclerViewInterface recyclerViewInterface;
    private boolean loading = false;
    private final DetailsViewModel detailsViewModel;

    public RelationsViewAdapter(Context context, RecyclerViewInterface recyclerViewInterface, DetailsViewModel detailsViewModel) {
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
        this.detailsViewModel = detailsViewModel;
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
        if (!loading && position >= getItemCount()-1  && !detailsViewModel.getMediaType().equals(MediaType.VISUAL_NOVEL)) {
            if (Objects.equals(detailsViewModel.getEntityType(), "Char")) {
                detailsViewModel.getCharRoles();
            } else if (Objects.equals(detailsViewModel.getEntityType(), "Staff")){
                detailsViewModel.getStaffRoles();
            }
        }

        RelationsView relationsViewItem = (RelationsView) holder;
        MediaDetails relation = this.getRelation(position);
        Image.loadImage(this.context, relation.getImage(), relationsViewItem.coverImg);
        relationsViewItem.relation.setText(relation.getRelation());
        relationsViewItem.title.setText(relation.getTitles().getUserPref());
        relationsViewItem.formatAndStatus.setText(String.format("%s · %s", relation.getFormat(),
                this.detailsViewModel.getMediaType().equals(MediaType.VISUAL_NOVEL) ? relation.getStatus() : AnilistObjectMappings.mediaStatusToString.get(relation.getStatus())));
    }

    @Override
    public int getItemCount() {
        return relationsList.size();
    }

    private VNDetails copyVNDetails(VNDetails vnDetails) {
        VNDetails copyVNDetails = new VNDetails();
        copyVNDetails.setCoverImg(vnDetails.getImage());
        copyVNDetails.setTitles(vnDetails.getTitles());
        copyVNDetails.setFormat(vnDetails.getFormat());
        copyVNDetails.setStatus(vnDetails.getStatus());
        copyVNDetails.setId(vnDetails.getId());
        copyVNDetails.setType(vnDetails.getType());
        return copyVNDetails;
    }
    public void addRelations(List<? extends MediaDetails> newItems) {
        this.loading = true;
        if (detailsViewModel.getMediaType() == MediaType.VISUAL_NOVEL && Objects.equals(detailsViewModel.getEntityType(), "Staff")) {
            for (MediaDetails mediaDetails : newItems) {
                VNDetails vnDetails = (VNDetails) mediaDetails;
                if (vnDetails.getStaffs() != null && !vnDetails.getStaffs().isEmpty()) {
                    for (StaffDetails staff : vnDetails.getStaffs()) {
                        if (Objects.equals(staff.getId(), detailsViewModel.getId())) {
                            VNDetails copyVNDetails = this.copyVNDetails(vnDetails);
                            copyVNDetails.setRelation(staff.getRole());
                            relationsList.add(copyVNDetails);
                        }
                    }
                }
            }
        } else {
            relationsList.addAll(newItems);
        }


        notifyItemRangeInserted(relationsList.size()-newItems.size(), newItems.size());
        this.loading = false;
    }

    public MediaDetails getRelation(int pos){
        return relationsList.get(pos);
    }

    public static class RelationsView extends RecyclerView.ViewHolder {
        TextView relation, title, formatAndStatus;
        ImageView coverImg;
        public RelationsView(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            relation = itemView.findViewById(R.id.relation);
            title = itemView.findViewById(R.id.mediaTitle);
            formatAndStatus = itemView.findViewById(R.id.formatAndStatus);
            coverImg = itemView.findViewById(R.id.mediaImage);
            itemView.setOnClickListener(view -> {
                if (recyclerViewInterface != null) {
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onItemClick(pos);
                    }
                }
            });
        }
    }
}
