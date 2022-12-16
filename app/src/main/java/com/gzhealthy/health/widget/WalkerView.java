package com.gzhealthy.health.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.gzhealthy.health.R;
import com.gzhealthy.health.utils.DispUtil;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * Created by Justin_Liu
 * on 2021/4/26
 */
public class WalkerView extends View {
    /**
     * 动画持续时间
     */
    private static final int ANIM_DURATION = 1800;
    private Context context;
    /**
     * 圆环前景画笔
     */
    private Paint circlePaint;
    /**
     * 圆环背景画笔
     */
    private Paint circleBackgroundPaint;
    /**
     * 步数画笔
     */
    private Paint walkTextPaint;
    /**
     * 图标画笔
     */
    private Paint iconPaint;
    /**
     * 备注画笔
     */
    private Paint descPaint;
    private Paint descCalPaint;
    private Paint caloriePaint;
    private Paint ovalPaint;


    private float CircleStorkeWidth = 28;
    private float circleCenter = 0;
    private float centerX = 0,centerY = 0;
    private RectF rectF;
    private RectF calorieRect;
    private RectF kiloRect;

    private float walk = 0;
    private int targetWalks = 10000;//目标步数
    private float targetAngel = 0;
    private float START_ANGEL = 90;
    private float icon_run_w = 0;
    private float icon_run_h = 0;

    private float icon_cal_w = 0;
    private float icon_cal_h = 0;

    private float icon_kilo_w = 0;
    private float icon_kilo_h = 0;

    private float icon_Oval_w = 0;
    private float icon_Oval_h = 0;

    private Bitmap icon_run = null;
    private Bitmap icon_calorie = null;
    private Bitmap icon_kilo = null;
    private Shader mLinearGradient= null;
    private Bitmap icon_Oval = null;

    private String calorie;
    private String kilo;

    /**
     *  运动圆的轨迹X
     */
    private double x;
    /**
     *  运动圆的轨迹Y
     */
    private double y;

    /**
     * 圆心坐标 X
     */
    private float x1;
    /**
     * 圆心坐标 Y
     */
    private float y1;


//    /**
//     * 直径
//     */
//    private float r1;
    /**
     * 半径
     */
    private float r_run;

    public WalkerView(Context context) {
        super(context);
        init(context);
    }

    public WalkerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WalkerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        float offset = dp(context,30);
        float rfl = centerX - circleCenter;
        float rfr = centerX + circleCenter;
        float rft = rfl;
        float rfb = centerY + circleCenter;;
        rectF = new RectF(rfl,rft,rfr,rfb);

        int rt = (int) (rfb + dp(context,60));
        int rb = (int) (rfb + dp(context,180));
        calorieRect = new RectF(rfl,rt,centerX,rb);
        kiloRect = new RectF(centerX,rt,rfr,rb);

        //圆环背景
        canvas.drawArc(rectF,0,360,false,circleBackgroundPaint);

        //圆环前景
        if (this.walk > 0){
            canvas.drawArc(rectF,START_ANGEL,targetAngel,false,circlePaint);
        }

        //步数
        Paint.FontMetrics fontMetrics=walkTextPaint.getFontMetrics();
        float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        float baseline=rectF.centerY()+distance;
        String walks = String.valueOf((int)this.walk);
//        if (walk>=targetWalks){
//            walks = walks.substring(0,1) + "," + walks.substring(1);
//        }
        canvas.drawText(walks,rectF.centerX(),baseline,walkTextPaint);


        float descBaselineOffset = 15;
        //当前步数
        canvas.drawText(/*getDesc(R.string.rings_desc)*/"步",rectF.centerX(),baseline + DispUtil.dipToPx(context,descBaselineOffset),descPaint);


        //人形icon
        canvas.drawBitmap(icon_run,
                centerX - (icon_run_w / 2),
                centerY - (icon_run_h + DispUtil.dipToPx(context,15)),
                iconPaint);


//        //卡路里icon
//        canvas.drawBitmap(icon_calorie, calorieRect.centerX() - (icon_cal_w / 2), calorieRect.top, iconPaint);
//
//
//        Paint.FontMetrics caloriefontMetrics=caloriePaint.getFontMetrics();
//        //卡路里数值
//        if (!TextUtils.isEmpty(calorie))
//            canvas.drawText(calorie+"千卡",calorieRect.centerX(),calorieRect.top+ icon_cal_h + Math.abs(caloriefontMetrics.top) + caloriefontMetrics.bottom,caloriePaint);
////            canvas.drawText(calorie+"千卡",rectF.centerX(),
////                    rectF.bottom + icon_cal_h + DispUtil.dipToPx(context,80),caloriePaint);
//
//        Paint.FontMetrics calorieDescfontMetrics=caloriePaint.getFontMetrics();
//        //卡路里desc
//        canvas.drawText(getDesc(R.string.rings_cal_desc),
//                calorieRect.centerX(),
//                calorieRect.top+ icon_cal_h + Math.abs(caloriefontMetrics.top) + caloriefontMetrics.bottom + calorieDescfontMetrics.bottom + Math.abs(calorieDescfontMetrics.top),descCalPaint);
//
//        //公里icon
//        canvas.drawBitmap(icon_kilo, kiloRect.centerX() - (icon_kilo_w / 2), kiloRect.top, iconPaint);
//
//        Paint.FontMetrics kilofontMetrics=caloriePaint.getFontMetrics();
//        if (!TextUtils.isEmpty(kilo))
//            canvas.drawText(kilo+"米",kiloRect.centerX(),kiloRect.top+ icon_kilo_h + Math.abs(kilofontMetrics.top) + kilofontMetrics.bottom,caloriePaint);
//
//        Paint.FontMetrics kiloDescfontMetrics=caloriePaint.getFontMetrics();
//        canvas.drawText(getDesc(R.string.rings_kilo_desc),
//                kiloRect.centerX(),
//                kiloRect.top+ icon_kilo_h + Math.abs(kiloDescfontMetrics.top) + kiloDescfontMetrics.bottom + kiloDescfontMetrics.bottom + Math.abs(kiloDescfontMetrics.top),descCalPaint);
//
//
//        //圆环的白点
        canvas.rotate(targetAngel, centerX, centerY);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawBitmap(icon_Oval,centerX - (icon_Oval_w / 2),rectF.bottom -(icon_Oval_h/2),iconPaint);

//        Paint rectpaint = new Paint();
//        rectpaint.setStyle(Paint.Style.STROKE);
//        rectpaint.setStrokeWidth(5f);
//        rectpaint.setColor(Color.parseColor("#ff5722"));
//        canvas.drawRect(calorieRect,rectpaint);
//
//        rectpaint.setColor(Color.parseColor("#2196f3"));
//        canvas.drawRect(rectF,ovalPaint);
//
//        rectpaint.setColor(Color.parseColor("#ffeb3b"));
//        canvas.drawRect(rectF,rectpaint);



    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int minimumWidth = getSuggestedMinimumWidth();
        final int minimumHeight = getSuggestedMinimumHeight();

        int height = measureHeight(minimumHeight,heightMeasureSpec);
        circleCenter = height/2;
        centerX = centerY = circleCenter;// + (CircleStorkeWidth/2);
        circleCenter -= CircleStorkeWidth;
        setMeasuredDimension(height, height);
    }

    private void init(Context context){
        this.context = context;

//        setBackgroundColor(context.getResources().getColor(R.color.white,null));
//        setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
//        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//        Log.e("111", "widthPixels==="+metrics.widthPixels);
//        CircleStorkeWidth = DispUtil.dp2px(context,10);
//        circleCenter = DispUtil.dp2px(context,120)/2;//metrics.widthPixels/8;
//        centerX = centerY = circleCenter + (CircleStorkeWidth/2);
//        circleCenter -= DispUtil.dp2px(context,10);

        float offset = dp(context,30);
        float rfl = centerX - circleCenter;
        float rfr = centerX + circleCenter;
        float rft = rfl;
        float rfb = centerY + circleCenter;;
        rectF = new RectF(rfl,rft,rfr,rfb);

        int rt = (int) (rfb + dp(context,60));
        int rb = (int) (rfb + dp(context,180));
        calorieRect = new RectF(rfl,rt,centerX,rb);
        kiloRect = new RectF(centerX,rt,rfr,rb);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(context.getResources().getColor(R.color.ring_bg,null));
        circlePaint.setStrokeWidth(CircleStorkeWidth);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);

        int color_e = context.getResources().getColor(R.color.walk_shader_e,null);
        int color_s = context.getResources().getColor(R.color.walk_shader_s,null);
        mLinearGradient = new LinearGradient(
                rectF.right - rectF.left,
                rectF.bottom,
                rectF.right,
                rectF.bottom - rectF.top,
                color_e,
                color_s,
                Shader.TileMode.CLAMP);
        circlePaint.setShader(mLinearGradient);

        circleBackgroundPaint = new Paint();
        circleBackgroundPaint.setAntiAlias(true);
        circleBackgroundPaint.setStyle(Paint.Style.STROKE);
        circleBackgroundPaint.setColor(context.getResources().getColor(R.color.ring_bg,null));
        circleBackgroundPaint.setStrokeWidth(CircleStorkeWidth);

        walkTextPaint = new Paint();
        walkTextPaint.setColor(Color.parseColor("#333333"));
        walkTextPaint.setTextSize(DispUtil.sp2px(context,28));
        walkTextPaint.setAntiAlias(true);
        walkTextPaint.setTextAlign(Paint.Align.CENTER);
//        walkTextPaint.setTypeface(Typeface.MONOSPACE);
        walkTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

        descPaint = new Paint();
        descPaint.setTextAlign(Paint.Align.CENTER);
        descPaint.setTextSize(DispUtil.sp2px(context,10));
        descPaint.setColor(context.getResources().getColor(R.color.global_999999,null));
        descPaint.setAntiAlias(true);


        iconPaint = new Paint();
        icon_run = getIconBmp(context,R.mipmap.icon_run_small);
        icon_run_w = icon_run.getWidth();
        icon_run_h = icon_run.getHeight();

        icon_calorie = getIconBmp(context,R.mipmap.icon_calorie);
        icon_cal_w = icon_calorie.getWidth();
        icon_cal_h = icon_calorie.getHeight();

        icon_kilo = getIconBmp(context,R.mipmap.icon_distance);
        icon_kilo_w = icon_kilo.getWidth();
        icon_kilo_h = icon_kilo.getHeight();

        icon_Oval = getIconBmp(context,R.mipmap.icon_sport_oval_small);
        icon_Oval_w = icon_Oval.getWidth();
        icon_Oval_h = icon_Oval.getHeight();

        caloriePaint = new Paint();
        caloriePaint.setAntiAlias(true);
        caloriePaint.setColor(context.getResources().getColor(R.color.global_333333,null));
        caloriePaint.setTextSize(DispUtil.sp2px(context,17));
        caloriePaint.setTextAlign(Paint.Align.CENTER);
        caloriePaint.setTypeface(Typeface.MONOSPACE);
        caloriePaint.setTypeface(Typeface.DEFAULT_BOLD);


        descCalPaint = new Paint();
        descCalPaint.setTextAlign(Paint.Align.CENTER);
        descCalPaint.setTextSize(DispUtil.sp2px(context,12));
        descCalPaint.setColor(context.getResources().getColor(R.color.global_999999,null));
        descCalPaint.setAntiAlias(true);

//        ovalPaint = new Paint();
//        ovalPaint.setColor(ContextCompat.getColor(context,R.color.red));
//        ovalPaint.setStyle(Paint.Style.STROKE);
//        ovalPaint.setStrokeWidth(1f);

        //小圆点路径设置
//        r_run = metrics.widthPixels / 3;
//        x = metrics.widthPixels /2;
//        x1 = (float) x;
//        y1 = metrics.widthPixels /2;//(rectF.bottom - rectF.top) / 2;
//        y = y1 - r_run + (rectF.bottom - rectF.top);
    }

    /**
     * 构造图片资源
     * @param context
     * @param resId
     * @return
     */
    private Bitmap getIconBmp(Context context,int resId){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),resId);
        return bitmap;
    }


    private String getDesc(int resId){
        return context.getString(resId);
    }


    /**
     * 步数
     * @param walk
     */
    public void setWalk(float walk) {
        this.walk = walk;
        if (walk <= 0){
            //没有数据时，圆点位置复位
            targetAngel = 0;
            postInvalidate();
            return;
        }
        float Angel = (this.walk / targetWalks) * 360;
        startAnim(Angel);
    }


    /**
     * 卡路里
     * @param calorie
     */
    public void setCalorie(float calorie) {
        String calories = String.valueOf(calorie);
        if (calorie > 1000){
            calories = String.valueOf(calorie);
            calories = calories.substring(0,1) +","+ calories.substring(1);
        }

        this.calorie = calories;
        postInvalidate();
    }


    /**
     * 运动距离
     * @param kilo
     */
    public void setKilo(float kilo) {
        this.kilo = String.valueOf(kilo);
        postInvalidate();
    }


    /**
     * 开始绘制动画
     * @param angel
     */
    public void startAnim(float angel){
        final ValueAnimator targetAnimator = ValueAnimator.ofFloat(0,angel);
        targetAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            targetAngel = value;
            postInvalidate();

        });

        targetAnimator.setDuration(ANIM_DURATION).start();

        final ValueAnimator walkAnimator = ValueAnimator.ofInt(0,(int)this.walk);
        walkAnimator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            WalkerView.this.walk = value;
            postInvalidate();
        });
        walkAnimator.setDuration(ANIM_DURATION).start();

    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //可以在这里destory
    }

    private int dp(Context context, int value){
        return DispUtil.dipToPx(context,value);
    }


    private int measureHeight(int defaultHeight, int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        Log.e("YViewHeight", "---speSize = " + specSize + "");

        switch (specMode) {
            case MeasureSpec.AT_MOST:
//                defaultHeight = (int) (-mPaint.ascent() + mPaint.descent()) + getPaddingTop() + getPaddingBottom();
                break;
            case MeasureSpec.EXACTLY:
                defaultHeight = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                defaultHeight = Math.max(defaultHeight, specSize);
                break;
        }
        return defaultHeight;
    }
}
