package com.example.anitracker.uiObjects;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.anitracker.activities.Details;
import com.example.anitracker.type.MediaType;

import java.util.Arrays;
import java.util.Objects;

public class AnitrackerLinkMovementMethod extends LinkMovementMethod {
    private final Context context;

    public AnitrackerLinkMovementMethod(Context context) {
        this.context = context;
    }

    public boolean onTouchEvent(TextView widget, android.text.Spannable buffer, MotionEvent event) {
        int action = event.getAction();
        //http://stackoverflow.com/questions/1697084/handle-textview-link-click-in-my-android-app
        if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);
            if (link.length != 0) {
                String url = link[0].getURL();
                String[] linkData = url.split("/");
                if (linkData.length >= 5 && Objects.equals(linkData[2], "anilist.co")) {
                    Intent intent = this.getAnilistIntent(linkData);
                    context.startActivity(intent);
                } else if (linkData.length == 2 || (linkData.length >= 4 && Objects.equals(linkData[2], "vndb.org"))) {
                    Intent intent = this.getVNDBIntent(linkData);
                    context.startActivity(intent);
                }
                else {
                    return super.onTouchEvent(widget, buffer, event);
                }
            }

        }
        return true;
    }

    public Intent getAnilistIntent(String[] linkData) {
        Intent intent = new Intent(context, Details.class);
        intent.putExtra("ID", linkData[4]);
        if (Objects.equals(linkData[3], "character")) {
            intent.putExtra("Type", MediaType.ANIME.rawValue);
            intent.putExtra("Entity", "Char");
        } else if (Objects.equals(linkData[3], "staff")) {
            intent.putExtra("Type", MediaType.ANIME.rawValue);
            intent.putExtra("Entity", "Staff");
        } else if (Objects.equals(linkData[3], "anime")) {
            intent.putExtra("Type", MediaType.ANIME.rawValue);
        } else if (Objects.equals(linkData[3], "manga")) {
            intent.putExtra("Type", MediaType.MANGA.rawValue);
        }

        return intent;
    }

    public Intent getVNDBIntent(String[] linkData) {
        Intent intent = new Intent(context, Details.class);
        String id = linkData[linkData.length - 1];
        intent.putExtra("Type", MediaType.VISUAL_NOVEL.rawValue);
        intent.putExtra("ID", id);
        if (Objects.equals(id.charAt(0), 'c')) {
            intent.putExtra("Entity", "Char");
        } else if (Objects.equals(id.charAt(0), 's')) {
            intent.putExtra("Entity", "Staff");
        }

        return intent;
    }
}
