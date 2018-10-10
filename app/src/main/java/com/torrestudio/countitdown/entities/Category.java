package com.torrestudio.countitdown.entities;

import android.support.annotation.StringRes;

import com.torrestudio.countitdown.R;

public enum Category {
    ALL_EVENTS (R.string.category_all_events),
    PAST_EVENTS (R.string.category_past_events),
    BUSINESS (R.string.category_business),
    EDUCATION (R.string.category_education),
    LEISURE (R.string.category_leisure),
    SPECIAL (R.string.category_special),
    SPORTS (R.string.category_sports);

    private int resourceId;

    Category(int resourceId) {
        this.resourceId = resourceId;
    }

    @StringRes
    public int getResourceId() {
        return resourceId;
    }
}
