package com.gzhealthy.health.tool;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    //根据日期获取星期
    public static String getWeek(String ptime) {
        String Week = "周";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//也可将此值当参数传进来
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(ptime));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                Week += "天";
                break;
            case 2:
                Week += "一";
                break;
            case 3:
                Week += "二";
                break;
            case 4:
                Week += "三";
                break;
            case 5:
                Week += "四";
                break;
            case 6:
                Week += "五";
                break;
            case 7:
                Week += "六";
                break;
            default:
                break;
        }
        return Week;
    }

    //根据日期获取前一天后一天
    public static String getafterDay(String strDate) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayAfter;
    }

    public static String getYesterday(String strDate) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayBefore;
    }


    /**
     * 计算此日期与当前日期相差天数
     * @param date 要判断的日期
     * @return
     */
    public static int compareDateCount(String date) {
        int compareResult = 0;
        //格式化时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date compareTime = format.parse(date);
            Date currentTime = format.parse(DateUtil.getStringDate());
            compareResult = getGapCount(compareTime, currentTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return compareResult;
    }

    //比较时间的大小
    public static boolean compareDate(String dateFirst, String dateSecond) {
        Boolean compareResult = true;
        //格式化时间
        SimpleDateFormat currentTime = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date beginTime = currentTime.parse(dateFirst);
            Date endTime = currentTime.parse(dateSecond);
            if (getGapCount(beginTime, endTime) < 0) {
                compareResult = false;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return compareResult;
    }

    //计算两个日期相隔多少天
    public static int getGapCount(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);
        return (int) ((fromCalendar.getTime().getTime() - toCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

    //按指定天数增加
    public static String addDay(String day, int dayAddNum) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date newDate2 = new Date(nowDate.getTime() + dayAddNum * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOk = simpleDateFormat.format(newDate2);
        return dateOk;
    }

    //按指定天数减少
    public static String deleteDay(String day, int daydeleteNum) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date newDate2 = new Date(nowDate.getTime() - daydeleteNum * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOk = simpleDateFormat.format(newDate2);
        return dateOk;
    }

    //将日期年月日格式转为月日型
    public static String toMouthAndDayShow(String date) {
        if (null != date) {
            String[] show = date.split("-");
            if (show.length == 3) {
                return show[1] + "-" + show[2];
            }
        }
        return "";
    }

    //获取系统时间
    public static String getSystemTime() {
        return getSystemTime();
    }

    //计算是否是儿童
    public static boolean isChild(String date) {

        Date isChilddate = DateUtil.StrToDate(date, "yyyy-MM-dd");

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(isChilddate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);
        int dayNum = (int) ((new Date().getTime() - toCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
        if (dayNum >= 2 * 365 && dayNum <= 12 * 365) {
            return true;
        } else {
            return false;
        }
    }

    //计算是否是成人
    public static boolean isAdult(String date) {
        Date isChilddate = DateUtil.StrToDate(date, "yyyy-MM-dd");
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(isChilddate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);
        int dayNum = (int) ((new Date().getTime() - toCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
        if (dayNum >= 12 * 365) {
            return true;
        } else {
            return false;
        }
    }

    /***
     *
     * @param startDate
     * @param endDate
     * @return 计算两个共有多少天
     */
    public static int getTotalDay(String startDate, String endDate) {
        int countDay = 0;
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date_start = sf.parse(startDate);
            Date date_end = sf.parse(endDate);
            countDay = Math.abs(DateUtils.getGapCount(date_start, date_end)) + 1;
//            mTotalDayTv.setText("共" + Math.abs(DateUtils.getGapCount(date_start, date_end)) + "晚");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return countDay;
    }

}
