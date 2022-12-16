package com.gzhealthy.health.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.gzhealthy.health.R;

/**
 * 电池
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class BatteryView extends LinearLayout {
    private int colorNormal = Color.parseColor("#333333");
    private int colorAbnormal = Color.parseColor("#db3f3f");

    private View vBatteryGroup;
    private View vBatterySize;
    private View vBatteryMouth;
    private TextView tvBatterySize;


    public BatteryView(Context context) {
        this(context, null);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_battery_view, this, true);
        vBatteryGroup = view.findViewById(R.id.vBatteryGroup);
        vBatterySize = view.findViewById(R.id.vBatterySize);
        vBatteryMouth = view.findViewById(R.id.vBatteryMouth);
        tvBatterySize = view.findViewById(R.id.tvBatterySize);
    }

    /**
     * 设置电量
     *
     * @param size 电量百分比
     */
    public void setBatterySize(int size) {
        if (size < 0) {
            size = 0;
        }
        if (size > 100) {
            size = 100;
        }
        if (size >= 20) {
            vBatteryGroup.setBackgroundResource(R.drawable.shape_battery_view_normal);
            vBatterySize.setBackgroundColor(colorNormal);
            vBatteryMouth.setBackgroundColor(colorNormal);
            tvBatterySize.setTextColor(colorNormal);
        } else {
            vBatteryGroup.setBackgroundResource(R.drawable.shape_battery_view_abnormal);
            vBatterySize.setBackgroundColor(colorAbnormal);
            vBatteryMouth.setBackgroundColor(colorAbnormal);
            tvBatterySize.setTextColor(colorAbnormal);
        }
        int width = ConvertUtils.dp2px(10 * size / 100);
        if (size > 0 && size < 10) {
            width = 1;
        }
        ViewGroup.LayoutParams params = vBatterySize.getLayoutParams();
        if (params != null && width > 0) {
            vBatterySize.setVisibility(View.VISIBLE);
            params.width = width;
        } else {
            vBatterySize.setVisibility(View.GONE);
        }
        tvBatterySize.setText(size + "%");
    }


}
