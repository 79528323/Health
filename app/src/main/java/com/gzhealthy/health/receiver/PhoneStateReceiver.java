//package com.gzhealthy.health.receiver;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.IBinder;
//import android.telephony.TelephonyManager;
//
//import com.gzhealthy.health.logger.Logger;
//
///**
// * Created by Justin_Liu
// * on 2021/4/21
// */
//public class PhoneStateReceiver extends BroadcastReceiver {
//    private static boolean inComingFlag = false;
////    private static String incomePhoneNum ="";
//    private TelephonyManager manager;
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        switch (manager.getCallState()){
//            case TelephonyManager.CALL_STATE_RINGING:
//                //来电
//                String incomePhoneNum = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);//来电号码
//                Logger.e("PhoneStateReceiver","来电号码："+incomePhoneNum);
//                break;
//
//                case TelephonyManager.CALL_STATE_OFFHOOK:
//                    Logger.e("PhoneStateReceiver","响铃");
//                    break;
//        }
//    }
//
//    @Override
//    public IBinder peekService(Context myContext, Intent service) {
//        return super.peekService(myContext, service);
//    }
//
//    public void sendPhoneCall(String incomePhoneNum){
//
//    }
//}
