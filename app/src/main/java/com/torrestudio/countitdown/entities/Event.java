package com.torrestudio.countitdown.entities;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Event implements Serializable {
    private static final long serialVersionUID = 1L;

    private Bitmap photoBitmap;
    private String photoUri;
    private String name;
    private long dateTimeInMillis;
    private String category;

    public Event(String photoUri, String name, long dateTimeInMillis, String category) {
        this.photoUri = photoUri;
        this.name = name;
        this.dateTimeInMillis = dateTimeInMillis;
        this.category = category;
    }

    public Bitmap getPhotoBitmap() {
        return photoBitmap;
    }

    public void setPhotoBitmap(Bitmap photoBitmap) {
        this.photoBitmap = photoBitmap;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDateTimeInMillis() {
        return dateTimeInMillis;
    }

    public void setDateTimeInMillis(long dateTimeInMillis) {
        this.dateTimeInMillis = dateTimeInMillis;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Event{" +
                "photoBitmap=" + photoBitmap +
                ", photoUri='" + photoUri + '\'' +
                ", name='" + name + '\'' +
                ", dateTimeInMillis=" + dateTimeInMillis +
                ", category='" + category + '\'' +
                '}';
    }
}
