package com.torrestudio.countitdown.database;

import android.provider.BaseColumns;

public class EventContract {

    // Private constructor to prevent instantiation of this class
    private EventContract() {}

    // Table definition
    public static class Event implements BaseColumns {
        public static final String TABLE_NAME = "event";
        public static final String COLUMN_PHOTO_URI = "uri";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_CATEGORY = "category";
    }
}
