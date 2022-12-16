package com.gzhealthy.health.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gzhealthy.health.activity.WelcomeActivity;
//import com.umeng.message.entity.UMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationClickReceiver extends BroadcastReceiver {

    public final static String DOWN_ACTION = "DownClick";
    public final static String CLOSE_ACTION = "CloseClick";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String message = intent.getStringExtra("msg");
//        L.i("接收到消息内容：" + message);
//        try {
//            final UMessage msg = new UMessage(new JSONObject(message));
//            switch (action) {
//                case DOWN_ACTION:
//                    if ("go_app".equals(msg.after_open)) { //打开应用
//                        Intent data = new Intent(intent);
//                        data.setClass(context, WelcomeActivity.class);
//                        data.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//需为Intent添加Flag：Intent.FLAG_ACTIVITY_NEW_TASK，否则无法启动Activity。
//                        context.startActivity(data);
//                    } else if ("go_custom".equals(msg.after_open)) {//自定义行为
////                        PublicStartMethod.go_custom(msg.custom, context);
//                    }
//                    break;
//                case CLOSE_ACTION:
//                    //处理第2个按钮事件，关闭通知
////                    Logger.d("=======", "CLOSE_ACTION");
//                    break;
//                default:
//                    break;
//            }

//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }
}
