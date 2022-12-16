package com.gzhealthy.health.widget.decoration;/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.gzhealthy.health.adapter.DragOtherAdapter;
import com.gzhealthy.health.adapter.DragSortAdapter;
import com.gzhealthy.health.adapter.HealthCardAdapter;
import com.gzhealthy.health.utils.DispUtil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * This class is from the v7 samples of the Android SDK. It's not by me!
 * <p/>
 * See the license above for details.
 */
public class HealthCardGridItemDecoration extends RecyclerView.ItemDecoration {
    private Context context;

    public HealthCardGridItemDecoration(Context context) {
        this.context = context;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        int left =0 ,right=0;

        if (itemPosition % 2 > 0){
            left = DispUtil.dp2px(context,7);
        }else {
            right = DispUtil.dp2px(context,7);
        }

        outRect.set(left,DispUtil.dp2px(context,14),right, 0);
    }
}