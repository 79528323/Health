package com.gzhealthy.health.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.OverScroller;

import com.github.mikephil.charting.utils.Utils;
import com.gzhealthy.health.R;
import com.gzhealthy.health.utils.DispUtil;

import androidx.annotation.Nullable;

/**
 * Created by Justin_Liu
 * on 2021/5/31
 * 滑动刻度尺
 */
public class ScrollRulerView extends View {

    private float shortScaleLength = 0;
    private float longScaleLength =0;

    private float ruleWithds;
    /**
     * 刻度简隔距离
     */
    private float scaleOffset = 20;
    private DisplayMetrics metrics;
    /**
     * 尺子
     */
    private Paint rulePaint;

    /**
     * 刻度
     */
    private Paint ruleScalePaint;

    /**
     * 中间标度
     */
    private Paint markerPaint;

    private int minScale = 30;
    private int maxScale = 200;

    /**
     * 跟踪触摸屏事件（flinging事件和其他gestures手势事件）的速率。
     */
    private VelocityTracker mVelocityTracker;
    /**
     * 处理滚动效果的工具类
     */
    private OverScroller overScroller;

    private float startX;


    private int MAX_RANGE ,MIN_RANGE;


    public ScrollRulerView(Context context) {
        super(context);
        init(context);
    }

    public ScrollRulerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public void init(Context context){
        metrics = context.getResources().getDisplayMetrics();
        overScroller = new OverScroller(context);
        shortScaleLength = dp(context,15);
        longScaleLength = shortScaleLength/3 + shortScaleLength;

        rulePaint = new Paint();
        rulePaint.setStrokeWidth(10);
        rulePaint.setColor(context.getColor(R.color.color_d8d8d8));

        ruleScalePaint = new Paint();
        ruleScalePaint.setColor(context.getColor(R.color.global_333333));
        ruleScalePaint.setStrokeWidth(Utils.convertDpToPixel(1f));

        markerPaint = new Paint();
        markerPaint.setColor(context.getColor(R.color.colorPrimary));

//        width = metrics.widthPixels;

        ruleWithds = (maxScale - minScale) * scaleOffset;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int rectLeft = 0 , rectTop = 300,rectRight = (int) ruleWithds,rectBottom = 500;
        canvas.drawRect(new RectF(rectLeft,rectTop,rectRight,rectBottom),rulePaint);

//        int index=0;
//        float rectWihtd = ruleWithds;
//        canvas.save();
//        while (rectWihtd - scaleOffset > 0){
//            Log.e("111","rectWihtd ="+rectWihtd);
//            index++;
////            canvas.save();
//            canvas.translate(scaleOffset,0);
//            canvas.drawLine(rectLeft,rectTop,rectLeft,rectTop + shortScaleLength,ruleScalePaint);
//            if (index % 10 ==0){
////                canvas.save();
////                ruleScalePaint.setStrokeWidth(Utils.convertDpToPixel(3f));
//                canvas.drawLine(rectLeft,rectTop,rectLeft, rectTop + longScaleLength,ruleScalePaint);
////                canvas.restore();
//            }
////            canvas.restore();
//            rectWihtd -= scaleOffset;
//        }
//
//        canvas.restore();

        Point a = new Point((int) (metrics.widthPixels/2 ) - 20 , 300);
        Point b = new Point((int) (metrics.widthPixels/2) + 20, 300);
        Point c = new Point((int) (metrics.widthPixels/2), 300 + 20);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(a.x,a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.close();
        canvas.drawPath(path, markerPaint);

        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.e("111","changed left="+left+" top="+top+" right="+right+"  bottom="+bottom);
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.e("111","onSizeChanged w="+w+" h="+h+" oldw="+oldw+"  oldh="+oldh);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                float distance = event.getX() - startX;
                overScroller.startScroll(
                        overScroller.getFinalX(),
                        overScroller.getFinalY(),
                        (int) -distance,
                        0,
                        0);
                invalidate();
                break;

            case  MotionEvent.ACTION_UP:
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }

                startX = event.getX();
                break;
        }
        return true;
    }

    /**
     * 添加用户的速度跟踪器
     */
    private void addVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }


    public void setRange(int max,int min){
        this.MAX_RANGE = max;
        this.MIN_RANGE = min;
    }

    private int dp(Context context, int value){
        return DispUtil.dipToPx(context,value);
    }
}
