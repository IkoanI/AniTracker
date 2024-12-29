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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.animeObjects.Trailer;
import com.example.anitracker.mediaObjects.Description;
import com.example.anitracker.mediaObjects.Genres;
import com.example.anitracker.mediaObjects.Info;
import com.example.anitracker.mediaObjects.Tag;
import com.example.anitracker.uiObjects.AnitrackerLinkMovementMethod;
import com.example.anitracker.uiObjects.Header;
import com.example.anitracker.uiObjects.Image;
import com.example.anitracker.uiObjects.NoScrollTextView;
import com.example.anitracker.uiObjects.TagsHeader;
import com.example.anitracker.vnObjects.Screenshots;
import com.example.anitracker.vnObjects.VNLink;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.HeroCarouselStrategy;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OverviewViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Object> objectList;
    private final Context context;
    private final AnitrackerLinkMovementMethod linkClickHandler;

    private final int headerTypeVar = 0,
            descriptionTypeVar = 1,
            trailerTypeVar = 2,
            infoTypeVar = 3,
            genreListVar = 4,
            imageCarouselVar = 5,
            tagsHeaderVar = 6,
            tagsVar = 7,
            linksVar = 8;

    public OverviewViewAdapter(Context context) {
        this.objectList = new ArrayList<>();
        this.context = context;
        this.linkClickHandler = new AnitrackerLinkMovementMethod(this.context);
    }

    public void addObjects(List<Object> objects) {
        this.objectList.addAll(objects);
        notifyItemRangeChanged(0, objects.size());
    }

    @Override
    public int getItemViewType(int position) {
        Object object = objectList.get(position);
        if (object instanceof Header) {
            return headerTypeVar;
        } else if (object instanceof Description) {
            return descriptionTypeVar;
        } else if (object instanceof Trailer) {
            return trailerTypeVar;
        } else if (object instanceof Info) {
            return infoTypeVar;
        } else if (object instanceof Genres) {
            return genreListVar;
        } else if (object instanceof Screenshots) {
            return imageCarouselVar;
        } else if (object instanceof TagsHeader) {
            return tagsHeaderVar;
        } else if (object instanceof Tag) {
            return tagsVar;
        } else if (object instanceof VNLink) {
            return linksVar;
        }
        return -1;
    }

    public void setSpan (View view, float span) {
        ViewGroup.LayoutParams lp  = view.getLayoutParams();
        if (lp instanceof FlexboxLayoutManager.LayoutParams) {
            FlexboxLayoutManager.LayoutParams flexboxLp = (FlexboxLayoutManager.LayoutParams) lp;
            flexboxLp.setFlexBasisPercent(span);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
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

            case tagsHeaderVar:
                view = inflater.inflate(R.layout.tags_header_layout, parent, false);
                viewHolder = new TagsHeaderView(view);
                break;

            case tagsVar:
                view = inflater.inflate(R.layout.tag_view_layout, parent, false);
                setSpan(view, 0.5f);
                viewHolder = new TagsView(view);
                break;

            case linksVar:
                view = inflater.inflate(R.layout.text_view_layout, parent, false);
                setSpan(view, 0.5f);
                viewHolder = new LinkView(view);
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
                descriptionView.description.setMovementMethod(this.linkClickHandler);
                if (description.isExpanded()) {
                    descriptionView.setExpanded();
                } else {
                    descriptionView.setNotExpanded();
                }
                descriptionView.expandButton.setOnClickListener(v -> {
                    description.setExpanded(!description.isExpanded());
                    notifyItemChanged(position);
                });
                break;


            case trailerTypeVar:
                Trailer trailer = (Trailer) objectList.get(position);
                TrailerView trailerView = (TrailerView) holder;
                Image.loadImage(this.context, trailer.getThumbnail(), trailerView.trailerThumbnail);
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
                imageCarousel.setOnFlingListener(null);
                snapHelper.attachToRecyclerView(imageCarousel);
                break;

            case tagsHeaderVar:
                TagsHeaderView tagsHeaderView = (TagsHeaderView) holder;
                TagsHeader tagsHeader = (TagsHeader) objectList.get(position);
                // set up show spoiler tag button
                TextView showSpoilerTags = tagsHeaderView.showSpoilers;

                if (!tagsHeader.hasSpoilers()) {
                    // no spoiler tags, hide show spoiler button
                    showSpoilerTags.setVisibility(View.GONE);
                } else {
                    showSpoilerTags.setOnClickListener(view1 -> {
                        objectList.subList(position + 1, objectList.size()).clear();

                        if (tagsHeader.getSpoilersShown()) {
                            showSpoilerTags.setText(R.string.show_spoilers);
                            tagsHeader.setSpoilersShown(false);
                            objectList.addAll(tagsHeader.getNoSpoilerTags());
                        } else {
                            showSpoilerTags.setText(R.string.hide_spoilers);
                            tagsHeader.setSpoilersShown(true);
                            objectList.addAll(tagsHeader.getAllTags());
                        }

                        notifyItemRangeChanged(position + 1, objectList.size());
                    });
                }

                break;

            case tagsVar:
                TagsView tagsView = (TagsView) holder;
                Tag tag = (Tag) objectList.get(position);
                tagsView.tagName.setText(tag.getTagName());
                if (tag.getTagRanking() != 0) {
                    tagsView.tagRanking.setText(String.format(Locale.ENGLISH,"%d%%", tag.getTagRanking()));
                    tagsView.tagRanking.setVisibility(View.VISIBLE);
                } else {
                    tagsView.tagRanking.setVisibility(View.GONE);
                }

                if (tag.getSpoiler()) {
                    tagsView.tagName.setTextColor(ContextCompat.getColor(context, R.color.red));
                    tagsView.tagRanking.setTextColor(ContextCompat.getColor(context, R.color.red));
                }
                else {
                    tagsView.tagName.setTextColor(ContextCompat.getColor(context, R.color.white));
                    tagsView.tagRanking.setTextColor(ContextCompat.getColor(context, R.color.white));
                }
                break;

            case linksVar:
                LinkView linkView = (LinkView) holder;
                VNLink vnLink = (VNLink) objectList.get(position);
                linkView.link.setText(vnLink.getLink());
                linkView.link.setMovementMethod(this.linkClickHandler);
                break;
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
        NoScrollTextView description;
        ImageView expandButton;
        public DescriptionView(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.description);
            expandButton = itemView.findViewById(R.id.expand);
        }

        public void setExpanded() {
            this.expandButton.setImageResource(R.drawable.baseline_keyboard_arrow_up_24);
            this.description.setMaxLines(Integer.MAX_VALUE);
            this.description.setEllipsize(null);
        }

        public void setNotExpanded() {
            this.expandButton.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
            this.description.setMaxLines(3);
            this.description.setEllipsize(TextUtils.TruncateAt.END);
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

    public static class ImageCarouselView extends RecyclerView.ViewHolder {
        RecyclerView imageCarousel;
        public ImageCarouselView(@NonNull View itemView) {
            super(itemView);
            imageCarousel = itemView.findViewById(R.id.imageCarousel);
        }
    }

    public static class TagsHeaderView extends RecyclerView.ViewHolder {
        TextView tags, showSpoilers;
        public TagsHeaderView(@NonNull View itemView) {
            super(itemView);
            tags = itemView.findViewById(R.id.tags);
            showSpoilers = itemView.findViewById(R.id.showSpoilers);
        }
    }

    public class TagsView extends RecyclerView.ViewHolder {
        TextView tagName, tagRanking;
        public TagsView(@NonNull View itemView) {
            super(itemView);
            tagName = itemView.findViewById(R.id.tagName);
            tagRanking = itemView.findViewById(R.id.tagRanking);
            itemView.setOnClickListener(view -> Toast.makeText(context, tagName.getText(), Toast.LENGTH_SHORT).show());
        }
    }

    public static class LinkView extends RecyclerView.ViewHolder {
        TextView link;
        public LinkView(@NonNull View itemView) {
            super(itemView);
            link = itemView.findViewById(R.id.text);
        }
    }
}
