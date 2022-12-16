package com.gzhealthy.health.widget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GregorianToLunarCalendarUtils {
    private boolean leap;
    public int leapMonth = 0; // 闰的是哪个月

    static SimpleDateFormat chineseDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    final static long[] lunarInfo = new long[]{
            0x4bd8, 0x4ae0, 0xa570, 0x54d5, 0xd260, 0xd950, 0x5554, 0x56af, 0x9ad0, 0x55d2,
            0x4ae0, 0xa5b6, 0xa4d0, 0xd250, 0xd255, 0xb54f, 0xd6a0, 0xada2, 0x95b0, 0x4977,
            0x497f, 0xa4b0, 0xb4b5, 0x6a50, 0x6d40, 0xab54, 0x2b6f, 0x9570, 0x52f2, 0x4970,
            0x6566, 0xd4a0, 0xea50, 0x6a95, 0x5adf, 0x2b60, 0x86e3, 0x92ef, 0xc8d7, 0xc95f,
            0xd4a0, 0xd8a6, 0xb55f, 0x56a0, 0xa5b4, 0x25df, 0x92d0, 0xd2b2, 0xa950, 0xb557,
            0x6ca0, 0xb550, 0x5355, 0x4daf, 0xa5b0, 0x4573, 0x52bf, 0xa9a8, 0xe950, 0x6aa0,
            0xaea6, 0xab50, 0x4b60, 0xaae4, 0xa570, 0x5260, 0xf263, 0xd950, 0x5b57, 0x56a0,
            0x96d0, 0x4dd5, 0x4ad0, 0xa4d0, 0xd4d4, 0xd250, 0xd558, 0xb540, 0xb6a0, 0x95a6,
            0x95bf, 0x49b0, 0xa974, 0xa4b0, 0xb27a, 0x6a50, 0x6d40, 0xaf46, 0xab60, 0x9570,
            0x4af5, 0x4970, 0x64b0, 0x74a3, 0xea50, 0x6b58, 0x5ac0, 0xab60, 0x96d5, 0x92e0,
            0xc960, 0xd954, 0xd4a0, 0xda50, 0x7552, 0x56a0, 0xabb7, 0x25d0, 0x92d0, 0xcab5,
            0xa950, 0xb4a0, 0xbaa4, 0xad50, 0x55d9, 0x4ba0, 0xa5b0, 0x5176, 0x52bf, 0xa930,
            0x7954, 0x6aa0, 0xad50, 0x5b52, 0x4b60, 0xa6e6, 0xa4e0, 0xd260, 0xea65, 0xd530,
            0x5aa0, 0x76a3, 0x96d0, 0x4afb, 0x4ad0, 0xa4d0, 0xd0b6, 0xd25f, 0xd520, 0xdd45,
            0xb5a0, 0x56d0, 0x55b2, 0x49b0, 0xa577, 0xa4b0, 0xaa50, 0xb255, 0x6d2f, 0xada0,
            0x4b63, 0x937f, 0x49f8, 0x4970, 0x64b0, 0x68a6, 0xea5f, 0x6b20, 0xa6c4, 0xaaef,
            0x92e0, 0xd2e3, 0xc960, 0xd557, 0xd4a0, 0xda50, 0x5d55, 0x56a0, 0xa6d0, 0x55d4,
            0x52d0, 0xa9b8, 0xa950, 0xb4a0, 0xb6a6, 0xad50, 0x55a0, 0xaba4, 0xa5b0, 0x52b0,
            0xb273, 0x6930, 0x7337, 0x6aa0, 0xad50, 0x4b55, 0x4b6f, 0xa570, 0x54e4, 0xd260,
            0xe968, 0xd520, 0xdaa0, 0x6aa6, 0x56df, 0x4ae0, 0xa9d4, 0xa4d0, 0xd150, 0xf252,
            0xd520};


    // 农历部分假日
    final static String[] lunarHoliday = new String[]{
            "0101 春节",
            "0115 元宵节",
            "0505 端午节",
            "0707 七夕",
            "0715 中元节",
            "0730 地藏节",
            "0802 灶君诞",
            "0815 中秋节",
            "0827 先师诞",
            "0909 重阳节",
            "1208 腊八节",
            "1223 小年",
            "0100 除夕"};

    // 公历部分节假日
    final static String[] solarHoliday = new String[]{
            "0101 元旦",
            "0214 情人节",
            "0308 妇女节",
            "0312 植树节",
            "0401 愚人节",
            "0501 劳动节",
            "0504 青年节",
            "0512 护士节",
            "0601 儿童节",
            "0701 建党节", //1921
            "0801 建军节", //1933
            //"0808 父亲节",
            "0910 教师节",
            "1001 国庆节",
            "1006 老人节",
            "1224 平安夜",
            "1225 圣诞节"};
    //24节气
    final static String[] sTermInfo = new String[]{
            // 时节     气候
            "小寒", "大寒",
            "立春", "雨水",
            "惊蛰", "春分",
            "清明", "谷雨",
            "立夏", "小满",
            "芒种", "夏至",
            "小暑", "大暑",
            "立秋", "处暑",
            "白露", "秋分",
            "寒露", "霜降",
            "立冬", "小雪",
            "大雪", "冬至"
    };


    // ====== 传回农历 y年的总天数 1900--2100
    public int yearDays(int y) {
        int i, sum = 348;
        for (i = 0x8000; i > 0x8; i >>= 1) {
            if ((lunarInfo[y - 1900] & i) != 0)
                sum += 1;
        }
        return (sum + leapDays(y));
    }

    // ====== 传回农历 y年闰月的天数
    public int leapDays(int y) {
        if (leapMonth(y) != 0) {
            if ((lunarInfo[y - 1899] & 0xf) != 0)
                return 30;
            else
                return 29;
        } else
            return 0;
    }

    // ====== 传回农历 y年闰哪个月 1-12 , 没闰传回 0
    public int leapMonth(int y) {
        long var = lunarInfo[y - 1900] & 0xf;
        return (int) (var == 0xf ? 0 : var);
    }

    // ====== 传回农历 y年m月的总天数
    public int monthDays(int y, int m) {
        if ((lunarInfo[y - 1900] & (0x10000 >> m)) == 0)
            return 29;
        else
            return 30;
    }

    /*
     * 计算公历nY年nM月nD日和bY年bM月bD日渐相差多少天
     * */
    public int getDaysOfTwoDate(int bY, int bM, int bD, int nY, int nM, int nD) {
        Date baseDate = null;
        Date nowaday = null;

        try {
            baseDate = chineseDateFormat.parse(bY + "年" + bM + "月" + bD + "日");
            nowaday = chineseDateFormat.parse(nY + "年" + nM + "月" + nD + "日");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 求出相差的天数
        int offset = (int) ((nowaday.getTime() - baseDate.getTime()) / 86400000L);
        return offset;
    }

    /*
     * 将公历year年month月day日转换成农历
     * 返回格式为20140506（int）
     * */
    public int getLunarDateINT(int year, int month, int day) {
        int iYear, LYear, LMonth, LDay, daysOfYear = 0;
        // 求出和1900年1月31日相差的天数
        //year =1908;
        //month = 3;
        //day =3;
        int offset = getDaysOfTwoDate(1900, 1, 31, year, month, day);
        //Log.i("--ss--","公历:"+year+"-"+month+"-"+day+":"+offset);
        // 用offset减去每农历年的天数
        // 计算当天是农历第几天
        // i最终结果是农历的年份
        // offset是当年的第几天
        for (iYear = 1900; iYear < 2100 && offset > 0; iYear++) {
            daysOfYear = yearDays(iYear);
            offset -= daysOfYear;
            //Log.i("--ss--","农历:"+iYear+":"+daysOfYear+"/"+offset);
        }

        if (offset < 0) {
            offset += daysOfYear;
            iYear--;
            //Log.i("--ss--","农历:"+iYear+":"+daysOfYear+"/"+offset);
        }
        // 农历年份
        LYear = iYear;

        leapMonth = leapMonth(iYear); // 闰哪个月,1-12
        leap = false;

        // 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
        int iMonth = 1, daysOfMonth = 0;
        for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
            // 闰月
            if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
                --iMonth;
                leap = true;
                daysOfMonth = leapDays(iYear);
            } else {
                daysOfMonth = monthDays(iYear, iMonth);
            }
            // 解除闰月
            if (leap && iMonth == (leapMonth + 1)) leap = false;

            offset -= daysOfMonth;
            //Log.i("--ss--","农历:"+iYear+"-"+iMonth+":"+daysOfMonth+"/"+offset);
        }
        // offset为0时，并且刚才计算的月份是闰月，要校正
        if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
            if (leap) {
                leap = false;
            } else {
                leap = true;
                --iMonth;
            }
        }
        // offset小于0时，也要校正
        if (offset < 0) {
            offset += daysOfMonth;
            --iMonth;
            //Log.i("--ss--","农历:"+iYear+"-"+iMonth+":"+daysOfMonth+"/"+offset);
        }
        LMonth = iMonth;
        LDay = offset + 1;
        //Log.i("--ss--","农历:"+LYear+"-"+LMonth+"-"+LDay);
        return LYear * 10000 + LMonth * 100 + LDay;
    }

    public String getFestival(int year, int month, int day) {//获取公历对应的节假日或二十四节气

        //农历节假日
        int var = getLunarDateINT(year, month, day);
        int lun_year = (int) var / 10000;
        int lun_month = (int) (var % 10000) / 100;
        int lun_Day = (int) (var - lun_year * 10000 - lun_month * 100);
        for (int i = 0; i < lunarHoliday.length; i++) {
            // 返回农历节假日名称
            String ld = lunarHoliday[i].split(" ")[0]; // 节假日的日期
            String ldv = lunarHoliday[i].split(" ")[1]; // 节假日的名称
            String lmonth_v = lun_month + "";
            String lday_v = lun_Day + "";
            String lmd = "";
            if (month < 10) {
                lmonth_v = "0" + lun_month;
            }
            if (day < 10) {
                lday_v = "0" + lun_Day;
            }
            lmd = lmonth_v + lday_v;
            if (ld.trim().equals(lmd.trim())) {
                return ldv;
            }
        }

        //公历节假日
        for (int i = 0; i < solarHoliday.length; i++) {
            if (i == solarHoliday.length && year < 1893 //1893年前没有此节日
                    || i + 3 == solarHoliday.length && year < 1999
                    || i + 6 == solarHoliday.length && year < 1942
                    || i + 10 == solarHoliday.length && year < 1949
                    || i == 19 && year < 1921
                    || i == 20 && year < 1933
                    || i == 22 && year < 1976) {
                break;
            }
            // 返回公历节假日名称
            String sd = solarHoliday[i].split(" ")[0]; // 节假日的日期
            String sdv = solarHoliday[i].split(" ")[1]; // 节假日的名称
            String smonth_v = month + "";
            String sday_v = day + "";
            String smd = "";
            if (month < 10) {
                smonth_v = "0" + month;
            }
            if (day < 10) {
                sday_v = "0" + day;
            }
            smd = smonth_v + sday_v;
            if (sd.trim().equals(smd.trim())) {
                return sdv;
            }
        }


        int b = getDateOfSolarTerms(year, month);
        if (day == (int) b / 100) {
            return sTermInfo[(month - 1) * 2];
        } else if (day == b % 100) {
            return sTermInfo[(month - 1) * 2 + 1];
        }
        return "";
    }

    private int sTerm(int y, int n) {
        int[] sTermInfo = new int[]{0, 21208, 42467, 63836, 85337, 107014,
                128867, 150921, 173149, 195551, 218072, 240693,
                263343, 285989, 308563, 331033, 353350, 375494,
                397447, 419210, 440795, 462224, 483532, 504758};
        Calendar cal = Calendar.getInstance();
        cal.set(1900, 0, 6, 2, 5, 0);
        long temp = cal.getTime().getTime();
        cal.setTime(new Date((long) ((31556925974.7 * (y - 1900) + sTermInfo[n] * 60000L) + temp)));
        int a = cal.get(Calendar.DAY_OF_MONTH);
        return a;
    }

    public int getDateOfSolarTerms(int year, int month) {
        int a = sTerm(year, (month - 1) * 2);
        int b = sTerm(year, (month - 1) * 2 + 1);
        return a * 100 + b;
        //return 0;
    }


    /**
     * 用于获取中国的传统节日
     *
     * @param gyear  公历的年
     * @param gmonth 公历的月
     * @param gday   公历日
     * @return 中国传统节日
     */
    public String getChinaCalendarMsg(int gyear, int gmonth, int gday) {


        int date = getLunarDateINT(gyear, gmonth, gday);

        int year = (int) date / 10000;
        int month = (int) (date % 10000) / 100;
        int day = (int) (date - year * 10000 - month * 100);

        String message = "";
        if (((month) == 1) && day == 1) {
            message = "春节";
        } else if (((month) == 1) && day == 15) {
            message = "元宵";
        } else if (((month) == 5) && day == 5) {
            message = "端午";
        } else if ((month == 7) && day == 7) {
            message = "七夕";
        } else if (((month) == 8) && day == 15) {
            message = "中秋";
        } else if ((month == 9) && day == 9) {
            message = "重阳";
        } else if ((month == 12) && day == 8) {
            message = "腊八";
        } else if ((month == 12) && day == 23) {
            message = "小年";
        } else {
            if (month == 12) {
                if ((((monthDays(year, month) == 29) && day == 29)) || ((((monthDays(year, month) == 30) && day == 30)))) {
                    message = "除夕";
                }
            }
        }
        return message;
    }
}
