package com.mike.baselib.utils;

import com.mike.baselib.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT1 = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_FORMAT2 = "yyyy/MM/dd";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String DATE_FORMAT4 = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT5 = "yyyy/MM/d HH:mm";

    /**
     * @param timeMillis
     * @return
     */
    public static String formatDate(long timeMillis) {
        return formatDate(timeMillis, DATE_FORMAT);
    }

    public static String formatDate(long timeMillis, String format) {
        return new SimpleDateFormat(format).format(timeMillis <= 0 ? new Date() : new Date(timeMillis));
    }

    /*
     * 将时间转换为时间戳
     */
    public static long dateToStamp(String s) throws ParseException {
        return dateToStamp(s, DATE_FORMAT);
    }

    /*
     * 将时间转换为时间戳
     */
    public static long dateToStamp(String s, String format) throws ParseException {
        return new SimpleDateFormat(format).parse(s).getTime();
    }

    public static Calendar getYearFirstDayCalendar(int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, amount);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return calendar;
    }

    public static String msecToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        int millisecond = 0;
        if (time <= 0)
            return AppContext.getInstance().getString(R.string.minutes_and_seconds);
        else {
            second = time / 1000;
            minute = second / 60;
            millisecond = time % 1000;
            if (second < 60) {
                timeStr = unitFormat(second) + AppContext.getInstance().getString(R.string.second) + unitFormat2(millisecond);
            } else if (minute < 60) {
                second = second % 60;
                timeStr = unitFormat(minute) + AppContext.getInstance().getString(R.string.minute) + unitFormat(second) + AppContext.getInstance().getString(R.string.second);
            } else {// 数字>=3600 000的时候
                hour = minute / 60;
                minute = minute % 60;
                second = second - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + AppContext.getInstance().getString(R.string.hour) + unitFormat(minute) + AppContext.getInstance().getString(R.string.minute) + unitFormat(second) + AppContext.getInstance().getString(R.string.second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {// 时分秒的格式转换
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public static String unitFormat2(int i) {// 毫秒的格式转换
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "00" + Integer.toString(i);
        else if (i >= 10 && i < 100) {
            retStr = "0" + Integer.toString(i);
        } else
            retStr = "" + i;
        return retStr;
    }

    public Date getCST(String strGMT) {
        DateFormat df = new SimpleDateFormat("EEE, d-MMM-yyyy HH:mm:ss z", Locale.ENGLISH);
        try {
            return df.parse(strGMT);
        } catch (ParseException e) {
            e.printStackTrace();
            //return new Date();
        }
        return null;
    }

    public String getGMT(Date dateCST) {
        DateFormat df = new SimpleDateFormat("EEE, d-MMM-yyyy HH:mm:ss z", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("GMT")); // modify Time Zone.
        return (df.format(dateCST));
    }
}
