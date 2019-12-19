package com.gabriel.corpwx.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

/**
 * @ClassName: LocalDataTimeUtils
 * @Author: yq
 * @Date: 2019-11-28 10:43
 * @Description: JDK 8 日期时间工具类
 **/
public class LocalDateTimeUtils {

    public static final String formatter_yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String formatter_yyyyMMddHHmm = "yyyy-MM-dd HH:mm";
    public static final String formatter_yyyyMMdd = "yyyy-MM-dd";

    /**
     * @Description: 将Date类型转化为指定格式字符串
     * @Param: [date, format]
     * @return: java.lang.String
     * @Date: 2019-11-28
     */
    public static String getStringByDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * @Description: 将指定格式的字符串转化为Date
     * @Param: [dateStr, format]
     * @return: java.util.Date
     * @Date: 2019-11-28
     */
    public static Date getDateByString(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @Description: 获取当前Date并转化为指定格式字符串
     * @Param: [format]
     * @return: java.lang.String
     * @Date: 2019-11-28
     */
    public static String getCurrentDate(String format) {
        return getStringByDate(new Date(), format);
    }

    /**
     * @Description: Date转换为LocalDateTime
     * @Param: [date]
     * @return: java.time.LocalDateTime
     * @Date: 2019-11-28
     */
    public static LocalDateTime convertDateToLDT(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * @Description: LocalDateTime转换为Date
     * @Param: [time]
     * @return: java.util.Date
     * @Date: 2019-11-28
     */
    public static Date convertLDTToDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }


    /**
     * @Description: 获取指定日期的秒
     * @Param: [time]
     * @return: java.lang.Long
     * @Date: 2019-11-28
     */
    public static Long getMilliByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * @Description: 获取指定日期的秒
     * @Param: [time]
     * @return: java.lang.Long
     * @Date: 2019-11-28
     */
    public static Long getSecondsByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
     * @Description: 获取指定时间的指定格式
     * @Param: [time, pattern]
     * @return: java.lang.String
     * @Date: 2019-11-28
     */
    public static String formatTime(LocalDateTime time,String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * @Description: 获取当前时间的指定格式
     * @Param: [pattern]
     * @return: java.lang.String
     * @Date: 2019-11-28
     */
    public static String formatNow(String pattern) {
        return  formatTime(LocalDateTime.now(), pattern);
    }

    /**
     * @Description: 日期加上一个数,根据field不同加不同值,field为ChronoUnit.*
     * @Param: [time, number, field]
     * @return: java.time.LocalDateTime
     * @Date: 2019-11-28
     */
    public static LocalDateTime plus(LocalDateTime time, long number, TemporalUnit field) {
        return time.plus(number, field);
    }

    /**
     * @Description: 日期减去一个数,根据field不同减不同值,field参数为ChronoUnit.*
     * @Param: [time, number, field]
     * @return: java.time.LocalDateTime
     * @Date: 2019-11-28
     */
    public static LocalDateTime minu(LocalDateTime time, long number, TemporalUnit field){
        return time.minus(number,field);
    }

    /**
     * 获取两个日期的差  field参数为ChronoUnit.*
     * @param startTime
     * @param endTime
     * @param field  单位(年月日时分秒)
     * @return
     */
    public static long betweenTwoTime(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {
        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        if (field == ChronoUnit.YEARS) return period.getYears();
        if (field == ChronoUnit.MONTHS) return period.getYears() * 12 + period.getMonths();
        return field.between(startTime, endTime);
    }

    /**
     * @Description: 比较两个Date的时间大小
     * @Param: [date, date2]
     * @return: boolean
     * @Date: 2019-11-28
     */
    public static boolean getLocalDateTime(Date date, Date date2) {
        if (date.compareTo(date2) == 1) {
            return true;
        }
        return false;
    }

    /**
     * @Description: 将LocalDateTime转化为Date
     * @Param: [localDateTime]
     * @return: java.util.Date
     * @Date: 2019-11-28
     */
    public static Date getDataByLocalDateTime(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * @Description: 将指定格式String类型日期转化为LocalDateTime
     * @Param: [format, localDateTime]
     * @return: java.time.LocalDateTime
     * @Date: 2019-11-28
     */
    public static LocalDateTime getLocalDateTimeByString(String format,String localDateTime) {
        DateTimeFormatter df= DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(localDateTime, df);
    }

    /**
     * @Description: 获取指定LocalDateTime的秒数
     * @Param: [localDateTime]
     * @return: java.lang.Long
     * @Date: 2019-11-28
     */
    public static Long getSecondByLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
     * @Description: 获取指定LocalDateTime的毫秒数
     * @Param: [localDateTime]
     * @return: java.lang.Long
     * @Date: 2019-11-28
     */
    public static Long getMillSecondByLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * @Description: 将LocalDateTime转化为指定格式字符串
     * @Param: [localDateTime, format]
     * @return: java.lang.String
     * @Date: 2019-11-28
     */
    public static String getStringByLocalDateTime(LocalDateTime localDateTime, String format) {
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * @Description: 将LocalDateTime转化为默认yy-mm-dd hh:mm:ss格式字符串
     * @Param: [localDateTime]
     * @return: java.lang.String
     * @Date: 2019-11-28
     */
    public static String getDefaultStringByLocalDateTime(LocalDateTime localDateTime) {
        return getStringByLocalDateTime(localDateTime, formatter_yyyyMMddHHmmss);
    }

    /**
     * @Description: 获取当前LocalDateTime并转化为指定格式字符串
     * @Param: [format]
     * @return: java.lang.String
     * @Date: 2019-11-28
     */
    public static String getCurrentLocalDateTime(String format) {
        return getStringByLocalDateTime(LocalDateTime.now(), format);
    }

    /**
     * @Description: 获取一天的开始时间
     * @Param: [localDateTime]
     * @return: java.lang.String
     * @Date: 2019-11-28
     */
    public static String getStartTime(LocalDateTime localDateTime) {
        return getDefaultStringByLocalDateTime(localDateTime.with(LocalTime.MIN));
    }

    /**
     * @Description: 获取当天的开始时间
     * @Param: []
     * @return: java.lang.String
     * @Date: 2019-11-28
     */
    public static String getStartTime() {
        return getDefaultStringByLocalDateTime(LocalDateTime.now().with(LocalTime.MIN));
    }

    /**
     * @Description: 获取一天的结束时间
     * @Param: [localDateTime]
     * @return: java.lang.String
     * @Date: 2019-11-28
     */
    public static String getEndTime(LocalDateTime localDateTime) {
        return getDefaultStringByLocalDateTime(localDateTime.with(LocalTime.MAX));
    }

    /**
     * @Description: 获取当天的结束时间
     * @Param: []
     * @return: java.lang.String
     * @Date: 2019-11-28
     */
    public static String getEndTime() {
        return getDefaultStringByLocalDateTime(LocalDateTime.now().with(LocalTime.MAX));
    }

    /**
     * @Description: 比较两个LocalDateTime的时间大小
     * @Param: [localDateTime1, localDateTime2]
     * @return: boolean
     * @Date: 2019-11-28
     */
    public static boolean getCompareLocalDateTime(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
        if (localDateTime1.isBefore(localDateTime2)) {
            return true;
        }
        return false;
    }


    /**
     * @Description: 计算两个指定格式String日期字符串的时间差(精确到毫秒)
     * @Param: [format, time1, time2]
     * @return: java.lang.Long
     * @Date: 2019-11-28
     */
    public static Long getCompareSecondLocalDateTime(String format ,String time1, String time2) {
        System.out.println("接收到的参数为:"+format+","+time1+","+time2);
        LocalDateTime localDateTime1=getLocalDateTimeByString(format,time1);
        LocalDateTime localDateTime2=getLocalDateTimeByString(format,time2);
        if(getMillSecondByLocalDateTime(localDateTime1)>getMillSecondByLocalDateTime(localDateTime2)) {
            return getMillSecondByLocalDateTime(localDateTime1)-getMillSecondByLocalDateTime(localDateTime2);
        }
        return getMillSecondByLocalDateTime(localDateTime2)-getMillSecondByLocalDateTime(localDateTime1);
    }

    /**
     * @Description: 获取两个LocalDateTime的天数差
     * @Param: [localDateTime1, localDateTime2]
     * @return: java.lang.Long
     * @Date: 2019-11-28
     */
    public static Long getCompareDayLocalDateTime(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
        if (getCompareLocalDateTime(localDateTime1, localDateTime2)) {
            Duration duration = Duration.between(localDateTime1, localDateTime2);
            return duration.toDays();
        } else {
            Duration duration = Duration.between(localDateTime2, localDateTime1);
            return duration.toDays();
        }
    }

    /**
     * @Description: 获取两个LocalDateTime的小时差
     * @Param: [localDateTime1, localDateTime2]
     * @return: java.lang.Long
     * @Date: 2019-11-28
     */
    public static Long getCompareYearLocalDateTime(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
        if (getCompareLocalDateTime(localDateTime1, localDateTime2)) {
            Duration duration = Duration.between(localDateTime1, localDateTime2);
            return duration.toHours();
        } else {
            Duration duration = Duration.between(localDateTime2, localDateTime1);
            return duration.toHours();
        }
    }
}
