package com.example.anitracker.uiObjects;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Image {
    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context.getApplicationContext())
                .load(url)
                .placeholder(LoadingCircleDrawable.getLoadingCircle(context))
                .into(imageView);
    }
}
