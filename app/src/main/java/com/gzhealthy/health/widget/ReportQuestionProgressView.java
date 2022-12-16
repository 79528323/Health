package com.gzhealthy.health.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gzhealthy.health.R;

/**
 * 中医体质问卷进度条
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class ReportQuestionProgressView extends LinearLayout {
    private View vMax, vCurrent;
    private TextView tvMax, tvCurrent;
    private int mMax = 0;
    private int mCurrent = 0;

    public ReportQuestionProgressView(Context context) {
        this(context, null);
    }

    public ReportQuestionProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReportQuestionProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_report_question_progress_view, this, true);
        vMax = view.findViewById(R.id.vMax);
        vCurrent = view.findViewById(R.id.vCurrent);
        tvMax = view.findViewById(R.id.tvMax);
        tvCurrent = view.findViewById(R.id.tvCurrent);
    }

    /**
     * 设置最大值
     *
     * @param max
     */
    public void setvMax(int max) {
        mMax = max;
        tvMax.setText("/" + max);
    }

    /**
     * 设置当前值
     *
     * @param current
     */
    public void setCurrent(int current) {
        mCurrent = current;
        tvCurrent.setText(current + "");
        if (mMax > 0 && mCurrent > 0) {
            vCurrent.getLayoutParams().width = vMax.getWidth() * mCurrent / mMax;
        }
    }

}
