package com.gzhealthy.health.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gzhealthy.health.R;
import com.gzhealthy.health.logger.Logger;
import com.gzhealthy.health.tool.NetworkUtils;
import com.gzhealthy.health.tool.ToastUtils;


public class NetWorkChangeReceiver extends BroadcastReceiver {

    private final String TAG = NetWorkChangeReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!NetworkUtils.isConnected(context)) { // connected to the internet
            Logger.e(TAG, "activeNetwork  = " + NetworkUtils.getNetWorkTypeName(context));
        } else {
            // 无网络连接
            ToastUtils.showShort(R.string.net_error_check);
        }
    }
}