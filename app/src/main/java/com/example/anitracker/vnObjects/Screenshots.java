package com.example.anitracker.vnObjects;

import java.util.ArrayList;
import java.util.List;

public class Screenshots {
    private final List<VNImage> screenshots;

    public Screenshots(List<VNImage> screenshots) {
        this.screenshots = screenshots;
    }

    public List<String> getScreenshotURLs() {
        if (this.screenshots != null && !this.screenshots.isEmpty()) {
            List<String> screenshotURLs = new ArrayList<>();
            for (VNImage screenshot : this.screenshots) {
                screenshotURLs.add(screenshot.getUrl());
            }
            return screenshotURLs;
        }
        return null;
    }
}
