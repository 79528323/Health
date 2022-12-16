package com.gzhealthy.health.tool;

public class DesPswUtil {
    static {
        try {
            System.loadLibrary("YunPay");
        } catch (Throwable ex) {
        }
    }

    public static String getDesStr() {
        return getStringFromNative();
    }

    public static String getDesKeyStr() {
        return getDesPswStringFromNative();
    }

    public static String getKeyStr() {
        return getKeyStringFromNative();
    }

    public static native String getStringFromNative();

    public static native String getKeyStringFromNative();

    public static native String getDesPswStringFromNative();
}
