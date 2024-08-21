package com.example.anitracker.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.anitracker.R;

import java.util.List;

public class ImageCarouselAdapter extends RecyclerView.Adapter<ImageCarouselAdapter.MyViewHolder> {
    Context context;
    List<String> images;
    CircularProgressDrawable loadingCircle;

    public ImageCarouselAdapter(List<String> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageCarouselAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.image_carousel_item, parent, false);
        loadingCircle = new CircularProgressDrawable(context);
        loadingCircle.setStrokeWidth(5);
        loadingCircle.setCenterRadius(30);
        loadingCircle.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_IN);
        loadingCircle.start();
        return new ImageCarouselAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageCarouselAdapter.MyViewHolder holder, int position) {
        Glide.with(context).load(images.get(position)).placeholder(loadingCircle).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
