package com.gzhealthy.health.service;

import com.gzhealthy.health.logger.Logger;

import cn.jpush.android.service.JCommonService;

/**
 * Created by Justin_Liu
 * on 2021/4/17
 */
public class MyJPushService extends JCommonService {
    private static final String TAG = "MyJPushService";
    public MyJPushService() {
        super();
        Logger.e(TAG,"APP被拉起");
    }


}
