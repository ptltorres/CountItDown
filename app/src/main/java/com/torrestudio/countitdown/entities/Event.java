package com.torrestudio.countitdown.entities;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.io.Serializable;

public class Event implements Serializable, Comparable<Event> {
    private static final long serialVersionUID = 1L;

    private transient Bitmap mPhotoBitmap;
    private String mPhotoUri;
    private String mName;
    private long mDateTimeInMillis;
    private String mCategory;
    private DateDifference mDateDifference;

    public Event(String photoUri, String name, long dateTimeInMillis, String category) {
        this.mPhotoUri = photoUri;
        this.mName = name;
        this.mDateTimeInMillis = dateTimeInMillis;
        this.mCategory = category;
        mDateDifference = new DateDifference(mDateTimeInMillis);
    }

    public Bitmap getPhotoBitmap() {
        return mPhotoBitmap;
    }

    public void setPhotoBitmap(Bitmap photoBitmap) {
        this.mPhotoBitmap = photoBitmap;
    }

    public String getPhotoUri() {
        return mPhotoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.mPhotoUri = photoUri;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public long getDateTimeInMillis() {
        return mDateTimeInMillis;
    }

    public void setDateTimeInMillis(long dateTimeInMillis) {
        this.mDateTimeInMillis = dateTimeInMillis;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        this.mCategory = category;
    }

    public DateDifference getDateDifference() {
        return mDateDifference;
    }

    public boolean isPastEvent() {
        return System.currentTimeMillis() > mDateTimeInMillis;
    }

    @Override
    public int compareTo(@NonNull Event compareEvent) {
        return Long.compare(mDateTimeInMillis, compareEvent.mDateTimeInMillis);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Event) {
            return this.mName.equals(((Event) obj).getName()) && this.mDateTimeInMillis == ((Event) obj).getDateTimeInMillis();
        }
        return false;
    }

    @Override
    public String toString() {
        return "Event{" +
                "mPhotoBitmap=" + mPhotoBitmap +
                ", mPhotoUri='" + mPhotoUri + '\'' +
                ", mName='" + mName + '\'' +
                ", mDateTimeInMillis=" + mDateTimeInMillis +
                ", mCategory='" + mCategory + '\'' +
                '}';
    }
}
