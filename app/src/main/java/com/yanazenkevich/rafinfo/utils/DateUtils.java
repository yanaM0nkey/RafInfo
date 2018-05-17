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

    public static String getAnnouncementDate(Announcement announcement){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
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
}
