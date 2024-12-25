package com.example.anitracker.uiObjects;

import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

public class LinkClickHandler extends LinkMovementMethod {
    private static LinkClickHandler sInstance;

    public static LinkClickHandler getInstance() {
        if (sInstance == null)
            sInstance = new LinkClickHandler();
        return sInstance;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        Log.d("LINK CLICK TEST", buffer.toString());
        return super.onTouchEvent(widget, buffer, event);
    }
}
