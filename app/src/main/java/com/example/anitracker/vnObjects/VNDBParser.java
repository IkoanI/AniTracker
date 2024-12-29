package com.example.anitracker.vnObjects;

public class VNDBParser {
    public static String parseText(String text) {
        return text.replaceAll("<","&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\\[url([^]]*)]", "<a href$1>")
                .replaceAll("\\[/url]", "</a>")
                .replaceAll("\\[b]([^\\[]*)\\[/b]", "<b>$1</b>");
    }
}
