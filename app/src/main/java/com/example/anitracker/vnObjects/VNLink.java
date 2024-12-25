package com.example.anitracker.vnObjects;

import android.text.Spanned;
import android.util.Log;

import androidx.core.text.HtmlCompat;

import com.google.gson.annotations.SerializedName;

public class VNLink {
    @SerializedName("label")
    private String label;
    @SerializedName("url")
    private String url;

    public String getLabel() {
        return label;
    }

    public String getUrl() {
        return url;
    }

    public Spanned getLink() {
        String link = String.format("<a href='%s'>%s</a>", this.getUrl(), this.getLabel());
        return HtmlCompat.fromHtml(link, HtmlCompat.FROM_HTML_MODE_LEGACY);
    }
}
