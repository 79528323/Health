package com.gzhealthy.health.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.youth.banner.indicator.BaseIndicator;

/**
 * 圆形指示器
 * 如果想要大小一样，可以将选中和默认设置成同样大小
 */
public class LauchIndicator extends BaseIndicator {
    RectF rectF;

    public LauchIndicator(Context context) {
        this(context, null);
    }

    public LauchIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LauchIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        rectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = config.getIndicatorSize();
        if (count <= 1) {
            return;
        }
        //间距*（总数-1）+默认宽度*（总数-1）+选中宽度
        int space = config.getIndicatorSpace() * (count - 1);
        int normal = config.getNormalWidth() * (count - 1);
        setMeasuredDimension(space + normal + config.getSelectedWidth(), config.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int count = config.getIndicatorSize();
        if (count <= 1) {
            return;
        }
        float left = 0;
        for (int i = 0; i < count; i++) {
            mPaint.setColor(config.getCurrentPosition() == i ? Color.parseColor("#00D0B9") : Color.parseColor("#C1EAE9"));
            int indicatorWidth = config.getCurrentPosition() == i ? config.getSelectedWidth() : config.getNormalWidth();
            rectF.set(left, 0, left + indicatorWidth, config.getHeight());
            left += indicatorWidth + config.getIndicatorSpace();
            canvas.drawRoundRect(rectF, config.getRadius(), config.getRadius(), mPaint);
        }
    }

}
