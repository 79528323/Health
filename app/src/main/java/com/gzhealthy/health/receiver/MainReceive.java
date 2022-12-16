package com.gzhealthy.health.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.gzhealthy.health.base.Constants;

public abstract class MainReceive extends BroadcastReceiver {
    private static final String ACTION;

    public static void registerReceiver(Context context, MainReceive carModelReceive) {
        try {
            IntentFilter myIntentFilter = new IntentFilter();
            myIntentFilter.addAction(ACTION);
            context.getApplicationContext().registerReceiver(carModelReceive, myIntentFilter);
        } catch (Exception var3) {
        }
    }

    public static void unregisterReceiver(Context context, MainReceive carModelReceive) {
        try {
            context.getApplicationContext().unregisterReceiver(carModelReceive);
        } catch (Exception var3) {
        }
    }

    static {
        ACTION = Constants.Receiver.INTENT_ACTION_MIAN;
    }

    @Override
    public abstract void onReceive(Context context, Intent intent);

    public static void sendBroadcast(Context context, String areaName) {
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.putExtra("areaName", areaName);
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(Context context) {
        Intent intent = new Intent();
        intent.setAction(ACTION);
        context.sendBroadcast(intent);
    }
}
