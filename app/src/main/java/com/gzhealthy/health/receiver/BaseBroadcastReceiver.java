package com.gzhealthy.health.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 广播封装
 */
public abstract class BaseBroadcastReceiver extends BroadcastReceiver {

    public static void registerReceiver(Context context, String action, BaseBroadcastReceiver baseBroadcastReceiver) {
        try {
            IntentFilter myIntentFilter = new IntentFilter();
            myIntentFilter.addAction(action);
            context.getApplicationContext().registerReceiver(baseBroadcastReceiver, myIntentFilter);
        } catch (Exception var3) {

        }
    }

    public static void unregisterReceiver(Context context, BaseBroadcastReceiver baseBroadcastReceiver) {
        try {
            context.getApplicationContext().unregisterReceiver(baseBroadcastReceiver);
        } catch (Exception var3) {

        }
    }

    @Override
    public abstract void onReceive(Context context, Intent intent);
}
