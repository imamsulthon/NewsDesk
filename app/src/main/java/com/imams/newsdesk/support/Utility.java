package com.imams.newsdesk.support;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    public static String formatDate(String currentDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss");
        try {
            Date date = format.parse(currentDate);
            format = new SimpleDateFormat("dd MMM yyyy, h:mm a");
            currentDate = format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Date: ", "Problem when parsing date format");
        }
        return currentDate;
    }
}
