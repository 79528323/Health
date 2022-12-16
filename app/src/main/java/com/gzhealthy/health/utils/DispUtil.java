package com.gzhealthy.health.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;

import java.lang.reflect.Method;

/**
 * DESCRIBE ：禁用字体大小和显示大小随系统变化
 */
public final class DispUtil {

    /**
     * 禁用显示大小改变和文字大小
     */
    public static Resources disabledDisplayDpiChange(Resources res) {
        Configuration newConfig = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //字体非默认值
            if (res.getConfiguration().fontScale != 1) {
                newConfig.fontScale = 1;
            }
            newConfig.densityDpi = getDefaultDisplayDensity();
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        } else {
            //字体非默认值
            if (res.getConfiguration().fontScale != 1) {
                newConfig.fontScale = 1;//设置默认
                res.updateConfiguration(newConfig, res.getDisplayMetrics());
            }
        }
        return res;
    }

    /**
     * 获取手机出厂时默认的densityDpi
     */
    public static int getDefaultDisplayDensity() {
        try {
            Class aClass = Class.forName("android.view.WindowManagerGlobal");
            Method method = aClass.getMethod("getWindowManagerService");
            method.setAccessible(true);
            Object iwm = method.invoke(aClass);
            Method getInitialDisplayDensity = iwm.getClass().getMethod("getInitialDisplayDensity", int.class);
            getInitialDisplayDensity.setAccessible(true);
            Object densityDpi = getInitialDisplayDensity.invoke(iwm, Display.DEFAULT_DISPLAY);
            return (int) densityDpi;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    public static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * dp转px
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }
}
