package com.torrestudio.countitdown.controllers;

import android.content.Context;
import android.database.Cursor;

import com.torrestudio.countitdown.database.EventContract;
import com.torrestudio.countitdown.database.EventDbController;
import com.torrestudio.countitdown.entities.Event;
import com.torrestudio.countitdown.interfaces.EventDataSubscriber;

import java.util.ArrayList;

public class EventDataController {

    private Context mContext;
    private EventDbController mDbController;

    // List of events in the database. All instances will share all the events.
    private static ArrayList<Event> mAllEvents;
    // Subscriber to be notified when new data is added. Subscriber may be replaced;
    private static ArrayList<EventDataSubscriber> mSubscribers;

    public static EventDataController initController(Context context) {
        return new EventDataController(context);
    }

    private EventDataController(Context context) {
        mContext = context;
        mDbController = new EventDbController(mContext);

        if (mAllEvents == null)
            mAllEvents = getAllEventsFromDb();

        if (mSubscribers == null)
            mSubscribers = new ArrayList<>();
    }

    private ArrayList<Event> getAllEventsFromDb() {
        ArrayList<Event> events = new ArrayList<>();

        Cursor cursor = mDbController.getAllEventRecords();
        while (cursor.moveToNext()) {
            String photoUri = cursor.getString(cursor.getColumnIndex(EventContract.Event.COLUMN_PHOTO_URI));
            String name = cursor.getString(cursor.getColumnIndex(EventContract.Event.COLUMN_NAME));
            long dateTime = cursor.getLong(cursor.getColumnIndex(EventContract.Event.COLUMN_DATE));
            String category = cursor.getString(cursor.getColumnIndex(EventContract.Event.COLUMN_CATEGORY));
            events.add(new Event(photoUri, name, dateTime, category));
        }
        return events;
    }

    public void createEvent(Event e) {
        mDbController.insertEventRecord(e);
        mAllEvents.add(e);

        for (EventDataSubscriber sub : mSubscribers)
            sub.onEventCreated(e);
    }

    public ArrayList<Event> getAllEvents() {
        return mAllEvents;
    }

    public static void subscribe(EventDataSubscriber subscriber) {
        mSubscribers.add(subscriber);
    }
}
