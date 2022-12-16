
package com.gzhealthy.health.tool;

import android.content.Context;
import android.util.Log;

import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * 日期工具类
 */
public class DateUtil {

    public static final String yyyyMMdd = "yyyy-MM-dd";

    /**
     * string转成Date类型
     *
     * @param @param  dateString
     * @param @param  format
     * @param @return
     * @return Date
     * @throws
     * @Description:
     */
    public static Date StrToDate(String dateString, String format) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(dateString);
            return date;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    /**
     * date转成string类型
     *
     * @param @param  date
     * @param @param  format
     * @param @return
     * @return String
     * @throws
     * @Description:
     */
    public static String DateToStr(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * js时间更是转换
     *
     * @param @param  time
     * @param @param  format
     * @param @return
     * @return String
     * @throws
     * @Description:
     */
    public static String getMilliToDateForTime(String time, String format) {
        time = time.substring(6, time.length() - 2);
        Date date = new Date(Long.valueOf(time));
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * string 转成毫秒
     *
     * @param @param  date
     * @param @param  format
     * @param @return
     * @return long
     * @throws
     * @Description:
     */
    public static long stringToLong(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date dt = null;
        try {
            dt = sdf.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dt.getTime();
    }

    public static final String SHORT_DATE_FORMAT_1 = "yyyy年MM月dd日";

    public static final String SHORT_DATE_FORMAT_2 = "yyyy-MM-dd";

    public static final String LONG_DATE_FORMAT_1 = "yyyyMMddHHmmss";

    /**
     * 24小时制
     */
    public static final String C_TIME_PATTON_24HHMM = "HH:mm";

    /**
     * 12小时制
     */
    public static final String C_TIME_PATTON_12HHMM = "hh:mm";

    /**
     * 中文月份数组
     */
    public static final String[] monthsZh = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};

    /**
     * 英文月份数组
     */
    public static final String[] monthsEn = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    /**
     * 获取现在时间
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static Date getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }

    /**
     * 获取现在时间
     *
     * @return 返回对应格式的字符串
     */
    public static String getStringDateByFormat(String format) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(currentTime);
        return dateString;
    }



    /**
     * 向前或往后一天
     * @param date
     * @param count
     */
    public static String scrollCalendarDate(String date,int count) {
        String day = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(date));
            calendar.add(Calendar.DAY_OF_MONTH,count);
            Date mDate = calendar.getTime();
            day = format.format(mDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }


    /**
     * 计算两个小时单位之间换算为分钟总和
     * @param startTime
     * @param endTime
     * @return  毫秒
     */
    public static long calulationMinuteBetweenTime(String startTime,String endTime){
        long total = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm",Locale.CHINA);
            Date date1 = format.parse(startTime);
            Date date2 = format.parse(endTime);

            Calendar time1Calendar = Calendar.getInstance();
            time1Calendar.setTime(date1);
            long caltime1 = time1Calendar.getTimeInMillis();

            Calendar time2Calendar = Calendar.getInstance();
            time2Calendar.setTime(date2);
            long caltime2 = time2Calendar.getTimeInMillis();

            total = Math.abs(caltime2 - caltime1);
            total /= 60000;
//            long d1 = Math.abs(date1.getTime());
//            long d2 = Math.abs(date2.getTime());
//            long timemillon = d2 - d1;
//            total = timemillon / 60000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return total;
    }


    public static long calulationTotalMinute(String startTime,String endTime){
        long total = 0;
        try {
            int sh = Integer.parseInt(startTime.substring(0,startTime.indexOf(":")));
            int eh = Integer.parseInt(endTime.substring(0,endTime.indexOf(":")));
            int sm = Integer.parseInt(startTime.substring(startTime.indexOf(":")+1));
            int em = Integer.parseInt(endTime.substring(endTime.indexOf(":")+1));
            if (sh > eh){
                sh = 24 - sh;
                if (sm > em)//开始分钟在于结束分钟时  不足一小时
                    sh -= 1;
                total += (sh + eh) * 60;
            }else if (sh < eh){
                total += (eh - sh) * 60;
            }

            if (sm > em){
                //不足一小时
                total += (60 - sm + em);
            }else if (sm < em){
//                total += 60;
                total += (em - sm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }


    /**
     * 根据两个秒数 获取两个时间差
     * */
    public static String getDatePoor(long endDate, long nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // long ns = 1000;
        // 获得两个时间的秒时间差异
        long diff = (endDate*1000L) - (nowDate*1000L);
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = diff % nd % nh % nm / ns;
        String res = "";
        if(day != 0){
            res += day + "天";
        }
        if(hour != 0){
            res += "  "+hour + ":"+min + ":"+sec;
        }
        return res;
    }



    /**
     * 判断当前是否午夜
     * @return
     */
    public static boolean isDayNight(){
        Calendar cal = Calendar.getInstance();// 当前日期
        int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
        int minute = cal.get(Calendar.MINUTE);// 获取分钟
        int minuteOfDay = hour * 60 + minute;// 从0:00分开是到目前为止的分钟数
        final int start = 6 * 60;// 起始时间 06:00的分钟数
        final int end = 18 * 60;// 结束时间 17:59的分钟数
        if (minuteOfDay >= start && minuteOfDay < end) {
            return false;
        }

        return true;
    }


    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getStringDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDate(long time) {
        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getStringDate3(long time) {
        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getStringDate4(long time) {
        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getStringDate2(long time) {
//        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(time);
        return dateString;
    }

    public static String getStringDate5(long time) {
//        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateString = formatter.format(time);
        return dateString;
    }

    public static String getStringDateHour(long time) {
//        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        String dateString = formatter.format(time);
        return dateString;
    }

    public static String getStringDate6(long time) {
//        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(time);
        return dateString;
    }


    /**
     * 获取时间
     *
     * @return 返回短时间字符串格式 MM月dd日
     */
    public static String getUpdateDate(long time) {
        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(currentTime);
        String dateString = date.split("-")[1] + "月" + date.split("-")[2] + "日";
        return dateString;
    }

    /**
     * 获取时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDate(long time, SimpleDateFormat format) {
        Date currentTime = new Date(time);
        String dateString = format.format(currentTime);
        return dateString;
    }

    /**
     * 获取自定义格式时间
     * @param pattern
     * @return
     */
    public static String getStringDateNow(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime.getTime());
        return dateString;
    }

    /**
     * 获取时间 小时:分;秒 HH:mm:ss
     *
     * @return
     */
    public static String getTimeShort() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDateShort(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param dateDate
     * @return
     */
    public static String dateToStrLong(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式时间转换为字符串 yyyyMM
     *
     * @param dateDate
     * @return
     */
    public static String dateToStr(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式字符串转换为时间 yyyyMM
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 得到现在时间
     *
     * @return
     */
    public static Date getNow() {
        Date currentTime = new Date();
        return currentTime;
    }

    /**
     * 提取一个月中的最后一天
     *
     * @param day
     * @return
     */
    public static Date getLastDate(long day) {
        Date date = new Date();
        long date_3_hm = date.getTime() - 3600000 * 34 * day;
        Date date_3_hm_date = new Date(date_3_hm);
        return date_3_hm_date;
    }

    /**
     * 得到现在时间
     *
     * @return 字符串 yyyyMMdd HHmmss
     */
    public static String getStringToday() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 得到现在小时
     */
    public static String getHour() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String hour;
        hour = dateString.substring(11, 13);
        return hour;
    }

    /**
     * 得到现在分钟
     *
     * @return
     */
    public static String getTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String min;
        min = dateString.substring(14, 16);
        return min;
    }

    /**
     * 根据用户传入的时间表示格式，返回当前时间的格式 如果是yyyyMMdd，注意字母y不能大写。
     *
     * @param sformat yyyyMMddhhmmss
     * @return
     */
    public static String getUserDate(String sformat) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(sformat);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * <br>
     * Description: 根据年月标识，返回前length个月的年月信息
     *
     * @param yearMonthTag
     * @param length
     * @return
     */
    public static List<YearMonthInfo> getYearMonthInfo(String yearMonthTag, int length, boolean isZh) {
        List<YearMonthInfo> yearMonthInfoList = new ArrayList<YearMonthInfo>();
        if (6 != yearMonthTag.length())
            return null;
        int month = Integer.parseInt(yearMonthTag.substring(4));
        Date date = strToDate(yearMonthTag);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for (int i = 0; i < length; i++) {
            YearMonthInfo yearMonthInfo = new YearMonthInfo();
            yearMonthInfo.tag = dateToStr(calendar.getTime());
            if (isZh) {
                yearMonthInfo.label = monthsZh[month - 1];
            } else {
                yearMonthInfo.label = monthsEn[month - 1];
            }
            month--;
            if (month == 0) {
                month = 12;
            }
            calendar.add(Calendar.MONTH, -1);
            yearMonthInfoList.add(yearMonthInfo);
        }
        return yearMonthInfoList;
    }

    /**
     * <br>
     * Description: 是否在两个日期之间
     *
     * @param dateStr1 yyyy-MM-dd
     * @param dateStr2 yyyy-MM-dd
     * @return
     */
    public static boolean isBetweenDate(String dateStr1, String dateStr2) {
        try {
            long date1 = strToDateShort(dateStr1).getTime();
            long date2 = strToDateShort(dateStr2).getTime();
            long now = new Date().getTime();
            if (date1 < now && now < date2) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * <br>
     * Description: 年月信息
     */
    public static class YearMonthInfo {
        /**
         * 年月标识，例201108,199912
         */
        public String tag;
        /**
         * 年月显示标签，中文:3月；英文：Mar
         */
        public String label;
    }

    ;

    /**
     * Description: 获取今天日期的毫秒数
     */
    public static long getTodayTime() {
        try {
            Date date = new Date();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = dateformat.format(date);
            return dateformat.parse(dateStr).getTime();
        } catch (Exception e) {

            return 0;
        }
    }

    /**
     * 日期字符串转换 pattern可以从R.string中获取，datetime_pattern_yyyymmddhhmmss, datetime_pattern_yyyy_mm_dd_hhmmss， date_pattern_chinese,datetime_pattern_chinese等
     *
     * @param originalPatternStrId 初始日期格式
     * @param targetPatternStrId   目标日期格式
     * @param datetime             日期字符串
     * @return
     */
    public static String formatDateTime(Context context, int originalPatternStrId, int targetPatternStrId, String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat(context.getString(originalPatternStrId), Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            sdf = new SimpleDateFormat(context.getString(targetPatternStrId), Locale.getDefault());
            String target = sdf.format(date);
            return target;
        }
        return null;

    }

    /**
     * getTimeByJavaToADZero
     *
     * @param @param  type
     * @param @param  time
     * @param @return
     * @return String
     * @throws
     * @Description: 从公元元年算起
     */
    public static String getTimeByJavaToADZero(String type, String time) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(type);
            Date data = dateFormat.parse(time);
            return data.getTime() + (long) (1970 * 365 + (25 * 20 - 8) - 19 + 5) * 24 * 60 * 60 * 1000 + "0000";

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 时间性能测试
     *
     * @param a 标记的 时间
     * @return
     */
    public static String testDate(long a) {
        long c = System.currentTimeMillis() - a;
        if (c < 1000) {
            return c + "毫秒";
        } else if (c > 1000 && c <= 60000) {
            return c / 1000 + "秒" + c % 1000 + "毫秒";
        } else if (c > 60000 && c <= 60000 * 60) {
            return c / 60000 + "分钟" + c % 60000 / 1000 + "秒" + c % 1000 + "毫秒";
        }
        return "0毫秒";
    }

    /**
     * 时间戳转化
     */

    public static String convert(long mill) {
        Date date = new Date(mill * 1000l);
        String strs = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            strs = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }

    public static String convert(String mill) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date d;
        try {
            d = sdf.parse(mill);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return re_time;
    }

    public static String getDateFormat(int dateTag) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, dateTag);
        SimpleDateFormat YearsFormat = new SimpleDateFormat("MM-dd");
        String date = YearsFormat.format(calendar.getTime());
        return date;
    }

    public static String getDateFormatHasYear(int dateTag) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, dateTag);
        SimpleDateFormat YearsFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = YearsFormat.format(calendar.getTime());
        return date;
    }

    public static String currentMonth() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");// 格式化对象
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return sdf.format(calendar.getTime());
    }

    public static boolean isDate(String str_input, String rDateFormat) {
        if (!isNull(str_input)) {
            SimpleDateFormat formatter = new SimpleDateFormat(rDateFormat);
            formatter.setLenient(false);
            try {
                formatter.format(formatter.parse(str_input));
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean isNull(String str) {
        if (str == null)
            return true;
        else
            return false;
    }

    public static String formateStringH(String dateStr, String pattren) {
        Date date = parseDate(dateStr, pattren);
        try {
            String str = dateToString(date, DateUtil.yyyyMMdd);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return dateStr;
        }
    }

    public static Date parseDate(String dateStr, String type) {
        SimpleDateFormat df = new SimpleDateFormat(type);
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String dateToString(Date date, String pattern)
            throws Exception {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 设置的需要判断的时间  //格式如2012-09-08
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */

//  String pTime = "2012-03-12";
    public static String getWeek(String pTime) {
        String Week = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            Week = "天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            Week = "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            Week = "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            Week = "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            Week = "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            Week = "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            Week = "六";
        }

        return "周" + Week;
    }

    /**
     * 添加天数
     *
     * @param num
     * @return
     */
    public static String addDateDay(Integer num, String type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, num);
        Date date = calendar.getTime();
        String datestr = simpleDateFormat.format(date);
        return datestr;
    }

    public static String timeToStringSpecial(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date(time));
    }

    public static String timeToStringSpecial2(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMdd);
        return sdf.format(new Date(time));
    }

    /**
     * @description：获取加一天
     * @author shicm
     * @date 2014年8月14日 上午11:16:50
     */
    public static String getTomorrowDate(long time) {
        Date currentTime = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * @description：获取加N天
     * @author shicm
     * @date 2014年8月14日 上午11:16:50
     */
    public static String getAfterSomeDate(long time, int addDay) {
        Date currentTime = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.DAY_OF_YEAR, addDay);
        Date date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Long getNowTime() {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        return date.getTime();
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNowTimeStr() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(System.currentTimeMillis());
        return time;
    }

    /**
     * 设置固定两位显示2017-02-08
     *
     * @param dateStr
     * @return
     */
    public static String dealDate(String dateStr) {
        StringBuilder sb = new StringBuilder();
        String[] dates = dateStr.split("-");
        String year;
        String month;
        String day;
        if (dates.length >= 2) {
            year = dates[0];
            month = dates[1];
            day = dates[2];
            if (month.length() == 1) {
                month = Integer.parseInt(month) < 10 ? String.format("0%s", month) : month;
            }
            if (day.length() == 1) {
                day = Integer.parseInt(day) < 10 ? String.format("0%s", day) : day;
            }
            sb.append(year).append("-").append(month).append("-").append(day);
        }
        return sb.toString();
    }

    /*
     * 将时间转换为时间戳
     */
    public static Long dateToStamp(String s) throws ParseException {
//        long res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
//        res = String.valueOf(ts);
        return ts;
    }

    public static Long dateToStamp(String s,String pattern) throws ParseException {
//        long res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
//        res = String.valueOf(ts);
        return ts;
    }

    public static String getTimeFormat(String str) {
        String intoStr = str.substring(5, str.length());
        String intoTimeStr = String.format("%s月%s日", intoStr.split("-")[0], intoStr.split("-")[1]);
        return intoTimeStr;
    }


    /**
     * 将时间转换为时间戳
     */
    public static String timeStamp2Date(long time, String format) throws android.net.ParseException {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    /**
     * 倒计时返回的格式 xx小时xx分钟xx秒
     */
    public static String cownTimeFormat(long millisUntilFinished) {
        int day = 0;
        int hour = 0;
        int minute = (int) (millisUntilFinished / 1000 / 60);
        int second = (int) (millisUntilFinished / 1000 % 60);
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }

        String strSecond = String.valueOf(second);
        if (strSecond.length() == 1) {
            strSecond = String.format("0%s", strSecond);
        }

        String strMinute = String.valueOf(minute);
        if (strMinute.length() == 1) {
            strMinute = String.format("0%s", strMinute);
        }

        String strHour = String.valueOf(hour);
        if (strHour.length() == 1) {
            strHour = String.format("0%s", strHour);
        }
        return strHour + ":" + strMinute + ":" + strSecond;
    }

    /**
     * 倒计时返回的格式 xx天
     */
    public static String cownTimeFormatDay(long millisUntilFinished) {
        int day = 0;
        int hour = 0;
        int minute = (int) (millisUntilFinished / 1000 / 60);
        int second = (int) (millisUntilFinished / 1000 % 60);
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }

        if (hour > 24) {
            day = hour / 24;
            hour = hour % 24;
        }
        String strDay = String.valueOf(day);
        if (strDay.length() == 1) {
            strDay = String.format("0%s", strDay);
        }
        return strDay;
    }

    /**
     * 倒计时返回的格式 xx小时
     */
    public static String cownTimeFormatHour(long millisUntilFinished) {
        int day = 0;
        int hour = 0;
        int minute = (int) (millisUntilFinished / 1000 / 60);
        int second = (int) (millisUntilFinished / 1000 % 60);
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }

        if (hour > 24) {
            day = hour / 24;
            hour = hour % 24;
        }
        String strHour = String.valueOf(hour);
        if (strHour.length() == 1) {
            strHour = String.format("0%s", strHour);
        }
        return strHour;
    }

    /**
     * 倒计时返回的格式 xx小时
     */
    public static String cownTimeFormatMinute(long millisUntilFinished) {
        int day = 0;
        int hour = 0;
        int minute = (int) (millisUntilFinished / 1000 / 60);
        int second = (int) (millisUntilFinished / 1000 % 60);
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }

        if (hour > 24) {
            day = hour / 24;
            hour = hour % 24;
        }
        String strMinute = String.valueOf(minute);
        if (strMinute.length() == 1) {
            strMinute = String.format("0%s", strMinute);
        }
        return strMinute;
    }

    /**
     * 倒计时返回的格式 xx小时
     */
    public static String cownTimeFormatSecond(long millisUntilFinished) {
        int day = 0;
        int hour = 0;
        int minute = (int) (millisUntilFinished / 1000 / 60);
        int second = (int) (millisUntilFinished / 1000 % 60);
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }

        if (hour > 24) {
            day = hour / 24;
            hour = hour % 24;
        }
        String strSecond = String.valueOf(second);
        if (strSecond.length() == 1) {
            strSecond = String.format("0%s", strSecond);
        }
        return strSecond;
    }

    /**
     * 浏览于 多少时间之前
     * 1：1-59分钟 返回分钟
     * 2：1-23小时 返回小时
     * 3：超过当天0点 返回日期
     */
    public static String broseTimeFormat(long millisUntilFinished) {
        String str = "";
        int day = 0;
        int hour = 0;
        int minute = (int) (millisUntilFinished / 1000 / 60);
//        int second = (int) (millisUntilFinished / 1000 % 60);
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        } else if (minute > 1 && minute < 59) {
            str = minute + "分钟";
        }

        if (hour > 24) {
            str = timeToStringSpecial2(millisUntilFinished);
        } else if (hour > 1 && hour < 24) {
            str = hour + "小时";
        } else {

        }

        return str;
    }


    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }


}
