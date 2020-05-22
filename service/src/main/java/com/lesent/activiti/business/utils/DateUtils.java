package com.lesent.activiti.business.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static final String yyyyMMddhhmmssSSS = "yyyyMMddhhmmssSSS";
    public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String _yyyyMMddHHmmss = "yyyy/MM/dd HH:mm:ss";
    public static final String _yyyy_MMddHH = "yyyy/MM/dd HH";
    public static final String _yyyyMMddHH = "yyyy-MM-dd HH";
    public static final String _yyyyMMddHHMM = "yyyy-MM-dd HH:mm";
    public static final String _yyyyMMddHHMM30 = "yyyyMMddHH";
    public static final String _MMdd2CHINESE = "MM月dd日";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String _yyyyMMdd = "yyyyMMdd";

    public static String parseDate_yyyy_MM_dd(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(yyyy_MM_dd);
        return sdf.format(date);

    }

    public static String parseDate_yyyyMMdd(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(_yyyyMMdd);
        return sdf.format(date);

    }

    public static String parseDate_yyyyMMddhhmmssSSS(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMddhhmmssSSS);
        return sdf.format(date);

    }

    public static String parseDateByPartner(Date date, String partner) {
        SimpleDateFormat sdf = new SimpleDateFormat(partner);
        return sdf.format(date);

    }

    public static String parseDateMMdd2CHINESE(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(_MMdd2CHINESE);
        return sdf.format(date);

    }

    public static Date parseDateyyyyMMddHHmmss(String date,String partner) {
        try {
            return parseDate(date, partner);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseDate_yyyyMMddHH(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(_yyyyMMddHH);
        return sdf.format(date);

    }

    public static String parseDate_yyyyMMddHHmmss(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMddHHmmss);
        return sdf.format(date);

    }

    public static String parseDate_yyyyMMddHHmm(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMddHHmmss);
        return sdf.format(date);

    }

    public static String parseDate_yyyyMMddHH30(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(_yyyyMMddHHMM30);
        return sdf.format(date);

    }

    public static String parseDate_yyyyMMddHHmmssByDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(_yyyy_MMddHH);
        return sdf.format(date);
    }

    public static String xyyyyMMddHH(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(_yyyyMMddHHMM30);
        return sdf.format(date);
    }

    public static Date after15() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, -15);// 15分钟之前的时间
        Date beforeD = beforeTime.getTime();
        return beforeD;
    }

    public static Date after7day() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.DAY_OF_YEAR, -7);// 7天之前的时间
        Date beforeD = beforeTime.getTime();
        return beforeD;
    }

    public static Date getDayBegin() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    //获取当天的结束时间
    public static Date getDayEnd() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }
}
