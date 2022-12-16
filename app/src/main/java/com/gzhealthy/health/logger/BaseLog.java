package com.gzhealthy.health.logger;

import android.text.TextUtils;

import com.gzhealthy.health.BuildConfig;


public class BaseLog implements LogInterface {

    protected static void printLine(String tag, boolean isTop) {
        if (isTop) {
            android.util.Log.d(tag, "═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            android.util.Log.d(tag, "═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }

    protected static boolean isEmpty(String line) {
        return TextUtils.isEmpty(line) || line.equals("\n") || line.equals("\t") || TextUtils.isEmpty(line.trim());
    }

    public static void printDefault(int type, String tag, String msg) {
        if (BuildConfig.DEBUG) {
            switch (type) {
                case V:
                    android.util.Log.v(tag, msg);
                    break;
                case D:
                    android.util.Log.d(tag, msg);
                    break;
                case I:
                    android.util.Log.i(tag, msg);
                    break;
                case W:
                    android.util.Log.w(tag, msg);
                    break;
                case E:
                    android.util.Log.e(tag, msg);
                    break;
                case A:
                    android.util.Log.wtf(tag, msg);
                    break;
            }
        }
    }

}
