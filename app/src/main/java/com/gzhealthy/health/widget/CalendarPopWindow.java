package com.gzhealthy.health.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.base.RxManager;
import com.gzhealthy.health.logger.Logger;
import com.gzhealthy.health.tool.DateUtil;
import com.gzhealthy.health.tool.DateUtils;
import com.gzhealthy.health.utils.ToastUtil;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * Created by Justin_Liu
 * on 2021/4/25
 */
public class CalendarPopWindow extends PopupWindow implements CalendarView.OnMonthChangeListener
        ,CalendarView.OnCalendarInterceptListener
        ,CalendarView.OnCalendarSelectListener{

    private RxManager rxManager;
    private final String[] MONTHS_LUNAR_ARRY ={"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月",};
    private CalendarView mCalendarView;
    public OnSelectCalendarCallback onSelectCalendarCallback;
    private TextView months_lunar;
    private boolean isOverNoSelect = false;

    public CalendarPopWindow(Context context,RxManager rxManager,OnSelectCalendarCallback onSelectCalendarCallback) {
        super(context);
        initView(context);
        this.onSelectCalendarCallback = onSelectCalendarCallback;
        this.rxManager = rxManager;
        this.rxManager.onRxEvent(RxEvent.ON_SCROLL_CALENDAR_DATE)
                .subscribe(o -> {
            String[] calendarInfo = ((String) o).split("-");
            int year = Integer.parseInt(calendarInfo[0]);
            int month = Integer.parseInt(calendarInfo[1]);
            int day = Integer.parseInt(calendarInfo[2]);
            mCalendarView.scrollToCalendar(year,month,day);
            mCalendarView.update();
        });
    }


    /**
     * @param context
     * @param rxManager
     * @param onSelectCalendarCallback
     * @param isOverNoSelect  超过当天日期不可选
     */
    public CalendarPopWindow(Context context,RxManager rxManager, OnSelectCalendarCallback onSelectCalendarCallback,boolean isOverNoSelect) {
        super(context);
        this.isOverNoSelect = isOverNoSelect;
        this.onSelectCalendarCallback = onSelectCalendarCallback;
        this.rxManager = rxManager;
        initView(context);
        this.rxManager.onRxEvent(RxEvent.ON_SCROLL_CALENDAR_DATE)
                .subscribe(o -> {
                    String[] calendarInfo = ((String) o).split("-");
                    int year = Integer.parseInt(calendarInfo[0]);
                    int month = Integer.parseInt(calendarInfo[1]);
                    int day = Integer.parseInt(calendarInfo[2]);
                    mCalendarView.scrollToCalendar(year,month,day);
                    mCalendarView.update();
                });
    }

    private void initView(Context context){
        int resouce = isOverNoSelect ? R.layout.popup_sleep_calendar_window : R.layout.popup_calendar_window;
        View contentView = LayoutInflater.from(context).inflate(resouce,null);
        setContentView(contentView);
        Drawable drawable = new ColorDrawable();
        setBackgroundDrawable(drawable);
        setOutsideTouchable(true);

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        mCalendarView =contentView.findViewById(R.id.calendarView);


        //最大日期范围是当年的一整年
        mCalendarView.setRange(
                mCalendarView.getCurYear(),1,1,mCalendarView.getCurYear(),12,31);
        mCalendarView.scrollToCalendar(mCalendarView.getCurYear(),mCalendarView.getCurMonth(),mCalendarView.getCurDay());
        if (isOverNoSelect){//睡眠日历模式
            //TODO  若当前为月份第一天时月份要往前一个月,而天数需设为上一个月最后一天
            int day = 0, month = 0 , year = 0;
            String date = DateUtil.scrollCalendarDate(DateUtil.getStringDate(),-1);
            String[] arry = date.split("-");
            day = Integer.parseInt(arry[2]);//截取天数
            month = Integer.parseInt(arry[1]);
            year = Integer.parseInt(arry[0]);

            Map<String,Calendar> map = new HashMap<>();
            map.put(getSchemeCalendar(
                    year < mCalendarView.getCurYear() ? year: mCalendarView.getCurYear()
                    , month
                    , day
                    , Color.parseColor("#00D0B9")
                    ,"昨晚").toString(),
                    getSchemeCalendar(
                            year < mCalendarView.getCurYear() ? year: mCalendarView.getCurYear() ,
                            month,
                            day,
                            Color.parseColor("#00D0B9"),"昨夜"));
            mCalendarView.setSchemeDate(map);
            mCalendarView.setRange(
                    year < mCalendarView.getCurYear() ? year: mCalendarView.getCurYear(),1,1,
                    mCalendarView.getCurYear(),12,31);
            mCalendarView.scrollToCalendar(mCalendarView.getCurYear(),mCalendarView.getCurMonth(),mCalendarView.getCurDay()-1);
        }

        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnCalendarInterceptListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        months_lunar = contentView.findViewById(R.id.months_lunar);
        months_lunar.setText(mCalendarView.getCurYear()+"年"+MONTHS_LUNAR_ARRY[mCalendarView.getCurMonth()-1]);
        View bg = contentView.findViewById(R.id.bg);
        bg.setOnClickListener(v -> {
            dismiss();
        });

    }


    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        if (isClick){
            onSelectCalendarCallback.onSelect(
                    calendar.getYear()+"-"+(calendar.getMonth()<10?"0":"")+calendar.getMonth()
                            +"-"+(calendar.getDay()<10?"0":"")+calendar.getDay());

            dismiss();
        }else {
//            Logger.e("onCalendarSelect=="+calendar.getYear()+"-"+(calendar.getMonth()<10?"0":"")+calendar.getMonth()
//                    +"-"+(calendar.getDay()<10?"0":"")+calendar.getDay());
        }
    }

    @Override
    public void onMonthChange(int year, int month) {
//        Log.e("111","onMonthChange  month="+month);
        if (months_lunar!=null){
            months_lunar.setText(year+"年"+MONTHS_LUNAR_ARRY[month-1]);
        }
    }

    public void onDestroy(){
        if (rxManager!=null){
            rxManager.clear();
        }
        rxManager = null;
    }

    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        String calendarDate = DateUtil.getStringDate(calendar.getTimeInMillis());
        if (DateUtils.compareDate(calendarDate, DateUtil.getStringDate())){
            //TODO 超过范围日期禁止点击
            return true;
        }

        return false;
    }

    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {

    }

    public interface OnSelectCalendarCallback{
        void onSelect(String day);
    }


    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
//        calendar.addScheme(0xFF008800, "假");
//        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }

}
