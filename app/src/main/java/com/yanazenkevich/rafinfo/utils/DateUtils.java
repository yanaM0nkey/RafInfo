package com.yanazenkevich.rafinfo.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.entities.Announcement;
import com.yanazenkevich.rafinfo.entities.Vacancy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

    public static String getStatus(Announcement announcement, Context context){
       return getStatus(announcement.getUpdated(), announcement.getCreated(), context);
    }

    public static String getStatus(Vacancy vacancy, Context context){
        return getStatus(vacancy.getUpdated(), vacancy.getCreated(), context);
    }

    private static String getStatus(long updated, long created, Context context){
        long date;
        if(updated == 0){
            date = created;
            return getDate(context, new Date(date));
        }else{
            date = updated;
            return context.getResources().getString(R.string.updated, getDate(context,new Date(date)));
        }
    }
    //EEE, d MMM yyyy HH:mm:ss Z
    private static String getDate(Context context, Date date){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fullFormat = new SimpleDateFormat("d MMM yyyy HH:mm");
        fullFormat.setTimeZone(TimeZone.getTimeZone("Europe/Minsk"));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat shortFormat = new SimpleDateFormat("HH:mm");
        shortFormat.setTimeZone(TimeZone.getTimeZone("Europe/Minsk"));
        if(date.getDay() == new Date().getDay()){
            return context.getResources().getString(R.string.created, shortFormat.format(date));
        }else{
            return fullFormat.format(date);
        }
    }

    public static String getAnnouncementDateFull(Announcement announcement){
        return getString(announcement, "EEE, d MMM yyyy HH:mm");
    }

    public static String getAnnouncementDate(Announcement announcement){
        return getString(announcement, "dd.MM.yyyy");
    }

    public static String getAnnouncementTime(Announcement announcement){
       return getString(announcement, "HH:mm");
    }

    private static String getString(Announcement announcement, String string){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(string);
        format.setTimeZone(TimeZone.getTimeZone("Europe/Minsk"));
        return format.format(announcement.getDate());
    }


    public static long getDateInMS(String date){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("Europe/Minsk"));
        Date dDate = new Date();
        try {
            dDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dDate.getTime();
    }

    public static String getSelectedDate(int day, int month, int year){
        return formatDate(day)+"."+formatDate(month+1)+"."+String.valueOf(year);
    }

    private static String formatDate(int selected){
        return selected < 10 ? "0"+String.valueOf(selected) : String.valueOf(selected);
    }

    public static String getSelectedTime(int hour, int minute){
        return formatTime(hour) + ":" + formatTime(minute);
    }

    private static String formatTime(int time){
        String selected;
        if(time == 0){
            selected = "00";
        }else{
            selected = time < 10 ? "0"+String.valueOf(time) : String.valueOf(time);
        }
        return selected;
    }
}
