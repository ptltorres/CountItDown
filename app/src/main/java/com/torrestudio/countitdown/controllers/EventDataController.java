package com.torrestudio.countitdown.controllers;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;

import com.torrestudio.countitdown.constants.Constant;
import com.torrestudio.countitdown.database.EventContract;
import com.torrestudio.countitdown.database.EventDbController;
import com.torrestudio.countitdown.entities.Category;
import com.torrestudio.countitdown.entities.Event;
import com.torrestudio.countitdown.interfaces.EventDataSubscriber;

import java.util.ArrayList;

public class EventDataController {

    private Context mContext;
    private EventDbController mDbController;

    // List of events in the database. All instances will share all the events.
    private static ArrayList<Event> sAllEvents = new ArrayList<>();
    private static ArrayList<Event> sFilteredEvents = new ArrayList<>();
    // Subscribers to be notified when new data is added.
    private static ArrayList<EventDataSubscriber> sSubscribers = new ArrayList<>();
    private static boolean isEventDataLoaded = false;

    public static EventDataController initController(Context context) {
        return new EventDataController(context);
    }

    private EventDataController(Context context) {
        mContext = context;
        mDbController = new EventDbController(mContext);
        if (!isEventDataLoaded) loadEvents();
    }

    private void loadEvents() {
        loadAllEventsFromDb();
    }

    private void loadAllEventsFromDb() {
        Cursor cursor = mDbController.getAllEventRecords();
        while (cursor.moveToNext()) {
            String photoUri = cursor.getString(cursor.getColumnIndex(EventContract.Event.COLUMN_PHOTO_URI));
            String name = cursor.getString(cursor.getColumnIndex(EventContract.Event.COLUMN_NAME));
            long dateTime = cursor.getLong(cursor.getColumnIndex(EventContract.Event.COLUMN_DATE));
            String category = cursor.getString(cursor.getColumnIndex(EventContract.Event.COLUMN_CATEGORY));
            Event event = new Event(photoUri, name, dateTime, category);
            event.setPhotoBitmap(getEventBitmapFromDevice(event));
            sAllEvents.add(event);
        }
        sFilteredEvents.addAll(sAllEvents);
        isEventDataLoaded = true;
    }

    public void createEvent(Event e) {
        mDbController.insertEventRecord(e);
        storeEventImageOnDevice(e);
        sAllEvents.add(e);
        resetFilteredEvents();

        for (EventDataSubscriber sub : sSubscribers)
            sub.onEventCreated(e);
    }

    private void storeEventImageOnDevice(Event event) {
        new ImageStorageController(mContext).
                setFileName(event.getPhotoUri()).
                setDirectoryName(Constant.DIRECTORY_NAME).
                save(event.getPhotoBitmap());
    }

    private Bitmap getEventBitmapFromDevice(Event event) {
         return new ImageStorageController(mContext).
                setFileName(event.getPhotoUri()).
                setDirectoryName(Constant.DIRECTORY_NAME).
                load();
    }

    public void filterEventsByCategory(Category criteria) {
        switch (criteria) {
            case ALL_EVENTS:
                resetFilteredEvents();
                break;
            case PAST_EVENTS:
                filterPastEvents();
                break;
            default:
                filterByCategory(criteria);
        }
    }

    private void resetFilteredEvents() {
        sFilteredEvents.clear();
        sFilteredEvents.addAll(sAllEvents);
    }

    // TODO: 10/5/2018
    private void filterPastEvents() {

    }

    private void filterByCategory(Category criteria) {
        sFilteredEvents.clear();
        for (Event e : sAllEvents) {
            if (e.getCategory().equalsIgnoreCase(mContext.getString(criteria.getResourceId())))
                sFilteredEvents.add(e);
        }
    }

    public ArrayList<Event> getEvents() {
        return sFilteredEvents;
    }

    /**
     * Registers an EventDataSubscriber instance into this class, so that it can be notified when an event is created
     * @param subscriber
     */
    public static void subscribe(EventDataSubscriber subscriber) {
        sSubscribers.add(subscriber);
    }
}
