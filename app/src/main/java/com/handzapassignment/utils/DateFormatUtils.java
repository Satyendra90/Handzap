package com.handzapassignment;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by satyendra on 6/2/16.
 */

public class DateFormatUtils {

    private static final String TAG = DateFormatUtils.class.getSimpleName();

    public static String convertDateFormat(String currentDateFormat, String requiredDateFormat, String value){

        String reqFormatedDate = "";
        try{
            SimpleDateFormat existingFormat = new SimpleDateFormat(currentDateFormat);
            SimpleDateFormat reqFormat = new SimpleDateFormat(requiredDateFormat, Locale.US);
            reqFormatedDate = reqFormat.format(existingFormat.parse(value));
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return reqFormatedDate;
    }

    public static Calendar getCalendarInstanceFromFormatedDate(String currentDateFormat, String value){

        Calendar calendar = Calendar.getInstance();
        try{
            SimpleDateFormat existingFormat = new SimpleDateFormat(currentDateFormat);
            Date date = existingFormat.parse(value);
            calendar.setTime(date);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return calendar;
    }

    public static Date getDateInstanceFromFormatedDate(String currentDateFormat, String value){

        Date date = null;
        try{
            SimpleDateFormat existingFormat = new SimpleDateFormat(currentDateFormat);
            date = existingFormat.parse(value);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return date;
    }

    public static String getCurrentDateInFormat(String requiredFormat){

        String currentDate = null;
        try{
            SimpleDateFormat reqFormat = new SimpleDateFormat(requiredFormat, Locale.US);
            currentDate = reqFormat.format(Calendar.getInstance().getTime());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return currentDate;
    }

    public static String convertCalendarInstanceInRequestedDateFormat(Calendar calendar, String requiredFormat){

        String date = null;
        try{
            SimpleDateFormat reqFormat = new SimpleDateFormat(requiredFormat, Locale.US);
            date = reqFormat.format(calendar.getTime());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return date;
    }

    public static String convertUTCToLocalTimeZone(String utcDate, String reqLocalFormat){
        // String utcdate= "2016-02-02 14:12:46";

        String date = null;
        try{

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            // Convert UTC to Local time (Works Fine)
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date time = sdf.parse(utcDate);

            SimpleDateFormat pstFormat = new SimpleDateFormat(reqLocalFormat, Locale.US);
            pstFormat.setTimeZone(TimeZone.getDefault());
            date = pstFormat.format(time);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return date;
    }

    public static String convertLocalTimeZoneToSpecificTimeZone(String localeDate, String reqLocalFormat, String timeZone){
        // String utcdate= "2016-02-02 14:12:46";

        String date = null;
        try{

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            // Convert UTC to Local time (Works Fine)
            sdf.setTimeZone(TimeZone.getDefault());
            Date time = sdf.parse(localeDate);

            SimpleDateFormat pstFormat = new SimpleDateFormat(reqLocalFormat, Locale.US);
            pstFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
            date = pstFormat.format(time);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return date;
    }

    public   static  Date convertLocalToTargetTimeZoneAbbreviation(String zoneValue ) {
        Date now = new Date();
        TimeZone zone = TimeZone.getTimeZone(zoneValue);
        switch (zoneValue){
            case "PST":
                zone = TimeZone.getTimeZone("America/Los_Angeles"); // For example...
                break;
            case "EDT":
                zone = TimeZone.getTimeZone("America/Aruba"); // For example...
                break;
            case "EST":
                zone = TimeZone.getTimeZone("America/Fort_Wayne"); // For example...
                break;
            case "CDT":
                zone = TimeZone.getTimeZone("America/Rainy_River"); // For example...
                break;
            case "CST":
                zone = TimeZone.getTimeZone("America/Chicago"); // For example...
                break;
            case "MDT":
                zone = TimeZone.getTimeZone("America/Guatemala" ); // For example...
                break;
            case "MST":
                zone = TimeZone.getTimeZone("US/Arizona"); // For example...
                break;
            case "PDT":
                zone = TimeZone.getTimeZone("US/Arizona"); // For example...
                break;
            case "AKDT":
                zone = TimeZone.getTimeZone("America/Metlakatla"); // For example...
                break;
            case "AKST":
                zone = TimeZone.getTimeZone("America/Metlakatla"); // For example...
                break;
            case "HST":
                zone = TimeZone.getTimeZone("Pacific/Honolulu"); // For example...
                break;
            case "IST":
                zone = TimeZone.getTimeZone("Asia/Kolkata"); // For example...
                break;
            default:
                zone = TimeZone.getTimeZone("America/Los_Angeles"); // For example...
                break;
        }


        /*if(isRequiredTime) {
            format = new SimpleDateFormat("HH:mm:ss"); // Put your pattern here
        }else {
            format = new SimpleDateFormat("dd-MM-yyyy"); // Put your pattern here
        }*/

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(zone);
        String text = formatter.format(now);
        Log.e(TAG, "Inside time =>"+text);
        return getDateInstanceFromFormatedDate("yyyy-MM-dd HH:mm:ss", text);
    }


    public static String convertMillisecondIntoUTCTime(long time, String reqFormat){
        String date = null;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(reqFormat, Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = sdf.format(new Date(time));
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return date;
    }

    /*Method  is used to format the time*/
    public static String formatTime12Hour(String time) {
        String timeActual = "";
        SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat date12Format = new SimpleDateFormat("hh a");
        try {
            timeActual = date12Format.format(date24Format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeActual;
    }
    /*Method  is used to format the time*/
    public static String formatTime12HourWithoutAm(String time) {
        String timeActual = "";
        SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat date12Format = new SimpleDateFormat("hh");
        try {
            timeActual = date12Format.format(date24Format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeActual;
    }




    public static  String getDayNameFromSpecficDate(String stringDate) throws ParseException {
    /*    SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date date = inFormat.parse(comingDate);
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        String dayName = outFormat.format(date);
        Log.e("DAY", "Day Name =>"+dayName);
        return dayName;*/

        String[] daysArray = new String[] {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        String day = "";

        int dayOfWeek =0;
        //dateTimeFormat = yyyy-MM-dd;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = formatter.parse(stringDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            dayOfWeek = c.get(Calendar.DAY_OF_WEEK)-1;
            if (dayOfWeek < 0) {
                dayOfWeek += 7;
            }
            day = daysArray[dayOfWeek];
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("returned day ", "day name => "+day);
        return day;
    }

    public static int calculateAge(Calendar dob) {
        Calendar current = Calendar.getInstance();
        current.setTime(new Date());
        int dobMonth = dob.get(Calendar.MONTH) + 1;
        int currentMonth = current.get(Calendar.MONTH) + 1;

        int dobYear = dob.get(Calendar.YEAR);
        int currentYear = current.get(Calendar.YEAR);

        if (currentMonth < dobMonth) {
            currentMonth += 12;
            currentYear -= 1;
        }

        //    String return type if you want month also (currentYear - dobYear) + "-" + (currentMonth - dobMonth);
        return (currentYear - dobYear);
    }

}
