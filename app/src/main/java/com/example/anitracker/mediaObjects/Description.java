package com.example.anitracker.mediaObjects;

import android.text.Html;
import android.text.Spanned;

public class Description {
    private final Spanned description;
    private boolean expanded;

    public Description(String description){
        this.description = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY);
        this.expanded = false;
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public Spanned getDescription() {
        return description;
    }
}
