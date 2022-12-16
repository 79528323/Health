package com.gzhealthy.health.widget.decoration;/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.gzhealthy.health.adapter.BackgroundMessageAdapter;
import com.gzhealthy.health.adapter.DragOtherAdapter;
import com.gzhealthy.health.adapter.DragSortAdapter;
import com.gzhealthy.health.utils.DispUtil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * This class is from the v7 samples of the Android SDK. It's not by me!
 * <p/>
 * See the license above for details.
 */
public class HealthCardItemDecoration extends RecyclerView.ItemDecoration {
    private Context context;

    public HealthCardItemDecoration(Context context) {
        this.context = context;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        int size = 0;
        if (parent.getAdapter() instanceof DragSortAdapter){
            size = ((DragSortAdapter)parent.getAdapter()).getItemCount() ;
        }else if (parent.getAdapter() instanceof DragOtherAdapter){
            size = ((DragOtherAdapter)parent.getAdapter()).getItemCount() ;
        }

        if (itemPosition < size){
            outRect.set(DispUtil.dp2px(context,20), 0, DispUtil.dp2px(context,20), DispUtil.dp2px(context,2));
        }
    }
}