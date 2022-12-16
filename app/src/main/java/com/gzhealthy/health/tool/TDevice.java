package com.gzhealthy.health.tool;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.logger.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

public class TDevice {

    /**
     * @param mActvity 保存屏幕宽高
     */
    public static void saveDisplaySize(Activity mActvity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActvity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        SPCache.putInt(Constants.SharePreference.DEVICE_WIDTH, displayMetrics.widthPixels);
        SPCache.putInt(Constants.SharePreference.DEVICE_HEIGHT, displayMetrics.heightPixels);
    }

    /**
     * 将px类型的尺寸转换成dp类型的尺寸
     *
     * @param mPx
     * @param mContext
     * @return
     */
    public static int px2dip(Context mContext, float mPx) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (mPx / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context mContext, float mDp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (mDp * scale + 0.5f);
    }

    /**
     * @方法名称: sp2px
     * @描述: 将sp值转换为px值
     */
    public static int sp2px(Context mContext, float mSp) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (mSp * fontScale + 0.5f);
    }

    public static int px2sp(Context mContext, float mPx) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (mPx / fontScale + 0.5f);
    }


    /**
     * 获取屏幕宽度(像素)
     *
     * @param mContext
     * @return
     */
    public static int getScreenWidth(Context mContext) {
        WindowManager mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        return mWindowManager.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕高度(像素)
     *
     * @param mContext
     * @return
     */
    public static int getScreenHeight(Context mContext) {
        WindowManager mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        return mWindowManager.getDefaultDisplay().getHeight();
    }

    /**
     * 2  * 获取版本号
     * 3  * @return 当前应用的版本号
     * 4
     */
    public static String getVersion(Context mContext) {
        try {
            PackageManager mManager = mContext.getPackageManager();
            PackageInfo mInfo = mManager.getPackageInfo(mContext.getPackageName(), 0);
            String mVersion = mInfo.versionName;
            return mVersion;
        } catch (Exception e) {
            e.printStackTrace();
            return "获取版本号失败";
        }
    }

    public static int getVersionCode(Context mContext) {
        try {
            PackageManager mManager = mContext.getPackageManager();
            PackageInfo mInfo = mManager.getPackageInfo(mContext.getPackageName(), 0);
            return mInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取设备的唯一标识码
     *
     * @param mContext
     * @return
     */
    public static String getDeviceId(Context mContext) {
        try {
            TelephonyManager mTm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

            String mDeviceId = null;
            if (checkPermission(mContext, Manifest.permission.READ_PHONE_STATE)) {
                mDeviceId = mTm.getDeviceId();
            }

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();

            if (TextUtils.isEmpty(mDeviceId)) {
                mDeviceId = mac;
            }

            if (TextUtils.isEmpty(mDeviceId)) {
                mDeviceId = android.provider.Settings.Secure.getString(mContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            }

            return mDeviceId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return UUID.randomUUID().toString();
    }


    /**
     * 获取移动终端类型
     *
     * @param context 上下文
     * @return 手机制式
     * <ul>
     * <li>PHONE_TYPE_NONE  : 0 手机制式未知</li>
     * <li>PHONE_TYPE_GSM   : 1 手机制式为GSM，移动和联通</li>
     * <li>PHONE_TYPE_CDMA  : 2 手机制式为CDMA，电信</li>
     * <li>PHONE_TYPE_SIP   : 3</li>
     * </ul>
     */
    public static int getPhoneType(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getPhoneType() : -1;
    }


    /**
     * 获取设备信息
     *
     * @param mContext
     * @return
     */
    public static String getDeviceInfo(Context mContext) {
        try {
            org.json.JSONObject mJson = new org.json.JSONObject();
            TelephonyManager tm = (TelephonyManager) mContext
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String mDeviceId = null;
            if (checkPermission(mContext, Manifest.permission.READ_PHONE_STATE)) {
                mDeviceId = tm.getDeviceId();
            }
            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) mContext
                    .getSystemService(Context.WIFI_SERVICE);
            String mac = wifi.getConnectionInfo().getMacAddress();
            mJson.put("mac", mac);
            if (TextUtils.isEmpty(mDeviceId)) {
                mDeviceId = mac;
            }
            if (TextUtils.isEmpty(mDeviceId)) {
                mDeviceId = android.provider.Settings.Secure.getString(mContext.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            mJson.put("device_id", mDeviceId);
            return mJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("NewApi")
    public static boolean checkPermission(Context mContext, String mPermission) {
        boolean mResult = false;
        if (Build.VERSION.SDK_INT >= 23) {
            if (mContext.checkSelfPermission(mPermission) == PackageManager.PERMISSION_GRANTED) {
                mResult = true;
            }
        } else {
            PackageManager pm = mContext.getPackageManager();
            if (pm.checkPermission(mPermission, mContext.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                mResult = true;
            }
        }
        return mResult;
    }

    private static boolean checkDeviceHasNavigationBar(Context mContext) {
        boolean mHasNavigationBar = false;
        Resources mResource = mContext.getResources();
        int mId = mResource.getIdentifier("config_showNavigationBar", "bool", "android");
        if (mId > 0) {
            mHasNavigationBar = mResource.getBoolean(mId);
        }
        try {
            Class mSystemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = mSystemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(mSystemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                mHasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                mHasNavigationBar = true;
            }
        } catch (Exception e) {
            Logger.e("tools", e);
        }
        return mHasNavigationBar;

    }

    /**
     * get navigationbar's height
     *
     * @param mContext
     * @return
     */
    public static int getNavigationBarHeight(Context mContext) {
        int mNavigationBarHeight = 0;
        Resources mResouce = mContext.getResources();
        int mId = mResouce.getIdentifier("navigation_bar_height", "dimen", "android");
        if (mId > 0 && checkDeviceHasNavigationBar(mContext)) {
            mNavigationBarHeight = mResouce.getDimensionPixelSize(mId);
            mNavigationBarHeight = px2dip(mContext, mNavigationBarHeight);
        }
        return mNavigationBarHeight;
    }

    /**
     * get statusbar's height
     *
     * @param mContext
     * @return
     */
    public static int getStatusBarHeight(Context mContext) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return mContext.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
