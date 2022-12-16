package com.gzhealthy.health.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.HealthClockAdapter;
import com.gzhealthy.health.model.HealthClockInfo;
import com.gzhealthy.health.model.SleepInfo;
import com.gzhealthy.health.utils.DispUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Justin_Liu
 * on 2021/6/23
 */
public class HealthClockPopupWindow extends PopupWindow {
    String[] content = {
            "子时 23:00-01:00",
            "丑时 01:00-03:00",
            "寅时 03:00-05:00",
            "卯时 05:00-07:00",
            "辰时 07:00-09:00",
            "巳时 09:00-11:00",
            "午时 11:00-13:00",
            "未时 13:00-15:00",
            "申时 15:00-17:00",
            "酉时 17:00-19:00",
            "戌时 19:00-21:00",
            "亥时 21:00-23:00"};
    OnSelectClockListener onSelectClockListener;
    RecyclerView recyclerView;
    HealthClockAdapter adapter;

    public HealthClockPopupWindow(Context context, int type) {
        super(context);
        init(context,type);
    }

    private void init(Context context, int type){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_health_clock,null);
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new HealthClockAdapter(context,type,v -> {
            this.onSelectClockListener.onSelect((Integer) v.getTag());
        });
        recyclerView.setAdapter(adapter);
        List<HealthClockInfo> list = new ArrayList<>();
        for (int index=0; index < content.length; index++){
            list.add(new HealthClockInfo(content[index],index+1));
        }

        adapter.refreshData(list);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        setWidth(DispUtil.dp2px(context,179));
        setHeight(metrics.widthPixels/2);
        setContentView(view);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

    }


    public void setOnSelectClockListener(OnSelectClockListener onSelectClockListener) {
        this.onSelectClockListener = onSelectClockListener;
    }

    public interface OnSelectClockListener{
        void onSelect(int type);
    }
}
