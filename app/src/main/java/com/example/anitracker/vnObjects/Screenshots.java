package com.example.anitracker.vnObjects;

import java.util.List;

public class Screenshots {
    List<String> screenshotURLs;

    public Screenshots(List<String> screenshotURLs) {
        this.screenshotURLs = screenshotURLs;
    }

    public List<String> getScreenshotURLs() {
        return screenshotURLs;
    }

    public void setScreenshotURLs(List<String> screenshotURLs) {
        this.screenshotURLs = screenshotURLs;
    }
}
