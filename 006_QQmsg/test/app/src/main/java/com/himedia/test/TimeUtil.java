package com.himedia.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by admin on 2018/9/6.
 */

public class TimeUtil {

    private static final String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    public static String formatDate(long time){

        if(System.currentTimeMillis()/1000 > time) {
            Calendar currentCal = Calendar.getInstance();
            currentCal.setTime(new Date(System.currentTimeMillis()));
            int currentYear = currentCal.get(Calendar.YEAR);
            int currentDay = currentCal.get(Calendar.DAY_OF_YEAR) + 1;

            Calendar msgCal = Calendar.getInstance();
            msgCal.setTime(new Date(time * 1000));
            int msgYear = currentCal.get(Calendar.YEAR);
            int msgDay = msgCal.get(Calendar.DAY_OF_YEAR) + 1;
            String msgHour = addZero(msgCal.get(Calendar.HOUR_OF_DAY));
            String msgMin = addZero(msgCal.get(Calendar.MINUTE));
            int msgWeek = msgCal.get(Calendar.DAY_OF_WEEK);


            //同一年
            if (currentYear == msgYear) {
                int gapDay = currentDay - msgDay;
                switch (gapDay){
                    case 0://今天
                        return msgHour + ":" + msgMin;
                    case 1://昨天
                        return "昨天 " + msgHour + ":" + msgMin;
                    case 2://同一周
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        return weekDays[msgWeek-1]+" "+msgHour + ":" + msgMin;
                    default:
                        break;
                }
            }
        }
        return stampToDate(time);
    }

    private static String addZero(int time){
        String ret = ""+time;
        if(time >=0 && time < 10){
            ret = "0"+time;
        }
        return ret;
    }

    /*
 * 将时间戳转换为时间
 */
    public static String stampToDate(long s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(s*1000);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
    * 将时间转换为时间戳
    */
    public static long dateToStamp(String s){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        return ts;
    }
}
