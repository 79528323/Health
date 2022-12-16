package com.gzhealthy.health.tool;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class CacheUtils {
    private static final String TAG = "CacheUtils";

    public static String getApplicationCache(Context context) {
        String cache = "";
        File external = context.getExternalCacheDir();
        if (external != null) {
//            Log.e("tools", "getExternalCacheDir :" + external.getAbsolutePath());
            cache = external.getAbsolutePath();
        } else if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//            Log.e("tools", "Environment :" + Environment.getExternalStorageDirectory().getPath());
            cache = Environment.getExternalStorageDirectory().getPath() + "/com.aiten.niubai";
        } else {
//            Log.e("tools", "getCacheDir :" + context.getCacheDir().getAbsolutePath());
            cache = context.getCacheDir().getAbsolutePath();
        }
        File cacheFile = new File(cache);
        if (!cacheFile.exists())
            cacheFile.mkdir();
//        Log.e("cache", "cache : " + cacheFile.getAbsolutePath());
        return cacheFile.getAbsolutePath();
    }

    private static CacheUtils instance = null;

    public static CacheUtils getInstance() {
        if (instance == null) {
            instance = new CacheUtils();
        }
        return instance;
    }

}
