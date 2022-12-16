package com.gzhealthy.health.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.gzhealthy.health.R;
import com.gzhealthy.health.tool.DateUtil;
import com.gzhealthy.health.tool.DateUtils;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

/**
 * Created by Justin_Liu
 * on 2021/4/23
 */
public class SleepCalendarView extends MonthView {
    /**
     * 自定义魅族标记的文本画笔
     */
    private Paint mTextPaint = new Paint();

    /**
     * 自定义魅族标记的圆形背景
     */
    private Paint mSchemeBasicPaint = new Paint();
    private float mRadio;
    private int mPadding;
    private float mSchemeBaseLine;
    private Context context;
    private java.util.Calendar mCalendar;
    private Paint grayPaint;


    private int mRadius;

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth , mItemHeight) /5 * 2;
        mSchemePaint.setStyle(Paint.Style.STROKE);
    }

    public SleepCalendarView(Context context) {
        super(context);
        this.context = context;

        mRadius = Math.min(mItemWidth , mItemHeight) /5 * 2;
        mSchemePaint.setStyle(Paint.Style.STROKE);

        mCalendar = java.util.Calendar.getInstance();
        grayPaint = new Paint();



        mTextPaint.setTextSize(dipToPx(context, 11));
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setFakeBoldText(true);

        mRadio = dipToPx(getContext(), 7);
        mPadding = dipToPx(getContext(), 4);
        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mRadio - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);

//        int w = getWidth();
//        int h = getHeight();
//        Logger.e("HealthyCalendarView","w "+w+"h "+h);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
//        mSelectedPaint.setStyle(Paint.Style.FILL);
//        mSelectedPaint.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
//        //选中背影为圆形
//        float center = mItemWidth / 2;
//        canvas.drawCircle(x+center,y+center,center-mItemWidth/6,mSelectedPaint);
//        Logger.e("111","getDay="+calendar.getDay());
////        canvas.drawRect(x + mPadding, y + mPadding, x + mItemWidth - mPadding, y + mItemHeight - mPadding, mSelectedPaint);
//        if (!calendar.isCurrentMonth())
//            return false;
//        return true;

        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        mSchemeBasicPaint.setColor(Color.parseColor("#DBFFFB"));
//        canvas.drawCircle(x + mItemWidth - mPadding - mRadio / 2, y + mPadding + mRadio, mRadio, mSchemeBasicPaint);
        canvas.drawRoundRect(x+mItemWidth/2, y ,x+mItemWidth,y + mItemWidth/3,10f,10f,mSchemeBasicPaint);
//        mTextPaint.setColor(context.getColor(R.color.colorPrimary));
        mTextPaint.setColor(context.getColor(R.color.colorPrimary));
        canvas.drawText(calendar.getScheme(), x + mItemWidth/2 + 5, y + mPadding + mSchemeBaseLine, mTextPaint);

    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
//        int cx = x + mItemWidth / 2;
//        int top = y - mItemHeight / 6;
//
//        if (isSelected) {//优先绘制选择的
//            mSelectTextPaint.setColor(ContextCompat.getColor(context,R.color.white));
//            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top + mItemHeight/6,
//                    mSelectTextPaint);
//
//            //农历日期 隐藏
////            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mSelectedLunarTextPaint);
//
//
//        } else if (hasScheme) {//否则绘制具有标记的
//            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
//                    calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);
//
//            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mCurMonthLunarTextPaint);
//        } else {//最好绘制普通文本
//            Paint paint = null;
//            if (calendar.isCurrentDay()){
//                paint = mCurDayTextPaint;
////                float stroke = paint.getStrokeWidth();
////                paint.setStrokeWidth(stroke-3);
//                paint.setColor(ContextCompat.getColor(context,R.color.white));
//
//                //未被选中的今天日期
//                float center = mItemWidth / 2;
//                mSelectedPaint.setColor(ContextCompat.getColor(context,R.color.global_x));
//                canvas.drawCircle(x+center,y+center,center-mItemWidth/5,mSelectedPaint);
//            }else if (calendar.isCurrentMonth()){
//                paint = mCurMonthTextPaint;
////                paint.setColor(ContextCompat.getColor(context,R.color.album_ColorPrimaryDark));
//            }else {
//                paint = mOtherMonthTextPaint;
//                paint.setColor(Color.RED);
//            }
//            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top + mItemHeight/6,paint);
//
//
//            //农历日期 隐藏
////            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10,
////                    calendar.isCurrentDay() ? mCurDayLunarTextPaint :
////                            calendar.isCurrentMonth() ? mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint);
//        }
        mCurMonthTextPaint.setColor(Color.parseColor("#333333"));
        mOtherMonthTextPaint.setColor(Color.parseColor("#FFFFFF"));
        grayPaint.set(mOtherMonthTextPaint);
        grayPaint.setColor(Color.parseColor("#999999"));
//        Log.e("111","month="+calendar.getMonth()+"day=="+calendar.getDay());
        String text = String.valueOf(calendar.getDay());
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;

        if (isSelected){
            canvas.drawText(text,
                    cx,
                    baselineY,
                    mSelectTextPaint);
        }else if (hasScheme){
            canvas.drawText(text, cx, baselineY, mCurMonthTextPaint/*calendar.isCurrentDay() ? mCurDayTextPaint : calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint*/);
        }else {
            if (calendar.isCurrentMonth()){
                canvas.drawText(text,cx,baselineY,mCurMonthTextPaint);
                String calendarDate = DateUtil.getStringDate(calendar.getTimeInMillis());
                if (DateUtils.compareDate(calendarDate,DateUtil.getStringDate()/*DateUtil.scrollCalendarDate(DateUtil.getStringDate(),-1)*/)){
                    canvas.drawText(text,cx,baselineY,grayPaint);
                }
            }
        }
    }


    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
