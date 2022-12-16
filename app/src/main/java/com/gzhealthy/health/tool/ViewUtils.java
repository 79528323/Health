package com.gzhealthy.health.tool;

import android.view.View;

public class ViewUtils {

    public static <V extends View> V $(View rootView, int mResId) {
        V view = (V) rootView.findViewById(mResId);
        return view;
    }
}
