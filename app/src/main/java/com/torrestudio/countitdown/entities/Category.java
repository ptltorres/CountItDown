/**
 * This enumerated type represents the categories for an event. They're defined in the order they
 * appear in the navigation drawer and the spinner in the creation activity. They're arranged
 * alphabetically (english).
 *
 * Author: P. Torres
 * Last Updated: 10/20/2018
 */
package com.torrestudio.countitdown.entities;

import android.support.annotation.StringRes;

import com.torrestudio.countitdown.R;

public enum Category {
    BUSINESS (R.string.category_business),
    EDUCATION (R.string.category_education),
    LEISURE (R.string.category_leisure),
    SPECIAL (R.string.category_special),
    SPORTS (R.string.category_sports),
    ALL_EVENTS (R.string.category_all_events),
    PAST_EVENTS (R.string.category_past_events);

    private int resourceId;

    Category(int resourceId) {
        this.resourceId = resourceId;
    }

    @StringRes
    public int getResourceId() {
        return resourceId;
    }

}
