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
import com.example.anitracker.animeObjects.AnimeDetails;
import com.example.anitracker.R;
import com.example.anitracker.interfaces.RecyclerViewInterface;
import com.example.anitracker.viewModels.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AnimeSearchPageViewAdapter extends RecyclerView.Adapter<AnimeSearchPageViewAdapter.MyViewHolder> {
    private final Context context;
    private final List<AnimeDetails> animePage;
    private final RecyclerViewInterface recyclerViewInterface;
    private final MainViewModel viewModel;
    private Boolean loading = false;
    private String userSearch;

    public AnimeSearchPageViewAdapter(Context context,
                           RecyclerViewInterface recyclerViewInterface, MainViewModel viewModel) {
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
        this.viewModel = viewModel;
        this.animePage = new ArrayList<>();

    }

    public void addItems(List<AnimeDetails> newItems){
        loading = true;
        animePage.addAll(newItems);
        notifyItemRangeInserted(animePage.size()-newItems.size(), newItems.size());
        loading = false;
    }

    public void clearItems(){
        animePage.clear();
        notifyDataSetChanged();
    }

    public AnimeDetails getItem(int position){
        return animePage.get(position);
    }

    public void setUserSearch (String userSearch){
        this.userSearch = userSearch;
    }


    @NonNull
    @Override
    public AnimeSearchPageViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflates layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.anime_search_page_view, parent, false);
        return new AnimeSearchPageViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeSearchPageViewAdapter.MyViewHolder holder, int position) {
        if(!loading && position >= getItemCount()-1){
            if(userSearch == null || userSearch.isEmpty()){
                viewModel.getAnimePage();
            }
            else {
                viewModel.getAnimePage(userSearch);
            }
        }

        // assign value to each view created based on position of recycler view
        AnimeDetails animeDetails = animePage.get(position);
        Glide.with(context).load(animeDetails.getCoverImg()).into(holder.coverImg);
        holder.title.setText(animeDetails.getTitles().getUserPref());
        holder.rating.setText(String.format(Locale.ENGLISH, "%d%%", animeDetails.getAvgScore()));
        holder.rank.setText(String.valueOf(position+1));

        String airedSeason = "";
        if(animeDetails.getSeason() != null){
            airedSeason = animeDetails.getSeason();
        }
        String airedYear = "TBA";
        if(animeDetails.getStartDate() != null){
            airedYear = String.valueOf(animeDetails.getStartDate().getYear());
        }
        String airedSeasonAndYear = String.format(Locale.ENGLISH, "%s %s", airedSeason, airedYear).trim();
        if(animeDetails.getAiringSchedule() != null){
            // currently airing show, display time to next episode
            holder.seasonAndFormat.setText(String.format(Locale.ENGLISH,"%s · %s (Ep %d airs in %d days)",
                    airedSeasonAndYear, animeDetails.getFormat(), animeDetails.getAiringSchedule().getAiringEp(),
                    animeDetails.getAiringSchedule().daysToNextEp()));
        }
        else if(animeDetails.getEpisodes() == 1){
            // show with only 1 episode, display duration of episode in minutes
            holder.seasonAndFormat.setText(String.format("%s · %s (%s mins)",
                    airedSeasonAndYear, animeDetails.getFormat() , animeDetails.getDuration()));
        }
        else if (animeDetails.getStartDate() == null) {
            // show to be announced
            holder.seasonAndFormat.setText(String.format("%s · %s",
                    airedSeasonAndYear, animeDetails.getFormat()));
        }
        else{
            // completed show, display number of episodes
            holder.seasonAndFormat.setText(String.format(Locale.ENGLISH,"%s · %s (%d eps)",
                    airedSeasonAndYear, animeDetails.getFormat() ,animeDetails.getEpisodes()));
        }

        if(animeDetails.getGenres() != null){
            holder.genres.setText(animeDetails.getGenres().getGenreList().toString().replaceAll("[\\[\\]]",""));
            holder.genres.setVisibility(View.VISIBLE);
        }
        else {
            holder.genres.setVisibility(View.GONE);
        }

        holder.favorites.setText(String.valueOf(animeDetails.getFavorites()));

        if(animeDetails.getStudios() != null){
            holder.studio.setText(String.join(" · ",animeDetails.getStudios().getAnimationStudios()));
            holder.studio.setVisibility(View.VISIBLE);
        }
        else{
            holder.studio.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        // to know how many views you want displayed
        return animePage.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // grabs views from layout file
        ImageView coverImg;
        TextView title, rating, rank, seasonAndFormat, genres, studio, favorites;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            coverImg = itemView.findViewById(R.id.cover);
            title = itemView.findViewById(R.id.title);
            rating = itemView.findViewById(R.id.rating);
            rank = itemView.findViewById(R.id.rank);
            seasonAndFormat = itemView.findViewById(R.id.seasonAndFormat);
            studio = itemView.findViewById(R.id.studio);
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
