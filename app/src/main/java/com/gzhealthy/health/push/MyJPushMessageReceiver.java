package com.gzhealthy.health.push;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.HomeActivity;
import com.gzhealthy.health.activity.SosMessageActivity;
import com.gzhealthy.health.activity.WelcomeActivity;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.manager.ActivityManager;
import com.gzhealthy.health.model.NotificationExtra;
import com.gzhealthy.health.tool.SPCache;

import java.util.List;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * Created by Justin_Liu
 * on 2021/4/17
 */
public class MyJPushMessageReceiver extends JPushMessageReceiver {
    public final static String ACTION_WATCH_BIND_MESSAGE = "watch_bind_transparent_transmission";

    public static final String CONTENT_TYPE_WATCHBIND = "watchBind";
    public static final String CONTENT_TYPE_UNTIE_BIND_CONDITION = "watchUntieBindCondition";
    public static final String CONTENT_TYPE_REFUSE_BIND = "refuseBind";
    /**
     * 心率异常弹窗
     */
    public static final String CONTENT_TYPE_RATE_WARN = "rateWarn";
    /**
     * 心率异常弹窗关闭
     */
    public static final String CONTENT_TYPE_CANCEL = "cancel";
    /**
     * 提示信息弹窗
     */
    public static final String CONTENT_TYPE_TIP = "tip";

    private static final String TAG = "PushMessageReceiver";
    private NotificationManager manager;

    enum NotificationType {
        SOS
    }

    /**
     * 收到自定义消息回调
     *
     * @param context
     * @param customMessage
     */
    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        Logger.e(TAG, "自定义消息回调 ==" + customMessage.toString());
        //在登录状态下才弹窗
         boolean isLogin = TextUtils.isEmpty(SPCache.getString(SPCache.KEY_TOKEN,"")) ?false:true;
        String type = customMessage.contentType;
        if (CONTENT_TYPE_RATE_WARN.equals(type)) {
            //心率异常，无论在哪个界面都需要弹窗提示
            if (isLogin)
                RxBus.getInstance().post(CONTENT_TYPE_RATE_WARN, customMessage.message);
        } else if (CONTENT_TYPE_CANCEL.equals(type)) {
            //关闭心率异常弹窗
            RxBus.getInstance().post(CONTENT_TYPE_CANCEL, customMessage.message);
        } else if (CONTENT_TYPE_TIP.equals(type)) {
            //提示信息提示信息
            if (isLogin)
                RxBus.getInstance().post(CONTENT_TYPE_TIP, customMessage.message);
        }
        //TODO 07-26 因为极光推送延迟，由透传通知绑定更新操作暂时弃用，改为轮询接口//
//        if (type.equals(CONTENT_TYPE_WATCHBIND)){
//            //手表绑定
//            RxBus.getInstance().postEmpty(RxEvent.WATCH_BIND_AND_BIND_PERSON_INFO);
//        }else if (type.equals(CONTENT_TYPE_REFUSE_BIND)){
//            //手表端拒绝绑定
//            RxBus.getInstance().postEmpty(RxEvent.WATCH_BIND_REFUSE);
//        }else if (type.equals(CONTENT_TYPE_UNTIE_BIND_CONDITION)){
//            //绑定手表而无须再进入个人信息绑定页面
//            RxBus.getInstance().postEmpty(RxEvent.WATCH_BIND_UNTIE_BIND_CONDITION);
//        }

//        RxBus.getInstance().postEmpty(RxEvent.WATCH_BIND_GET_WATCH);
    }

    /**
     * 点击通知回调
     *
     * @param context
     * @param notificationMessage
     */
    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {

        Logger.e(TAG, "onNotifyMessageOpened");
//        Intent intent = new Intent(context, WelcomeActivity.class);
////            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//
//        ActivityManager.doStartApplicationWithPackageNameMy(context);

        //进程不存在时启动应用
//            SPCache.putBoolean(SPCache.KEY_JUMP_SOS,true);
//            Intent intent = new Intent(context, WelcomeActivity.class);
////            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//            Log.e("111","Opened");
//            Activity activity = BaseApplication.activityStackManager.getActivity(HomeActivity.class);
//            if (activity==null){//已经退出
//                Intent intent = new Intent(context, LaucherActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }else {
//                Toast.makeText(context,"打开通知：\n"+notificationMessage.notificationContent,Toast.LENGTH_LONG).show();
//            }
        Intent i = null;
        NotificationExtra extra = JSON.parseObject(notificationMessage.notificationExtras, NotificationExtra.class);
        //TODO 点击通知时，判断APP是否存活,如若存活先不做其它动作跳转，包括当前应用是否在前后台时
        if (ActivityManager.hasActivity(HomeActivity.class)) {
            switch (extra.messageType) {
                case 0:
                    RxBus.getInstance().postEmpty(RxEvent.JUMP_ACTIVITY_SOS_MESSAGE);
                    break;

                case 1:
                    RxBus.getInstance().postEmpty(RxEvent.JUMP_ACTIVITY_ECG);
                    break;
            }
        } else {
            Logger.e(TAG, "HomeActivity 不存在");
//            super.onNotifyMessageOpened(context,notificationMessage);

            try {
                //打开自定义的Activity
                i = new Intent(context, WelcomeActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt(IntentParam.START_APP_JUMP_SOURCE,extra.messageType);
                i.putExtra(IntentParam.START_APP_JUMP_SOURCE, extra.messageType);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 收到通知回调（提醒消息）
     *
     * @param context
     * @param notificationMessage
     */
    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        Logger.e(TAG, "onNotifyMessageArrived ：" + notificationMessage.toString());
        boolean isLogin = TextUtils.isEmpty(SPCache.getString(SPCache.KEY_TOKEN,"")) ?false:true;
        if (!isLogin)
            return;
        NotificationExtra extra = JSON.parseObject(notificationMessage.notificationExtras, NotificationExtra.class);
        List<Integer> list = null;
        switch (extra.messageType) {//0-sos通知消息，1-心电通知消息，2-管理后台消息
            case 0:
//                int bageCount = SPCache.getInt(SPCache.KEY_BAGE_COUNT,0);
//                SPCache.putInt(SPCache.KEY_BAGE_COUNT,bageCount+1);
                RxBus.getInstance().postEmpty(RxEvent.REFRESH_MESSAGE_BDAGE);
                break;

            case 1:
            case 2:
//                int noreadCount = SPCache.getInt(SPCache.KEY_BAGE_COUNT_BACKGROUND_NO_READ,0);
//                SPCache.putInt(SPCache.KEY_BAGE_COUNT_BACKGROUND_NO_READ,noreadCount+1);
                RxBus.getInstance().postEmpty(RxEvent.REFRESH_BACKGROUND_MESSAGE_BDAGE);
                break;
        }


        //判断8.0，若为8.0型号的手机进行创下一下的通知栏
//        if (Build.VERSION.SDK_INT >= 26) {
//            sendNotification_O(context,"tian_normal","普通消息通道"
//                    ,"notificationMessage.notificationTitle","notificationMessage.notificationContent","小标题");
//        } else {
//            Notification.Builder builder = new Notification.Builder(context);
//            builder.setSmallIcon(R.mipmap.ic_launcher)
//                    .setWhen(System.currentTimeMillis())
//                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
//                    .setContentTitle(notificationMessage.notificationTitle)
//                    .setContentText(notificationMessage.notificationContent)
//                    .setAutoCancel(true)
//                    .setContentIntent(pendingIntentClick)
//                    .setDeleteIntent(pendingIntentCancel);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                manager.notify(id, builder.build());
//            }
//        }
    }

    /**
     * 清除通知回调
     *
     * @param context
     * @param notificationMessage
     */
    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage notificationMessage) {
        Logger.e(TAG, "onNotifyMessageDismiss");
    }

    /**
     * 注册成功回调
     *
     * @param context
     * @param s
     */
    @Override
    public void onRegister(Context context, String s) {
        Logger.e(TAG, "onRegister =" + s);
        //增加极光regid添加到本地配置
        SPCache.putString(SPCache.KEY_JPUSH_REG_ID, s);
    }


    /**
     * 长连接状态回调
     *
     * @param context
     * @param b
     */
    @Override
    public void onConnected(Context context, boolean b) {
        Logger.e(TAG, "onConnected=" + b);
//        HealthApp.isJPushConnect = b;
//        if (!b){
////            JPushInterface.resumePush(context);
        RxBus.getInstance().post(RxEvent.JPUSH_RETRY_CONNECT, b);
//        }
    }

    /**
     * 注册失败回调
     *
     * @param context
     * @param cmdMessage
     */
    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        Logger.e(TAG, "onCommandResult =" + cmdMessage);
        switch (cmdMessage.cmd) {
            case 0: //注册失败
                RxBus.getInstance().postEmpty(RxEvent.JPUSH_RETRY_REGISTER);
                break;
            case 1000: //自定义消息展示错误
                Logger.e(TAG, "onCommandResult 自定义消息展示错误");
                break;
            case 2003: //isPushStopped 异步回调
//                if (cmdMessage.errorCode == 1){
//                    RxBus.getInstance().postEmpty(RxEvent.JPUSH_RETRY_CONNECT);
//                }
                break;
            case 2004: //getConnectionState 异步回调
                Logger.e(TAG, "onCommandResult JPush getConnectionState");
                break;
            case 2005: //getRegistrationID 异步回调
                Logger.e(TAG, "onCommandResult JPush getRegistrationID");
                break;
            case 2006: //onResume 设置回调
                Logger.e(TAG, "onCommandResult JPush onResume");
                break;
            case 2007: //onStop 设置回调
                Logger.e(TAG, "onCommandResult JPush onStop");
//                RxBus.getInstance().postEmpty(RxEvent.JPUSH_RETRY_CONNECT);
                break;
            case 10000: //厂商 token 注册回调，通过 extra 可获取对应 platform 和 token 信息

                break;

            default:
                break;
        }
    }

    /**
     * tag 增删查改的操作会在此方法中回调结果。
     *
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        Logger.e(TAG, "onTagOperatorResult");
    }

    /**
     * 查询某个 tag 与当前用户的绑定状态的操作会在此方法中回调结果。
     *
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        Logger.e(TAG, "onCheckTagOperatorResult");
    }

    /**
     * alias 相关的操作会在此方法中回调结果。
     *
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        Logger.e(TAG, "onAliasOperatorResult " + jPushMessage.getErrorCode());
    }

    /**
     * 设置手机号码会在此方法中回调结果
     *
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        Logger.e(TAG, "onMobileNumberOperatorResult");
    }

    @Override
    public Notification getNotification(Context context, NotificationMessage notificationMessage) {
        Logger.e(TAG, "getNotification");
        return super.getNotification(context, notificationMessage);
    }


    /**
     * @param context
     * @param isOn    通知开关状态
     * @param source  触发场景，0为sdk启动，1为检测到通知开关状态变更
     */
    @Override
    public void onNotificationSettingsCheck(Context context, boolean isOn, int source) {
        super.onNotificationSettingsCheck(context, isOn, source);
    }

    /**
     * 通过通知渠道发送通知 Android O 新增API
     * 其他的还和以前一样
     *
     * @param channelID   渠道ID
     * @param channelName 渠道名字
     * @param subText     小标题
     * @param title       大标题
     * @param text        内容
     */
    @TargetApi(Build.VERSION_CODES.O)
    public void sendNotification_O(Context context, String channelID, String channelName, String subText, String title, String text) {

        //创建通道管理器
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        NotificationManager manager;
        manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        //构建通知
        Notification.Builder builder = new Notification.Builder(context);
        //设置小图标
        builder.setSmallIcon(R.mipmap.app_logo);
        //设置通知 标题，内容，小标题
        builder.setContentTitle(title);
        builder.setContentText(text);
//        builder.setSubText(subText);
        //设置通知颜色
        builder.setColor(Color.parseColor("#c82226"));
        //设置创建时间
        builder.setWhen(System.currentTimeMillis());
        //创建通知时指定channelID
        builder.setChannelId(channelID);

        Intent resultIntent = new Intent(context, SosMessageActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        //构建一个通知，最后通知管理器 发送
        Notification notification = builder.build();
        manager.notify(1, notification);
    }

}
