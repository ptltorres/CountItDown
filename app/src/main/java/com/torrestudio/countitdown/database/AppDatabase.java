/**
 * This is the app's database. This class is supposed to only be used by
 * {@link com.torrestudio.countitdown.database.EventDbController}
 *
 * Created by P. Torres on 09/05/18
 */

package com.torrestudio.countitdown.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;

class AppDatabase extends SQLiteOpenHelper implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String SQL_CREATE_EVENT_TABLE =
            "CREATE TABLE " + EventContract.Event.TABLE_NAME + " (" +
             EventContract.Event._ID + " INTEGER PRIMARY KEY, " +
             EventContract.Event.COLUMN_PHOTO_URI + " TEXT, " +
             EventContract.Event.COLUMN_NAME + " TEXT, " +
             EventContract.Event.COLUMN_DATE + " INTEGER, " +
             EventContract.Event.COLUMN_CATEGORY + " TEXT)";

    public static final String SQL_DELETE_EVENT_TABLE =
            "DROP TABLE IF EXISTS " + EventContract.Event.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CountItDown.db";

    public AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_EVENT_TABLE);
        onCreate(db);
    }
}
