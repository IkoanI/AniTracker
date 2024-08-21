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
import com.example.anitracker.interfaces.RecyclerViewInterface;
import com.example.anitracker.mangaObjects.MangaDetails;
import com.example.anitracker.repository.AnilistObjectMappings;
import com.example.anitracker.type.MediaStatus;
import com.example.anitracker.viewModels.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MangaSearchPageViewAdapter extends RecyclerView.Adapter<MangaSearchPageViewAdapter.MyViewHolder> {
    private final Context context;
    private final List<MangaDetails> mangaPage;
    private final RecyclerViewInterface recyclerViewInterface;
    private final MainViewModel viewModel;
    private Boolean loading = false;
    private String userSearch;

    public MangaSearchPageViewAdapter(Context context,
                           RecyclerViewInterface recyclerViewInterface, MainViewModel viewModel) {
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
        this.viewModel = viewModel;
        this.mangaPage = new ArrayList<>();

    }

    public void addItems(List<MangaDetails> newItems){
        loading = true;
        mangaPage.addAll(newItems);
        notifyItemRangeInserted(mangaPage.size()-newItems.size(), newItems.size());
        loading = false;
    }

    public void clearItems(){
        mangaPage.clear();
        notifyDataSetChanged();
    }

    public MangaDetails getItem(int position){
        return mangaPage.get(position);
    }

    public void setUserSearch (String userSearch){
        this.userSearch = userSearch;
    }


    @NonNull
    @Override
    public MangaSearchPageViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflates layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.manga_search_page_view, parent, false);
        return new MangaSearchPageViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaSearchPageViewAdapter.MyViewHolder holder, int position) {
        if(!loading && position >= getItemCount()-1){
            if(userSearch == null || userSearch.isEmpty()){
                viewModel.getMangaPage();
            }
            else {
                viewModel.getMangaPage(userSearch);
            }
        }

        // assign value to each view created based on position of recycler view
        MangaDetails mangaDetails = mangaPage.get(position);
        Glide.with(context).load(mangaDetails.getCoverImg()).into(holder.coverImg);
        holder.title.setText(mangaDetails.getTitles().getUserPref());
        holder.rating.setText(String.format(Locale.ENGLISH, "%d%%", mangaDetails.getAvgScore()));
        holder.rank.setText(String.valueOf(position+1));

        String airedYears = "TBA";
        if(mangaDetails.getStartDate() != null){
            airedYears = String.valueOf(mangaDetails.getStartDate().getYear());
        }

        if(mangaDetails.getEndDate() != null){
            airedYears += " - ";
            airedYears += mangaDetails.getEndDate().getYear();
        }

        String volumes = "?";
        if(mangaDetails.getVolumes() != 0){
            volumes = String.valueOf(mangaDetails.getVolumes());
        }

        if(Objects.equals(mangaDetails.getStatus(), AnilistObjectMappings.mediaStatusToString.get(MediaStatus.FINISHED))){
            // finished manga, display volumes
            holder.seasonAndFormat.setText(String.format("%s · %s (%s vols)", airedYears, mangaDetails.getFormat(), volumes));
        }
        else{
            // unfinished manga, display status
            holder.seasonAndFormat.setText(String.format("%s · %s (%s)", airedYears, mangaDetails.getFormat(), mangaDetails.getStatus()));
        }

        if(mangaDetails.getGenres() != null){
            holder.genres.setText(mangaDetails.getGenres().getGenreList().toString().replaceAll("[\\[\\]]",""));
            holder.genres.setVisibility(View.VISIBLE);
        }
        else {
            holder.genres.setVisibility(View.GONE);
        }

        holder.favorites.setText(String.valueOf(mangaDetails.getFavorites()));
    }

    @Override
    public int getItemCount() {
        // to know how many views you want displayed
        return mangaPage.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // grabs views from layout file
        ImageView coverImg;
        TextView title, rating, rank, seasonAndFormat, genres, favorites;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            coverImg = itemView.findViewById(R.id.cover);
            title = itemView.findViewById(R.id.title);
            rating = itemView.findViewById(R.id.rating);
            rank = itemView.findViewById(R.id.rank);
            seasonAndFormat = itemView.findViewById(R.id.seasonAndFormat);
            genres = itemView.findViewById(R.id.genres);
            favorites = itemView.findViewById(R.id.favorites);
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
