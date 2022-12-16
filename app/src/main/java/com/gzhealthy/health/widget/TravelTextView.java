package com.gzhealthy.health.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.gzhealthy.health.tool.FontUtils;

public class TravelTextView extends AppCompatTextView {

    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public TravelTextView(Context context) {
        super(context);
//        Typeface customFont = selectTypeface(context, Typeface.NORMAL);
//        setTypeface(customFont);
    }

    public TravelTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context, attrs);
    }

    public TravelTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context, attrs);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {
        int textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);

//        Typeface customFont = selectTypeface(context, textStyle);
//        setTypeface(customFont);
    }

//    private Typeface selectTypeface(Context context, int textStyle) {
//        switch (textStyle) {
//            case Typeface.BOLD: // bold
//                return FontUtils.getTypeface("SourceHanSansCN-Bold.otf", context);
//            case Typeface.NORMAL: // regular
//            default:
//                return FontUtils.getTypeface("SourceHanSansCN-Normal.otf", context);
//        }
//    }
}
