package com.torrestudio.countitdown.entities;

import java.io.Serializable;

public class DateDifference implements Serializable {
    private static final long serialVersionUID = 1L;

    private long mTargetDate;
    private long mElapsedDays;
    private long mElapsedHours;
    private long mElapsedMinutes;
    private long mElapsedSeconds;

    public DateDifference(long targetDate) {
        mTargetDate = targetDate;
        update();
    }

    public long getElapsedDays() {
        return mElapsedDays;
    }

    public long getElapsedHours() {
        return mElapsedHours;
    }

    public long getElapsedMinutes() {
        return mElapsedMinutes;
    }

    public long getElapsedSeconds() {
        return mElapsedSeconds;
    }

    /**
     * Updates the elapsed measurements of times between the current time and the target date.
     */
    public void update() {
        long currentTime = System.currentTimeMillis();
        long difference = mTargetDate - currentTime;

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        mElapsedDays = difference / daysInMilli;
        difference = difference % daysInMilli;

        mElapsedHours = difference / hoursInMilli;
        difference = difference % hoursInMilli;

        mElapsedMinutes = difference / minutesInMilli;
        difference = difference % minutesInMilli;

        mElapsedSeconds = difference / secondsInMilli;
    }
}
