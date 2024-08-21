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
import com.example.anitracker.viewModels.MainViewModel;
import com.example.anitracker.vnObjects.Developer;
import com.example.anitracker.vnObjects.VNDetails;
import com.example.anitracker.vnObjects.VNResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VNSearchViewAdapter extends RecyclerView.Adapter<VNSearchViewAdapter.MyViewHolder> {
    private final Context context;
    private final List<VNResponse> vnPage;
    private final RecyclerViewInterface recyclerViewInterface;
    private final MainViewModel viewModel;
    private Boolean loading = false;
    private String userSearch;
    private Boolean hasMore = true;

    public VNSearchViewAdapter(Context context,
                               RecyclerViewInterface recyclerViewInterface, MainViewModel viewModel) {
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
        this.viewModel = viewModel;
        this.vnPage = new ArrayList<>();

    }

    public void addItems(List<VNResponse> newItems){
        loading = true;
        vnPage.addAll(newItems);
        notifyItemRangeInserted(vnPage.size()-newItems.size(), newItems.size());
        loading = false;
    }

    public void clearItems(){
        vnPage.clear();
        notifyDataSetChanged();
    }

    public VNResponse getItem(int position){
        return vnPage.get(position);
    }

    public void setUserSearch (String userSearch){
        this.userSearch = userSearch;
    }

    public void setHasMore (boolean hasMore) { this.hasMore = hasMore; }


    @NonNull
    @Override
    public VNSearchViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflates layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.vn_search_view, parent, false);
        return new VNSearchViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull VNSearchViewAdapter.MyViewHolder holder, int position) {
        if(!loading && position >= getItemCount()-1 && hasMore){
            String fields = "title, image{thumbnail}, developers{name}, released, length, length_minutes, rating, id";
            if(userSearch == null || userSearch.isEmpty()){
                viewModel.getVNPage("rating", fields);
            }
            else {
                viewModel.getVNPage(userSearch, "searchrank", fields);
            }
        }

        // assign value to each view created based on position of recycler view
        VNResponse vnDetails = vnPage.get(position);
        Glide.with(context).load(vnDetails.getImage().getThumbnail()).into(holder.coverImg);
        holder.title.setText(vnDetails.getTitle());
        holder.rating.setText(String.format(Locale.ENGLISH, "%d%%", vnDetails.getRating()));
        holder.rank.setText(String.valueOf(position+1));

        String yearAndPlayTime = "Unknown";
        if(vnDetails.getReleased() != null){
            if(vnDetails.getReleased().length() >= 4){
                // display year of release
                yearAndPlayTime = vnDetails.getReleased().substring(0, 4);
            }
            else{
                // display whatever status given
                yearAndPlayTime = vnDetails.getReleased();
            }
        }

        if(vnDetails.getLength() != null){
            // display play time
            yearAndPlayTime += String.format(Locale.ENGLISH, " · %s", vnDetails.getLength());
        }
        holder.yearAndPlayTime.setText(yearAndPlayTime);


        if(!vnDetails.getDevelopers().isEmpty()){
            List<String> developers = new ArrayList<>();
            for(Developer developer : vnDetails.getDevelopers()){
                developers.add(developer.getName());
            }
            holder.developers.setText(String.join(" · ", developers));
        }
    }

    @Override
    public int getItemCount() {
        // to know how many views you want displayed
        return vnPage.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // grabs views from layout file
        ImageView coverImg;
        TextView title, rating, rank, yearAndPlayTime, developers;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            coverImg = itemView.findViewById(R.id.cover);
            title = itemView.findViewById(R.id.title);
            rating = itemView.findViewById(R.id.rating);
            rank = itemView.findViewById(R.id.rank);
            yearAndPlayTime = itemView.findViewById(R.id.yearAndPlayTime);
            developers = itemView.findViewById(R.id.developers);
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
