package com.gzhealthy.health.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by Justin_Liu
 * on 2021/9/15
 */
public class CustomTypefaceTextView extends AppCompatTextView {



    public CustomTypefaceTextView(@NonNull Context context) {
        super(context);
    }

    public CustomTypefaceTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
