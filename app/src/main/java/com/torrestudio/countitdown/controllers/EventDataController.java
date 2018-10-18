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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class EventDataController {

    // List of events in the database. All instances will share all the events.
    private static List<Event> sAllEvents = new ArrayList<>();
    // Subscribers to be notified when new data is added.
    private static List<EventDataSubscriber> sSubscribers = new ArrayList<>();
    private static boolean isEventDataLoaded = false;
    private static boolean sSubscribersNotified = false;

    private Context mContext;
    private EventDbController mDbController;
    private List<Event> mFilteredEvents = new ArrayList<>();

    public static EventDataController initController(Context context) {
        return new EventDataController(context);
    }

    private EventDataController(Context context) {
        mContext = context;
        mDbController = new EventDbController(mContext);

        if (!isEventDataLoaded)
            loadEventsAndNotifySubscribers();
        else
            resetFilteredEvents();
    }

    private void loadEventsAndNotifySubscribers() {
        new Thread(() -> {
            loadAllEventsFromDb();
            notifySubscribers();
        }).start();
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
        Collections.sort(sAllEvents);
        resetFilteredEvents();
        isEventDataLoaded = true;
    }

    private void notifySubscribers() {
        if (!sSubscribersNotified) {
            for (EventDataSubscriber sub : sSubscribers)
                sub.onEventDataLoaded();
            sSubscribersNotified = true;
        }
    }

    public void createEvent(Event e) {
        mDbController.insertEventRecord(e);
        storeEventImageOnDevice(e);
        insertSortedEventToList(e);
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

    private void insertSortedEventToList(Event e) {
        sAllEvents.add(e);
        Collections.sort(sAllEvents);

        // Crashes the app. Using a less efficient approach for now :-(
        /*
            for (int i = 0; i < sAllEvents.size(); i++) {
                if (Long.compare(e.getDateTimeInMillis(), sAllEvents.get(i).getDateTimeInMillis()) < 0)
                    sAllEvents.add(i, e);
            }
        */
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
        mFilteredEvents.clear();

        for (Event event : sAllEvents)
            if (!event.isPastEvent())
                mFilteredEvents.add(event);
    }

    private void filterPastEvents() {
        mFilteredEvents.clear();

        for (Event event : sAllEvents)
            if (event.isPastEvent())
                mFilteredEvents.add(event);

        Collections.reverse(mFilteredEvents);
    }

    private void filterByCategory(Category criteria) {
        mFilteredEvents.clear();
        for (Event e : sAllEvents) {
            if (e.getCategory().equalsIgnoreCase(mContext.getString(criteria.getResourceId())) && !e.isPastEvent())
                mFilteredEvents.add(e);
        }
    }

    public List<Event> getEvents() {
        return mFilteredEvents;
    }

    /**
     * Registers an EventDataSubscriber instance into this class, so that it can be notified when an event is created
     * @param subscriber
     */
    public static void subscribe(EventDataSubscriber subscriber) {
        sSubscribers.add(subscriber);
        if (isEventDataLoaded) {
            subscriber.onEventDataLoaded();
        }
    }
}
