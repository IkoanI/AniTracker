package com.example.anitracker.vnObjects;

public class VNDBParser {
    public static String parseText(String text) {
        return text.replaceAll("<","&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\n", "<br>")
                .replaceAll("\\[url([^]]*)]", "<a href$1>")
                .replaceAll("\\[/url]", "</a>")
                .replaceAll("\\[b]([^\\[]*)\\[/b]", "<b>$1</b>")
                .replaceAll("\\[i]([^\\[]*)\\[/i]", "<i>$1</i>")
                .replaceAll("\\[u]([^\\[]*)\\[/u]", "<u>$1</u>")
                .replaceAll("\\[s]([^\\[]*)\\[/s]", "<s>$1</s>");
    }
}
