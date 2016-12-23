package com.example.eqoram.alpha;

import android.util.Log;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sven on 12/20/2016.
 */

public final class ConversionHelper {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-DD kk:mm:ss.SSS");

    public static Timestamp stringToTimeStamp(String time){
        Timestamp timestamp = null;
        Date dateTime = null;
        try{
            dateTime = DATE_FORMAT.parse(time);
        }catch (ParseException ex){
            Log.d("Error", ex.getMessage());
        }
        timestamp = new Timestamp(dateTime.getTime());
        return timestamp;
    }
    public static String timeStampToString(Timestamp ts){
        return ts.toString();
    }
    public static Timestamp now(){
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public static boolean intToBool(int bool){
       return (bool == 1)? true : false;
    }
}
