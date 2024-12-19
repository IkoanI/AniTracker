package com.example.anitracker.uiObjects;

import android.content.Context;
import android.graphics.PorterDuff;

import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.example.anitracker.R;

public class LoadingCircleDrawable {
    public static CircularProgressDrawable getLoadingCircle(Context context) {
        CircularProgressDrawable loadingCircle = new CircularProgressDrawable(context);
        loadingCircle.setStrokeWidth(5);
        loadingCircle.setCenterRadius(30);
        loadingCircle.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_IN);
        loadingCircle.start();
        return loadingCircle;
    }
}
