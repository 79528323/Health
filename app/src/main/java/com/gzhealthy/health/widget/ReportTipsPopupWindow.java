package com.gzhealthy.health.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.gzhealthy.health.R;

/**
 * Created by Justin_Liu
 * on 2021/6/23
 */
public class ReportTipsPopupWindow extends PopupWindow {
    ImageView close;

    public ReportTipsPopupWindow(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_report_tips,null);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        setWidth(metrics.widthPixels/2 + (metrics.widthPixels/8));
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        close = view.findViewById(R.id.close);
        close.setOnClickListener(v -> {
            dismiss();
        });
        setContentView(view);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

    }


}
