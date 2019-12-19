package com.gabriel.corpwx.util;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {

    /**
     * 值 yyyyMM
     */
    public static final String YYYYMM = "yyyyMM";

    /**
     * 值 yyyy
     */
    public static final String YYYY = "yyyy";

    /**
     * 值 yyyy-MM
     */
    public static final String YYYY_MM = "yyyy-MM";

    /**
     * 值 yyyy-MM-dd
     */
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 值 yyyyMMdd
     */
    public static final String YYYYIMMIDD = "yyyy/MM/dd";

    /**
     * 值 yyyy/MM/dd
     */
    public static final String YYYYMMDD = "yyyyMMdd";

    /**
     * 值 yyMMdd
     */
    public static final String YYMMDD = "yyMMdd";

    /**
     * 值 yyMM
     */
    public static final String YYMM = "yyMM";

    /**
     * 值 yyyy-MM-dd HH:mm:ss
     */
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 值 yyyy-MM-dd HH:mm
     */
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    /**
     * 值 yyyyMMddHHmmss
     */
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    /**
     * 值 HHmmss
     */
    public static final String HHMMSS = "HHmmss";

    /**
     * 值 MM-dd HH:mm
     */
    public static final String MM_DD_HH_MM = "MM-dd HH:mm";

    /**
     * 时间转换,Date 转 Calendar
     *
     * @param date
     * @return
     */
    public static Calendar dateToCal(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    /**
     * 时间段遍历每一天生成List
     *
     * @param startTime endTime
     * @return
     */
    public static List<String> getDateListStr(String startTime, String endTime) {
        List<String> listDate = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = dateFormat.parse(startTime);
            Date endDate = dateFormat.parse(endTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            while (calendar.getTime().before(endDate) || calendar.getTime().equals(endDate)) {
                listDate.add(dateFormat.format(calendar.getTime()));
                calendar.add(Calendar.DATE, 1);
            }
            return listDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 时间段遍历每月生成List
     *
     * @param startTime endTime
     * @return
     */
    public static List<String> getMonthListStr(String startTime, String endTime) {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        try {
            min.setTime(sdf.parse(startTime));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

            max.setTime(sdf.parse(endTime));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

            while (min.before(max)) {
                result.add(sdf.format(min.getTime()));
                min.add(Calendar.MONTH, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 时间段遍历每季度生成List
     *
     * @param startTime endTime
     * @return
     */
    public static List<String> getQuarterListStr(String startTime, String endTime) {

        if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
            if (startTime.length() > 4) {
                startTime = startTime.substring(0, 4);
            }
            if (endTime.length() > 4) {
                endTime = endTime.substring(0, 4);
            }
        }

        List<String> quarterList = new ArrayList<>();
        String[] quarterStart = {"01", "04", "07", "10"};
        String[] quarterEnd = {"03", "06", "09", "12"};
        Date startDate = DateUtils.strToDate(startTime, DateUtils.YYYY);
        Date endDate = DateUtils.strToDate(endTime, DateUtils.YYYY);
        Calendar maxYearCalendar = Calendar.getInstance();
        Calendar minYearCalendar = Calendar.getInstance();
        maxYearCalendar.setTime(startDate);
        minYearCalendar.setTime(endDate);
        int startYear = maxYearCalendar.get(Calendar.YEAR);
        int endYear = maxYearCalendar.get(Calendar.YEAR);
        for (int index = 0; index < endYear - startYear + 1; index++) {
            startYear += index;
            String year = String.valueOf(startYear);
            quarterList.add(year + "-" + quarterStart[0] + "~" + year + "-" + quarterEnd[0]);
            quarterList.add(year + "-" + quarterStart[1] + "~" + year + "-" + quarterEnd[1]);
            quarterList.add(year + "-" + quarterStart[2] + "~" + year + "-" + quarterEnd[2]);
            quarterList.add(year + "-" + quarterStart[3] + "~" + year + "-" + quarterEnd[3]);
        }
        return quarterList;
    }

    /**
     * 时间段遍历每季度生成List
     *
     * @param startTime endTime
     * @return 格式：2018_1，年_季度
     */
    public static List<String> getQuarterListStrB(String startTime, String endTime) {
        List<String> quarterList = new ArrayList<>();

        Calendar sta = DateUtils.strToCal(startTime, DateUtils.YYYY);
        Calendar end = DateUtils.strToCal(endTime, DateUtils.YYYY);

        int s = Integer.valueOf(String.format(
                "%s%s",
                sta.get(Calendar.YEAR),
                (sta.get(Calendar.MONTH) + 1) < 10 ? "0" + (sta.get(Calendar.MONTH) + 1) : sta.get(Calendar.MONTH) + 1
        ));
        int e = Integer.valueOf(String.format(
                "%s%s",
                end.get(Calendar.YEAR),
                (end.get(Calendar.MONTH) + 1) < 10 ? "0" + (end.get(Calendar.MONTH) + 1) : end.get(Calendar.MONTH) + 1
        ));
        while (e >= s) {
            String q = String.valueOf(sta.get(Calendar.YEAR));
            int month = sta.get(Calendar.MONTH);
            switch (month) {
//                case Calendar.JANUARY:
                case Calendar.FEBRUARY:
                case Calendar.MARCH:
                    q += "_1";
                    break;
                case Calendar.APRIL:
                case Calendar.MAY:
                case Calendar.JUNE:
                    q += "_2";
                    break;
                case Calendar.JULY:
                case Calendar.AUGUST:
                case Calendar.SEPTEMBER:
                    q += "_3";
                    break;
                case Calendar.OCTOBER:
                case Calendar.NOVEMBER:
                case Calendar.DECEMBER:
                    q += "_4";
                    break;
                default:
                    break;
            }

            if (!quarterList.contains(q)) {
                if (q.length() > 4) {
                    quarterList.add(q);
                }
            }

            sta.add(Calendar.MONTH, 1);
            s = Integer.valueOf(String.format(
                    "%s%s",
                    sta.get(Calendar.YEAR),
                    (sta.get(Calendar.MONTH) + 1) < 10 ? "0" + (sta.get(Calendar.MONTH) + 1) : sta.get(Calendar.MONTH) + 1
            ));
        }
        return quarterList;
    }

    /**
     * 时间段遍历每年生成List
     *
     * @param startTime endTime
     * @return
     */
    public static List<String> getYearListStr(String startTime, String endTime) {
        if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
            if (startTime.length() > 4) {
                startTime = startTime.substring(0, 4);
            }
            if (endTime.length() > 4) {
                endTime = endTime.substring(0, 4);
            }
        }
        List<String> yearList = new ArrayList<>();
        Date startDate = DateUtils.strToDate(startTime, DateUtils.YYYY);
        Date endDate = DateUtils.strToDate(endTime, DateUtils.YYYY);
        Calendar maxYearCalendar = Calendar.getInstance();
        Calendar minYearCalendar = Calendar.getInstance();
        maxYearCalendar.setTime(endDate);
        minYearCalendar.setTime(startDate);
        int endYear = maxYearCalendar.get(Calendar.YEAR);
        int startYear = minYearCalendar.get(Calendar.YEAR);
        for (int index = 0; index < endYear - startYear + 1; index++) {
            int yearOne = startYear + index;
            String year = String.valueOf(yearOne);
            yearList.add(year);

        }
        return yearList;
    }

    /**
     * 对月份进行操作
     *
     * @param
     * @return
     */
    public static Date stepMonth(Date date, int month) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, month);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    /**
     * 对年份进行操作
     *
     * @param
     * @return
     */
    public static Date stepYear(Date date, int year) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, year);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    /**
     * 对天进行操作
     *
     * @param
     * @return
     */
    public static Date stepDay(Date date, int day) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.DATE, day);
        return rightNow.getTime();
    }

    /**
     * 对小时进行操作
     *
     * @param
     * @return
     */
    public static Date stepHours(Date date, int hours) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.HOUR, hours);
        return rightNow.getTime();
    }

    /**
     * 对分钟进行操作
     *
     * @param
     * @return
     */
    public static Date stepMinutes(Date date, int minute) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.MINUTE, minute);
        return rightNow.getTime();
    }

    /**
     * 获取本月最后一天
     *
     * @param date
     * @return
     */
    public static String getMonthLastDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        return format.format(cale.getTime());
    }

    /**
     * 获取本月第一天
     *
     * @param date
     * @return
     */
    public static String getMonthFristDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        return format.format(cale.getTime());
    }

    /**
     * 获取本年第一天
     *
     * @param date
     * @return
     */
    public static String getYearFirstDate(Date date) {
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, Integer.parseInt(yearFormat.format(date)));
        Date currYearFirst = calendar.getTime();
        return dateFormat.format(currYearFirst);
    }

    /**
     * 获取本年最后一天
     *
     * @param date
     * @return
     */
    public static String getYearLastDate(Date date) {
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, Integer.parseInt(yearFormat.format(date)));
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
        return dateFormat.format(currYearLast);
    }

    /**
     * Calendar转字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String calToStr(Calendar date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String dateStr = sdf.format(date.getTime());
        return dateStr;
    }

    /**
     * 字符串转Calendar
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Calendar strToCal(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar calendar = null;
        if (dateStr != null && !"".equals(dateStr)) {
            try {
                calendar = Calendar.getInstance();
                Date date = sdf.parse(dateStr);
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return calendar;
    }

    /**
     * Date转Integer
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Integer dateToInt(Date date, String pattern) {
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String dateStr = sdf.format(date);
        return Integer.parseInt(dateStr);
    }

    /**
     * Date转字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToStr(Date date, String pattern) {
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String dateStr = sdf.format(date);
        return dateStr;
    }

    /**
     * 字符串转Date
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date strToDate(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;
        if (dateStr != null && !"".equals(dateStr)) {
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    public static String subDate(Date startTime, Date endTime) {
        String subTime = "";
        long endLong = endTime.getTime();
        long startLong = startTime.getTime();
        long subLong = endLong - startLong;

        if (subLong < 0) {
            subTime = "00时00分";
            return subTime;
        }
//        long day=subLong/(24*60*60*1000);
        long hour = (subLong / (60 * 60 * 1000));
        long min = ((subLong / (60 * 1000)) - hour * 60);
        long second = ((subLong / 1000) - (min * 60 * 1000) - (hour * 60 * 60 * 1000));
//        long s=(subLong/1000-day*24*60*60-hour*60*60-min*60);
        if (hour == 0) {
            subTime = "00时";
        } else {
            if (hour < 10) {
                subTime = "0" + hour + "时";
            } else {
                subTime = hour + "时";
            }

        }
        if (min == 0) {
            subTime = subTime + "00" + "分";
        } else {
            if (min < 10) {
                subTime = subTime + "0" + min + "分";
            } else {
                subTime = subTime + min + "分";
            }

        }
        if (second == 0) {
            subTime = subTime + "00" + "秒";
        } else {
            if (min < 10) {
                subTime = subTime + "0" + second + "秒";
            } else {
                subTime = subTime + second + "秒";
            }

        }

        return subTime;
    }

    public static long subLong(Date startTime, Date endTime) {

        long endLong = endTime.getTime();
        long startLong = startTime.getTime();
        long subLong = endLong - startLong;

        if (subLong < 0) {
            subLong = 0;
            return subLong;
        }

        return subLong / 1000;
    }

    /**
     * 日期转星期
     *
     * @param datetime
     * @return
     */
    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 判断当前日期是星期几<br>   
     * @param pTime 修要判断的时间<br>  
     * @return dayForWeek 判断结果<br>  
     * @Exception 发生异常<br>  
     */
    public static int dayForWeek(String pTime) throws Exception {
        SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    public static void main(String[] args) {
        System.out.println(dateToWeek("20190226"));
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        System.out.println(f.format(stepDay(new Date(),2)));
    }

}
