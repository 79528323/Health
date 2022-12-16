package com.gzhealthy.health.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.gzhealthy.health.R;
import com.gzhealthy.health.logger.Logger;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;


public class HealthyBackgroundService extends Service {

    private final String TAG = HealthyBackgroundService.class.getSimpleName();
//    private Boolean isQuit = false;// 退出应用标识符
//    private Timer timer = new Timer();// 程序退出定时器
//
//    private Messenger clientMessenger;

    private ServerHandler serverHandler;
    private int count=0;
    class ServerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            count = msg.arg1;
            if (count >0 ){
                count--;
                msg = obtainMessage();
                msg.arg1 = count;
                this.sendMessageDelayed(msg,1000);
                Logger.e("111","HealthyBackgroundService  -------> 倒数count中。。剩余="+count);
            }
        }
    }

//    final Messenger serverMessenger = new Messenger(new ServerHandler());


//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return serverMessenger.getBinder();
//    }

//    @Override
//    public void unbindService(ServiceConnection conn) {
//        super.unbindService(conn);
////        isQuit = false;
//    }


//    @Override
//    public void onStart(Intent intent, int startId) {
//        Logger.e("111","HealthyBackgroundService  -------> onStart");
//        super.onStart(intent, startId);
//    }


    @Override
    public void onCreate() {
        super.onCreate();
        Logger.e("111","HealthyBackgroundService  -------> onCreate");
        serverHandler = new ServerHandler();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //NotificationCompat.Builder builder =
            //new NotificationCompat.Builder(this, CHANNEL_ID)
            //         .setContentTitle("")
            //         .setContentText("");
            NotificationChannel channel = new NotificationChannel("1001", "体安", NotificationManager.IMPORTANCE_MIN);
            channel.enableVibration(false);//去除振动

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) manager.createNotificationChannel(channel);

            Notification.Builder builder = new Notification.Builder(getApplicationContext(),"1001")
//                    .setContentTitle("正在后台运行")
                    .setSmallIcon(R.mipmap.app_logo);
            startForeground(1, builder.build());//id must not be 0,即禁止是0
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int flag = START_STICKY;
        count = intent.getIntExtra("count",0);
        Message msg = new Message();
        msg.arg1 = count;
        serverHandler.sendMessage(msg);
        Logger.e("111","HealthyBackgroundService  -------> onStartCommand");
        return super.onStartCommand(intent, flag, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.e("111","HealthyBackgroundService  -------> onDestroy");
    }
}
