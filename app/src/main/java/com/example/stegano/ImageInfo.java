package com.example.stegano;

import android.graphics.Bitmap;

public class ImageInfo {

    private String url;
    private String description;
    private Bitmap image;

    public ImageInfo(String url, String description, Bitmap image) {
        this.url = url;
        this.description = description;
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public Bitmap getImage() {
        return  image;
    }

    public String getDescription() { return description; }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
