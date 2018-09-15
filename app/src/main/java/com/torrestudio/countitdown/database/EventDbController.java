/**
 * This class is in charge of performing operations on the app's databae
 */

package com.torrestudio.countitdown.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import com.torrestudio.countitdown.entities.Event;

public class EventDbController  {

    private AppDatabase mAppDatabase;

    public EventDbController(Context context) {
        mAppDatabase = new AppDatabase(context);
    }

    public long insertEventRecord(Event event) {
        SQLiteDatabase writableDatabase = mAppDatabase.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventContract.Event.COLUMN_PHOTO_URI, event.getPhotoUri());
        values.put(EventContract.Event.COLUMN_NAME, event.getName());
        values.put(EventContract.Event.COLUMN_DATE, event.getDateTimeInMillis());
        values.put(EventContract.Event.COLUMN_CATEGORY, event.getCategory());

        long newRowId = writableDatabase.insert(EventContract.Event.TABLE_NAME, null, values);

        if (newRowId != -1) {
            return newRowId;
        }
        else {
            throw new SQLException("Error creating event with name: " + event.getName());
        }
    }

    public Cursor getAllEventRecords() {
        SQLiteDatabase readableDatabase = mAppDatabase.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                EventContract.Event.COLUMN_PHOTO_URI,
                EventContract.Event.COLUMN_NAME,
                EventContract.Event.COLUMN_DATE,
                EventContract.Event.COLUMN_CATEGORY
        };

        Cursor cursor = readableDatabase.query(
                EventContract.Event.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        return cursor;
    }

    public int deleteEventRecord(Event event) {
        String selection = EventContract.Event.COLUMN_NAME + " LIKE ? AND" +
                EventContract.Event.COLUMN_CATEGORY + " LIKE ?";

        String[] selectionArgs = {event.getName(), event.getCategory()};

        int deletedRow = mAppDatabase.getReadableDatabase().delete(
                EventContract.Event.TABLE_NAME, selection, selectionArgs);

        return deletedRow;
    }

}
