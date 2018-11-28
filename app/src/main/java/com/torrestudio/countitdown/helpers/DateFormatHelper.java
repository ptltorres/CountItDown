/**
 *  Helper class that converts a Calendar instance into a String, with the corresponding regional
 *  date format.
 *
 *  Author: P. Torres
 *  Last Modified: 11/14/18
 */

package com.torrestudio.countitdown.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateFormatHelper {

    private static final String US_DATE_FORMAT = "MM/dd/YYYY";
    private static final String NOT_US_DATE_FORMAT = "dd/MM/YYYY";
    private static final String TIME_FORMAT = "hh:mm a";

    private static SimpleDateFormat sDateFormatter, sTimeFormatter;

    static {
        sDateFormatter = new SimpleDateFormat(
                Locale.getDefault().equals(Locale.US) ? US_DATE_FORMAT : NOT_US_DATE_FORMAT);
        sTimeFormatter = new SimpleDateFormat(TIME_FORMAT);
    }

    public static String formatDate(Calendar calendar) {
        return sDateFormatter.format(calendar.getTime());
    }

    public static String formatDate(long calendarInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendarInMillis);
        return sDateFormatter.format(calendar.getTime());
    }

    public static String formatTime(Calendar calendar) {
        return sTimeFormatter.format(calendar.getTime());
    }

    public static String formatTime(long calendarInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendarInMillis);
        return sTimeFormatter.format(calendar.getTime());
    }
}
