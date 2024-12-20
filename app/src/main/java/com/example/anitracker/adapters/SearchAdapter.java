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
import com.example.anitracker.animeObjects.AnimeDetails;
import com.example.anitracker.interfaces.RecyclerViewInterface;
import com.example.anitracker.mangaObjects.MangaDetails;
import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.repository.AnilistObjectMappings;
import com.example.anitracker.type.MediaStatus;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.uiObjects.LoadingCircleDrawable;
import com.example.anitracker.viewModels.SearchViewModel;
import com.example.anitracker.vnObjects.Developer;
import com.example.anitracker.vnObjects.VNDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private final Context context;
    private final List<MediaDetails> resultPage = new ArrayList<>();
    private final RecyclerViewInterface recyclerViewInterface;
    private final SearchViewModel viewModel;
    private Boolean loading = false;
    private final MediaType mediaType;

    public SearchAdapter(MediaType mediaType, Context context, RecyclerViewInterface recyclerViewInterface, SearchViewModel viewModel) {
        this.mediaType = mediaType;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
        this.viewModel = viewModel;
    }

    public void addItems(List<? extends MediaDetails> newItems) {
        loading = true;
        resultPage.addAll(newItems);
        notifyItemRangeInserted(resultPage.size()-newItems.size(), newItems.size());
        loading = false;
    }

    public void clearItems() {
        notifyItemRangeRemoved(0, resultPage.size());
        resultPage.clear();
    }

    public MediaDetails getItem(int position) {
        return resultPage.get(position);
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflates layout
        LayoutInflater inflater = LayoutInflater.from(context);

        if (this.mediaType == MediaType.ANIME) {
            View view = inflater.inflate(R.layout.anime_card, parent, false);
            return new AnimeSearchViewHolder(view, recyclerViewInterface);
        } else if (this.mediaType == MediaType.MANGA) {
            View view = inflater.inflate(R.layout.manga_card, parent, false);
            return new MangaSearchViewHolder(view, recyclerViewInterface);
        } else if (this.mediaType == MediaType.VISUAL_NOVEL) {
            View view = inflater.inflate(R.layout.vn_card, parent, false);
            return new VNSearchViewHolder(view, recyclerViewInterface);
        } else {
            //TODO implement default search card
            View view = inflater.inflate(R.layout.anime_card, parent, false);
            return new SearchViewHolder(view, recyclerViewInterface);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        if (!loading && position >= getItemCount()-1) {
            viewModel.getSearchPage(this.mediaType);
        }

        // assign value to each view created based on position of recycler view
        MediaDetails details = resultPage.get(position);

        this.setDetails(details, holder, position);

        if (details instanceof AnimeDetails) {
            setAnimeDetails((AnimeDetails) details, (AnimeSearchViewHolder) holder);
        } else if (details instanceof MangaDetails) {
            setMangaDetails((MangaDetails) details, (MangaSearchViewHolder) holder);
        } else if (details instanceof VNDetails) {
            setVNDetails((VNDetails) details, (VNSearchViewHolder) holder);
        }
    }

    public void setDetails(MediaDetails details, SearchViewHolder holder, int position) {
        Glide.with(context)
                .load(details.getCoverImg())
                .placeholder(LoadingCircleDrawable.getLoadingCircle(context))
                .into(holder.coverImg);
        holder.title.setText(details.getTitles().getUserPref());
        holder.rating.setText(String.format(Locale.ENGLISH, "%d%%", details.getAvgScore()));
        holder.rank.setText(String.valueOf(position+1));
    }

    public void setAnimeDetails(AnimeDetails animeDetails, AnimeSearchViewHolder holder) {
        String airedSeason = "";
        if (animeDetails.getSeason() != null) {
            airedSeason = animeDetails.getSeason();
        }

        String airedYear = "TBA";
        if (animeDetails.getStartDate() != null) {
            airedYear = String.valueOf(animeDetails.getStartDate().getYear());
        }
        String airedSeasonAndYear = String.format(Locale.ENGLISH, "%s %s", airedSeason, airedYear).trim();

        if (animeDetails.getAiringSchedule() != null) {
            // currently airing show, display time to next episode
            holder.seasonAndFormat.setText(String.format(Locale.ENGLISH,"%s · %s (Ep %d airs in %d days)",
                    airedSeasonAndYear, animeDetails.getFormat(), animeDetails.getAiringSchedule().getAiringEp(),
                    animeDetails.getAiringSchedule().daysToNextEp()));
        } else if(animeDetails.getEpisodes() == 1) {
            // show with only 1 episode, display duration of episode in minutes
            holder.seasonAndFormat.setText(String.format("%s · %s (%s mins)",
                    airedSeasonAndYear, animeDetails.getFormat() , animeDetails.getDuration()));
        } else if (animeDetails.getStartDate() == null) {
            // show to be announced
            holder.seasonAndFormat.setText(String.format("%s · %s",
                    airedSeasonAndYear, animeDetails.getFormat()));
        } else {
            // completed show, display number of episodes
            holder.seasonAndFormat.setText(String.format(Locale.ENGLISH,"%s · %s (%d eps)",
                    airedSeasonAndYear, animeDetails.getFormat() ,animeDetails.getEpisodes()));
        }

        if (animeDetails.getStudios() != null) {
            holder.studio.setText(String.join(" · ",animeDetails.getStudios().getAnimationStudios()));
            holder.studio.setVisibility(View.VISIBLE);
        } else {
            holder.studio.setVisibility(View.GONE);
        }

        if (animeDetails.getGenres() != null) {
            holder.genres.setText(animeDetails.getGenres().getGenreList().toString().replaceAll("[\\[\\]]",""));
            holder.genres.setVisibility(View.VISIBLE);
        } else {
            holder.genres.setVisibility(View.GONE);
        }
        holder.favorites.setText(String.valueOf(animeDetails.getFavorites()));
    }

    public void setMangaDetails(MangaDetails mangaDetails, MangaSearchViewHolder holder) {
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

        if(Objects.equals(mangaDetails.getStatus(), AnilistObjectMappings.mediaStatusToString.get(MediaStatus.FINISHED.rawValue))){
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

    public void setVNDetails(VNDetails vnDetails, VNSearchViewHolder holder) {
        String yearAndPlayTime = "Unknown";
        if (vnDetails.getStartDate() != null) {
            if (vnDetails.getStartDate().getYear() != 0) {
                // display year of release
                yearAndPlayTime = String.valueOf(vnDetails.getStartDate().getYear());
            }
            else{
                // display whatever status given
                yearAndPlayTime = vnDetails.getStatus();
            }
        }

        if (vnDetails.getLength() != null) {
            // display play time
            yearAndPlayTime += String.format(Locale.ENGLISH, " · %s", vnDetails.getLength());
        }
        holder.yearAndPlayTime.setText(yearAndPlayTime);


        if (vnDetails.getDevelopers() != null && !vnDetails.getDevelopers().isEmpty()) {
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
        return resultPage.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        // grabs views from layout file
        ImageView coverImg;
        TextView title, rating, rank;
        public SearchViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            coverImg = itemView.findViewById(R.id.cover);
            title = itemView.findViewById(R.id.title);
            rating = itemView.findViewById(R.id.rating);
            rank = itemView.findViewById(R.id.rank);
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

    public static class AnimeSearchViewHolder extends SearchViewHolder {
        TextView seasonAndFormat, genres, studio, favorites;
        public AnimeSearchViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView, recyclerViewInterface);
            seasonAndFormat = itemView.findViewById(R.id.seasonAndFormat);
            studio = itemView.findViewById(R.id.studio);
            genres = itemView.findViewById(R.id.genres);
            favorites = itemView.findViewById(R.id.favorites);
        }
    }

    public static class MangaSearchViewHolder extends SearchViewHolder {
        TextView seasonAndFormat, genres, favorites;
        public MangaSearchViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView, recyclerViewInterface);
            seasonAndFormat = itemView.findViewById(R.id.seasonAndFormat);
            genres = itemView.findViewById(R.id.genres);
            favorites = itemView.findViewById(R.id.favorites);
        }
    }

    public static class VNSearchViewHolder extends SearchViewHolder{
        // grabs views from layout file
        TextView yearAndPlayTime, developers;
        public VNSearchViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView, recyclerViewInterface);
            yearAndPlayTime = itemView.findViewById(R.id.yearAndPlayTime);
            developers = itemView.findViewById(R.id.developers);
        }
    }
}
