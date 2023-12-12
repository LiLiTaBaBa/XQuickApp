package com.zlj.utils.other;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author ChayChan
 * @description: 和时间相关的工具类
 * @date 2017/6/19  20:08
 */

public final class TimeUtils {

    public static final long ONE_MINUTE_MILLIONS = 60 * 1000;
    public static final long ONE_HOUR_MILLIONS = 60 * ONE_MINUTE_MILLIONS;
    public static final long ONE_DAY_MILLIONS = 24 * ONE_HOUR_MILLIONS;

    /**
     * 获取短时间格式
     *
     * @return
     */
    public static String getShortTime(long millis) {
        Date date = new Date(millis);
        Date curDate = new Date();

        String str = "";
        long durTime = curDate.getTime() - date.getTime();

        int dayStatus = calculateDayStatus(date, new Date());

        if (durTime <= 10 * ONE_MINUTE_MILLIONS) {
            str = "刚刚";
        } else if (durTime < ONE_HOUR_MILLIONS) {
            str = durTime / ONE_MINUTE_MILLIONS + "分钟前";
        } else if (dayStatus == 0) {
            str = durTime / ONE_HOUR_MILLIONS + "小时前";
        } else if (dayStatus == -1) {
            str = "昨天" + DateFormat.format("HH:mm", date);
        }else if (isSameYear(date, curDate)&&isSameMonth(date, curDate)&&isSameWeek(date, curDate)){
            str="本周";
        }else if (isSameYear(date, curDate)&&isSameMonth(date, curDate)){
            str="本月";
        }else {
            str = DateFormat.format("yyyy/MM", date).toString();
        }
        return str;
    }

    public static boolean isSameWeek(Date targetTime, Date compareTime) {
        Calendar tarCalendar = Calendar.getInstance();
        tarCalendar.setTime(targetTime);
        int tarYear = tarCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comYear = compareCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);

        return tarYear == comYear;
    }

    public static boolean isSameMonth(Date targetTime, Date compareTime) {
        Calendar tarCalendar = Calendar.getInstance();
        tarCalendar.setTime(targetTime);
        int tarYear = tarCalendar.get(Calendar.MONTH);

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comYear = compareCalendar.get(Calendar.MONTH);

        return tarYear == comYear;
    }


    public static boolean isSameYear(Date targetTime, Date compareTime) {
        Calendar tarCalendar = Calendar.getInstance();
        tarCalendar.setTime(targetTime);
        int tarYear = tarCalendar.get(Calendar.YEAR);

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comYear = compareCalendar.get(Calendar.YEAR);

        return tarYear == comYear;
    }


    public static int calculateDayStatus(Date targetTime, Date compareTime) {
        Calendar tarCalendar = Calendar.getInstance();
        tarCalendar.setTime(targetTime);
        int tarDayOfYear = tarCalendar.get(Calendar.DAY_OF_YEAR);

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comDayOfYear = compareCalendar.get(Calendar.DAY_OF_YEAR);

        return tarDayOfYear - comDayOfYear;
    }

    public static String secToTime(int time) {
        time=time/1000;
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":"
                        + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String secToTime(float time) {
        time=time/1000;
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = (int) (time / 60);
            if (minute < 60) {
                second = (int) (time % 60);
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = (int) (time - hour * 3600 - minute * 60);
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":"
                        + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    /**
     * yyyy-MM-dd
     * @param date
     * @return
     */
    public static String getNeedTime(String date){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * yyyy-MM-dd
     * @param date
     * @return
     */
    public static String getNeedTime2(long date){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(new Date(date));
    }

    /**
     * 获取天
     * @param time
     * @return
     */
    public static String getDay(String time){
        try {
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date=format.parse(time);
            return String.valueOf(date.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取月
     * @param time
     * @return
     */
    public static String getMonth(String time){
        try {
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date=format.parse(time);
            return (date.getMonth()+1)+"月";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取时间戳
     * @param date
     * @return
     */
    public static String getSecondTimestamp(Date date){
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        return timestamp.substring(0,length-3);
    }


    /**
     * 获取时间戳
     * @param date
     * @return
     */
    public static String getEncodeTimestamp(Date date){
        String time= String.valueOf(getSecondTimestamp(date));
        return time.substring(3,time.length());
    }



}
