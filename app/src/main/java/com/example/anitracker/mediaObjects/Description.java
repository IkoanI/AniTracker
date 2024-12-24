package com.example.anitracker.mediaObjects;

import android.text.Spanned;

import androidx.core.text.HtmlCompat;

public class Description {
    private final Spanned description;
    private boolean expanded;

    public Description(String description) {
        this.description = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY);
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
