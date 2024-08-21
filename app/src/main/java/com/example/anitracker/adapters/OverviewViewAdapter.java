package com.example.anitracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anitracker.R;
import com.example.anitracker.animeObjects.Trailer;
import com.example.anitracker.mediaObjects.Description;
import com.example.anitracker.mediaObjects.Genres;
import com.example.anitracker.mediaObjects.Info;
import com.example.anitracker.uiObjects.Header;
import com.example.anitracker.vnObjects.Screenshots;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.FullScreenCarouselStrategy;
import com.google.android.material.carousel.HeroCarouselStrategy;


import java.util.List;

public class OverviewViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Object> objectList;
    Context context;
    private final int headerTypeVar = 0;
    private final int descriptionTypeVar = 1;
    private final int trailerTypeVar = 2;
    private final int infoTypeVar = 3;
    private final int genreListVar = 4;
    private final int imageCarouselVar = 5;



    public OverviewViewAdapter(List<Object> objectList, Context context) {
        this.objectList = objectList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(objectList.get(position) instanceof Header){
            return headerTypeVar;
        }
        else if(objectList.get(position) instanceof Description){
            return descriptionTypeVar;
        }
        else if(objectList.get(position) instanceof Trailer){
            return trailerTypeVar;
        }
        else if(objectList.get(position) instanceof Info){
            return infoTypeVar;
        }
        else if(objectList.get(position) instanceof Genres){
            return genreListVar;
        }
        else if(objectList.get(position) instanceof Screenshots){
            return imageCarouselVar;
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType){
            case headerTypeVar:
                view = inflater.inflate(R.layout.header_layout, parent, false );
                viewHolder = new HeaderView(view);
                break;

            case descriptionTypeVar:
                view = inflater.inflate(R.layout.description_view_layout, parent, false );
                viewHolder = new DescriptionView(view);
                break;

            case trailerTypeVar:
                view = inflater.inflate(R.layout.trailer_view_layout, parent, false );
                viewHolder = new TrailerView(view);
                break;

            case infoTypeVar:
                view = inflater.inflate(R.layout.info_view_layout, parent, false );
                viewHolder = new InfoView(view);
                break;

            case genreListVar:
                view = inflater.inflate(R.layout.genre_recycler_view, parent, false);
                viewHolder = new GenreView(view);
                break;

            case imageCarouselVar:
                view = inflater.inflate(R.layout.image_carousel, parent, false);
                viewHolder = new ImageCarouselView(view);
                break;
        }

        assert viewHolder != null;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case headerTypeVar:
                Header header = (Header) objectList.get(position);
                HeaderView headerView = (HeaderView) holder;
                headerView.headerText.setText(header.getHeader());
                break;

            case descriptionTypeVar:
                Description description = (Description) objectList.get(position);
                DescriptionView descriptionView = (DescriptionView) holder;
                descriptionView.description.setText(description.getDescription());
                descriptionView.description.setText(description.getDescription());
                descriptionView.expandButton.setOnClickListener(v -> {
                    if(!descriptionView.expanded){
                        // clicked when description not yet expanded
                        descriptionView.expandButton.setImageResource(R.drawable.baseline_keyboard_arrow_up_24);
                        descriptionView.description.setMaxLines(Integer.MAX_VALUE);
                        descriptionView.description.setEllipsize(null);
                        descriptionView.expanded = true;
                    }
                    else{
                        // clicked when description already expanded
                        descriptionView.expandButton.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                        descriptionView.description.setMaxLines(3);
                        descriptionView.description.setEllipsize(TextUtils.TruncateAt.END);
                        descriptionView.expanded = false;
                    }
                });
                break;


            case trailerTypeVar:
                Trailer trailer = (Trailer) objectList.get(position);
                TrailerView trailerView = (TrailerView) holder;
                Glide.with(context).load(trailer.getThumbnail()).into(trailerView.trailerThumbnail);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getTrailerLink()));
                trailerView.playButton.setOnClickListener(v -> context.startActivity(intent));
                break;


            case infoTypeVar:
                Info info = (Info) objectList.get(position);
                InfoView infoView = (InfoView) holder;
                infoView.desc.setText(info.getDescription());
                infoView.val.setText(info.getValue());
                break;

            case genreListVar:
                Genres genres = (Genres) objectList.get(position);
                GenreView genreView = (GenreView) holder;
                GenreViewAdapter genreViewAdapter = new GenreViewAdapter(genres.getGenreList(), context);
                RecyclerView genreRecView = genreView.genreRecView;
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                genreRecView.setAdapter(genreViewAdapter);
                genreRecView.setLayoutManager(linearLayoutManager);
                break;

            case imageCarouselVar:
                Screenshots screenshots = (Screenshots) objectList.get(position);
                ImageCarouselView imageCarouselView = (ImageCarouselView) holder;
                ImageCarouselAdapter imageCarouselAdapter = new ImageCarouselAdapter(screenshots.getScreenshotURLs(), context);
                RecyclerView imageCarousel = imageCarouselView.imageCarousel;
                imageCarousel.setAdapter(imageCarouselAdapter);
                CarouselLayoutManager carouselLayoutManager = new CarouselLayoutManager(new HeroCarouselStrategy());
                carouselLayoutManager.setCarouselAlignment(CarouselLayoutManager.ALIGNMENT_CENTER);
                imageCarousel.setLayoutManager(carouselLayoutManager);
                CarouselSnapHelper snapHelper = new CarouselSnapHelper();
                snapHelper.attachToRecyclerView(imageCarousel);

        }

    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    public static class HeaderView extends RecyclerView.ViewHolder{
        TextView headerText;
        public HeaderView(@NonNull View itemView) {
            super(itemView);
            headerText = itemView.findViewById(R.id.header);
        }
    }

    public static class DescriptionView extends RecyclerView.ViewHolder{
        TextView description;
        ImageView expandButton;
        Boolean expanded = false;
        public DescriptionView(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.description);
            expandButton = itemView.findViewById(R.id.expand);
        }
    }

    public static class TrailerView extends RecyclerView.ViewHolder{
        ImageView trailerThumbnail, playButton;
        public TrailerView(@NonNull View itemView) {
            super(itemView);
            trailerThumbnail = itemView.findViewById(R.id.thumbnail);
            playButton = itemView.findViewById(R.id.playButton);
        }
    }

    public static class InfoView extends RecyclerView.ViewHolder{
        TextView desc, val;
        public InfoView(@NonNull View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.description);
            val = itemView.findViewById(R.id.value);

        }
    }

    public static class GenreView extends RecyclerView.ViewHolder{
        RecyclerView genreRecView;
        public GenreView(@NonNull View itemView) {
            super(itemView);
            genreRecView = itemView.findViewById(R.id.genreView);
        }
    }

    public static class ImageCarouselView extends RecyclerView.ViewHolder{
        RecyclerView imageCarousel;
        public ImageCarouselView(@NonNull View itemView) {
            super(itemView);
            imageCarousel = itemView.findViewById(R.id.imageCarousel);
        }
    }
}
