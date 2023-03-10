package com.gzhealthy.health.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

import com.gzhealthy.health.R;
import com.gzhealthy.health.utils.DispUtil;

import java.math.BigDecimal;


/**
 * 刻度尺
 */
public class HorizontalScaleView extends View {

    private final int SCALE_WIDTH_BIG = 3;//大刻度线宽度
    private final int SCALE_WIDTH_SMALL = 2;//小刻度线宽度
    private final int LINE_WIDTH = 2;//指针线宽度

    private int rectPadding = 40;//圆角矩形间距
    private int rectWidth;//圆角矩形宽
    private int rectHeight;//圆角矩形高

    private int maxScaleLength;//大刻度长度
    private int midScaleLength;//中刻度长度
    private int minScaleLength;//小刻度长度
    private int scaleSpace;//刻度间距
    private int scaleSpaceUnit;//每大格刻度间距
    private int height, width;//view高宽
    private int ruleHeight;//刻度尺高

    private int max;//最大刻度
    private int min;//最小刻度
    private int borderLeft, borderRight;//左右边界值坐标
    private float midX;//当前中心刻度x坐标
    private float originMidX;//初始中心刻度x坐标
    private float minX;//最小刻度x坐标,从最小刻度开始画刻度

    private float lastX;

    private float originValue;//初始刻度对应的值
    private float currentValue;//当前刻度对应的值

    private Paint paint;//画笔

    private Context context;

    private String descri = "体重";//描述
    private String unit = "kg";//刻度单位

    private VelocityTracker velocityTracker;//速度监测
    private float velocity;//当前滑动速度
    private float a = 1000000;//加速度
    private boolean continueScroll;//是否继续滑动

    private boolean isMeasured;

    private static final int ruleScaleColors = Color.parseColor("#CCCCBF");

    private OnValueChangeListener onValueChangeListener;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (null != onValueChangeListener) {
                float v = (float) (Math.round(currentValue * 10)) / 10;//保留一位小数
                onValueChangeListener.onValueChanged(v);
            }
        }
    };

    public HorizontalScaleView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public HorizontalScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public HorizontalScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        //初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);

    }

    //设置刻度范围
    public void setRange(int min, int max) {
        this.min = min;
        this.max = max;
        originValue = (max + min) / 2;
        currentValue = originValue;
    }

    /**
     * 设定当前刻度数值
     * @param currentValue
     */
    public void setCurrentValue(float currentValue) {
        if (currentValue > max || currentValue < min )
            return;

        this.currentValue = currentValue;
        if (originValue > 0){
            float offset = (currentValue - originValue) * scaleSpaceUnit;
            midX -= offset;
            minX -= offset;
            calculateCurrentScale();
            confirmBorder();
            postInvalidate();
        }
    }

    //设置刻度单位
    public void setUnit(String unit) {
        this.unit = unit;
    }

    //设置刻度描述
    public void setDescri(String descri) {
        this.descri = descri;
    }

    //设置value变化监听
    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        this.onValueChangeListener = onValueChangeListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isMeasured) {
            width = getMeasuredWidth();
            height = getMeasuredHeight();
            ruleHeight = height / 2;
            maxScaleLength = height / 10;
            midScaleLength = height / 12;
            minScaleLength = maxScaleLength / 2;
            scaleSpace = height / 100 > 4 ? height / 100 : 4;
            scaleSpaceUnit = scaleSpace + SCALE_WIDTH_BIG + SCALE_WIDTH_SMALL * 9;
            rectWidth = scaleSpaceUnit;
            rectHeight = scaleSpaceUnit / 2;

            borderLeft = width / 2 - ((min + max) / 2 - min) * scaleSpaceUnit;
            borderRight = width / 2 + ((min + max) / 2 - min) * scaleSpaceUnit;
            midX = (borderLeft + borderRight) / 2;
            originMidX = midX;
            minX = borderLeft;
            isMeasured = true;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画刻度线
        for (int i = min; i <= max; i++) {
            //画刻度数字
            Rect rect = new Rect();
            String str = String.valueOf(i);
            paint.setColor(context.getColor(R.color.global_666666));
            paint.setTextSize(DispUtil.sp2px(context,16));
            paint.getTextBounds(str, 0, str.length(), rect);
            int w = rect.width();
            int h = rect.height();

            int maxScaleLgh = 0;//刻度数字增加长度
            if (i % 10 ==0){
                maxScaleLgh = maxScaleLength;
                canvas.drawText(str,
                        minX + (i - min) * scaleSpaceUnit - w / 2 - SCALE_WIDTH_BIG / 2,
                        ruleHeight + maxScaleLength + h + minScaleLength / 2 + maxScaleLgh + (maxScaleLgh/2),
                        paint);
            }


            //画大刻度线
            paint.setStrokeWidth(SCALE_WIDTH_SMALL);
            paint.setColor(ruleScaleColors);
//            canvas.drawLine(minX + (i - min) * scaleSpaceUnit, ruleHeight - maxScaleLength, minX + (i - min) * scaleSpaceUnit, ruleHeight, paint);

            canvas.drawLine(minX + (i - min) * scaleSpaceUnit,
                    ruleHeight,
                    minX + (i - min) * scaleSpaceUnit,
                    ruleHeight + maxScaleLength+ h + maxScaleLgh/2,
                    paint);

            if (i == max) {
                continue;//最后一条不画中小刻度线
            }
            //画中刻度线
//            paint.setStrokeWidth(SCALE_WIDTH_SMALL);
//            canvas.drawLine(minX + (i - min) * scaleSpaceUnit + scaleSpaceUnit / 2, ruleHeight, minX + (i - min) * scaleSpaceUnit + scaleSpaceUnit / 2, ruleHeight - midScaleLength, paint);
            //画小刻度线
//            paint.setStrokeWidth(SCALE_WIDTH_SMALL);
//            for (int j = 1; j < 10; j++) {
////                if (j == 5) {
////                    continue;
////                }
//                canvas.drawLine(minX + (i - min) * scaleSpaceUnit + (SCALE_WIDTH_SMALL + scaleSpace) * j, ruleHeight, minX + (i - min) * scaleSpaceUnit + (SCALE_WIDTH_SMALL + scaleSpace) * j, ruleHeight - minScaleLength, paint);
//            }

        }

        //画刻尺横线
        paint.setStrokeWidth(LINE_WIDTH);
        paint.setColor(ruleScaleColors);
        canvas.drawLine(minX + SCALE_WIDTH_BIG / 2,
                ruleHeight + LINE_WIDTH / 2,
                minX + (max - min) * scaleSpaceUnit - SCALE_WIDTH_BIG / 2,
                ruleHeight + LINE_WIDTH / 2,
                paint);
        //画指针线
//        paint.setColor(getResources().getColor(R.color.colorPrimaryDark));
//        canvas.drawLine(width / 2, 0, width / 2, ruleHeight, paint);
        //画圆角矩形
//        paint.setStyle(Paint.Style.FILL);
//        RectF r = new RectF();
//        r.left = width / 2 - rectWidth / 2;
//        r.top = ruleHeight + rectPadding;
//        r.right = width / 2 + rectWidth / 2;
//        r.bottom = ruleHeight + rectPadding + rectHeight;
//        canvas.drawRoundRect(r, 10, 10, paint);



        //画小三角形指针
//        Path path = new Path();
//        path.moveTo(width / 2 - scaleSpace * 2, ruleHeight + rectPadding);
//        path.lineTo(width / 2, ruleHeight + rectPadding - 10);
//        path.lineTo(width / 2 + scaleSpace * 2, ruleHeight + rectPadding);
//        path.close();
//        canvas.drawPath(path, paint);
        paint.setTextSize(DispUtil.sp2px(context,24));
        paint.setColor(Color.parseColor("#00C9B8"));
        Point a = new Point(width / 2 - scaleSpace * 4 , ruleHeight + LINE_WIDTH / 2);
        Point b = new Point(width / 2 + scaleSpace * 4, ruleHeight + LINE_WIDTH / 2);
        Point c = new Point(width / 2, ruleHeight + rectPadding/2);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.close();
        canvas.drawPath(path, paint);


        //绘制文字
//        paint.setColor(Color.parseColor("#666666"));
//        Rect rect1 = new Rect();
//        paint.getTextBounds(descri, 0, descri.length(), rect1);
//        int w1 = rect1.width();
//        int h1 = rect1.height();
//        canvas.drawText(descri, width / 2 - w1 / 2, ruleHeight + rectPadding + rectHeight + h1 + 10, paint);
        //绘制当前刻度值数字
        paint.setColor(Color.parseColor("#00C9B8"));
//        float v = (float) (Math.round(currentValue * 10)) / 10;//保留一位小数
        int v = Math.round(currentValue);
        String value = v + unit;
        Rect rect2 = new Rect();
        paint.getTextBounds(value, 0, value.length(), rect2);
        int w2 = rect2.width();
        int h2 = rect2.height();
        canvas.drawText(value, width / 2 - w2 / 2, ruleHeight  - h2 / 2, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                continueScroll = false;
                //初始化速度追踪
                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain();
                } else {
                    velocityTracker.clear();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(event);
                int offsetX = (int) (lastX - x);
                minX -= offsetX;
                midX -= offsetX;
                calculateCurrentScale();
                invalidate();
                lastX = x;
                break;
            case MotionEvent.ACTION_UP:
                confirmBorder();
                //当前滑动速度
                velocityTracker.computeCurrentVelocity(1000);
                velocity = velocityTracker.getXVelocity();
                float minVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
                if (Math.abs(velocity) > minVelocity) {
                    continueScroll = true;
                    continueScroll();
                } else {
                    computeMedianScaleScrollOff();
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                velocityTracker.recycle();
                velocityTracker = null;
                break;
        }
        return true;
    }

    //计算当前刻度
    private void calculateCurrentScale() {
        float offsetTotal = midX - originMidX;
//        Log.e("111","offsetTotal ="+offsetTotal);
        int offsetBig = (int) (offsetTotal / scaleSpaceUnit);//移动的大刻度数
//        Log.e("111","offsetBig ="+offsetBig);
        float offsetS = offsetTotal % scaleSpaceUnit;
        int offsetSmall = (new BigDecimal(offsetS / (scaleSpace + SCALE_WIDTH_SMALL)).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue();//移动的小刻度数 四舍五入取整
//        Log.e("111","offsetSmall ="+offsetSmall);
        float offset = offsetBig + offsetSmall * 0.1f;
//        Log.e("111","offset ="+offset);
        if (originValue - offset > max) {
            currentValue = max;
        } else if (originValue - offset < min) {
            currentValue = min;
        } else {
            currentValue = originValue - offset;
        }
        mHandler.sendEmptyMessage(0);
    }

    //指针线超出范围时 重置回边界处
    private void confirmBorder() {
        if (midX < borderLeft) {
            midX = borderLeft;
            minX = borderLeft - (borderRight - borderLeft) / 2;
            postInvalidate();
        } else if (midX > borderRight) {
            midX = borderRight;
            minX = borderLeft + (borderRight - borderLeft) / 2;
            postInvalidate();
        }
    }

    //手指抬起后继续惯性滑动
    private void continueScroll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                float velocityAbs = 0;//速度绝对值
                if (velocity > 0 && continueScroll) {
                    velocity -= 50;
                    minX += velocity * velocity / a;
                    midX += velocity * velocity / a;
                    velocityAbs = velocity;
                } else if (velocity < 0 && continueScroll) {
                    velocity += 50;
                    minX -= velocity * velocity / a;
                    midX -= velocity * velocity / a;
                    velocityAbs = -velocity;
                }
                calculateCurrentScale();
                confirmBorder();
                postInvalidate();
                if (continueScroll && velocityAbs > 0) {
                    post(this);
                } else {
                    computeMedianScaleScrollOff();
                    continueScroll = false;
                }
            }
        }).start();
    }

    /**
     *  滑动后将中间指针自动回正到刻度值上
     */
    private void computeMedianScaleScrollOff(){
        float abs = midX - originMidX;
        if (Math.asin(abs)%scaleSpaceUnit !=0){
            float cos =  abs % scaleSpaceUnit;
            Log.e("111","cos =="+cos);
//            if (scaleSpaceUnit/2 < Math.abs(cos)){
//                minX += cos;
//                midX += cos;
//            }else {
//                minX -= cos;
//                midX -= cos;
//            }

            minX -= cos;
            midX -= cos;

            calculateCurrentScale();
            confirmBorder();
            postInvalidate();
        }
    }


    public interface OnValueChangeListener {
        void onValueChanged(float value);
    }
}
