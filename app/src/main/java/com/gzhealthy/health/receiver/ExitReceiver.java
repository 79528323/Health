package com.gzhealthy.health.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gzhealthy.health.logger.Logger;


public class ExitReceiver extends BroadcastReceiver {
    private static final String TAG = ExitReceiver.class.getSimpleName();
    Activity activity = null;

    public ExitReceiver(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.i(TAG, "程序退出");
        if (null != activity) {
            activity.finish();
        }
    }
}