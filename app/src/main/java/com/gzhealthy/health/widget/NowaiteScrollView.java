package com.gzhealthy.health.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

import com.gzhealthy.health.logger.Logger;

/**
 * 重写ScrollView解决滑动里面的RecycleView卡顿问题
 */

public class NowaiteScrollView extends ScrollView {
    private int downX;
    private int downY;
    private int mTouchSlop;
    ScrollButtom scrollButtom;

    public void setScrollButtom(ScrollButtom scrollButtom) {
        this.scrollButtom = scrollButtom;
    }

    public NowaiteScrollView(Context context) {
        this(context, null);
    }

    public NowaiteScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NowaiteScrollView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    }

    //
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        View view = (View) getChildAt(getChildCount() - 1);
        int d = view.getBottom();
        d -= (getHeight() + getScrollY());
        if (d == 0) {
            Logger.e("11", "滑到地步了");
            if (null != scrollButtom)
                scrollButtom.scrollbutom();
        } else {
            super.onScrollChanged(l, t, oldl, oldt);
        }
    }

    @Override

    public boolean onInterceptTouchEvent(MotionEvent e) {

        int action = e.getAction();

        switch (action) {

            case MotionEvent.ACTION_DOWN:

                downX = (int) e.getRawX();

                downY = (int) e.getRawY();

                break;

            case MotionEvent.ACTION_MOVE:

                int moveY = (int) e.getRawY();

                if (Math.abs(moveY - downY) > mTouchSlop) {

                    return true;

                }

        }

        return super.onInterceptTouchEvent(e);

    }

    public interface ScrollButtom {
        public void scrollbutom();
    }
}
