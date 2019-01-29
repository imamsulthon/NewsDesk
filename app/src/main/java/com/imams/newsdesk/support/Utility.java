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
            format = new SimpleDateFormat("dd-MM-yyyy, h:mm a");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Date Utils: ", "Problem with parsing date format");
        }
        return currentDate;
    }
}
