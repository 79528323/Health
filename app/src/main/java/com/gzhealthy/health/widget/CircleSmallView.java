package com.gzhealthy.health.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
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
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.gzhealthy.health.R;
import com.gzhealthy.health.utils.DispUtil;

import androidx.annotation.Nullable;

/**
 * Created by Justin_Liu
 * on 2021/4/26
 */
public class CircleSmallView extends View {
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
     * 图标画笔
     */
    private Paint iconPaint;


    private float lineWidth = 1;
    private float circleCenter = 0;
    private float centerX = 0,centerY = 0;
    private RectF rectF;

    private float value = 0;
    private int totalValue = 0;//目标步数
    private float targetAngel = 0;
    private float START_ANGEL = 90;
    private float icon_run_w = 0;
    private float icon_run_h = 0;

    private Bitmap icon_run = null;

    private int background_color;
    private int foreground_color;
    private int insideIconResId;

    private static final int DEFAULT_BACKGROUND_COLOR = Color.TRANSPARENT;

    public CircleSmallView(Context context) {
        super(context);
        init(context);
    }

    public CircleSmallView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        rectF = new RectF();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleSmallView);
        background_color = a.getColor(R.styleable.CircleSmallView_circle_bg,DEFAULT_BACKGROUND_COLOR);
        foreground_color = a.getColor(R.styleable.CircleSmallView_circle_fore_bg,DEFAULT_BACKGROUND_COLOR);
        insideIconResId = a.getResourceId(R.styleable.CircleSmallView_circle_inside_img,-1);
        totalValue = a.getInt(R.styleable.CircleSmallView_circle_total_value,totalValue);
        lineWidth = a.getDimension(R.styleable.CircleSmallView_circle_line_width,lineWidth);

        iconPaint = new Paint();
        icon_run = getIconBmp(context,insideIconResId);
        icon_run_w = icon_run.getWidth();
        icon_run_h = icon_run.getHeight();
        init(context);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //圆环背景
        canvas.drawArc(rectF,0,360,false,circleBackgroundPaint);

        //圆环前景
        if (this.value > 0){
            canvas.drawArc(rectF,START_ANGEL,targetAngel,false,circlePaint);
        }

        //中心icon
        canvas.drawBitmap(icon_run,centerX - (icon_run_w / 2),centerY - (icon_run_h /2),iconPaint);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int minimumWidth = getSuggestedMinimumWidth();
        final int minimumHeight = getSuggestedMinimumHeight();

        int height = measureHeight(minimumHeight,heightMeasureSpec);
        circleCenter = height/2;
        centerX = centerY = circleCenter + (lineWidth/2);
        circleCenter -= lineWidth;

        rectF.left = centerX - circleCenter;
        rectF.right = centerX + circleCenter;
        rectF.top = centerX - circleCenter;
        rectF.bottom = centerY + circleCenter;

        setMeasuredDimension(height, height);
    }


    private int measureHeight(int defaultHeight, int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
//        Log.e("YViewHeight", "---speSize = " + specSize + "");

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

    private void init(Context context){
        this.context = context;

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(foreground_color);
        circlePaint.setStrokeWidth(lineWidth);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);

        circleBackgroundPaint = new Paint();
        circleBackgroundPaint.setAntiAlias(true);
        circleBackgroundPaint.setStyle(Paint.Style.STROKE);
        circleBackgroundPaint.setColor(background_color);
        circleBackgroundPaint.setStrokeWidth(lineWidth);

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


    public void setValue(float value) {
        this.value = value;
        if (this.value <= 0){
            //没有数据时，圆点位置复位
            targetAngel = 0;
            postInvalidate();
            return;
        }
        float Angel = (this.value / totalValue) * 360;
        startAnim(Angel);
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
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //可以在这里destory
    }

    private int dp(Context context, int value){
        return DispUtil.dipToPx(context,value);
    }
}
