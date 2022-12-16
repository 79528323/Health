package com.gzhealthy.health.service;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;


public class ExitAppService extends Service {

    private final String TAG = ExitAppService.class.getSimpleName();
    private Boolean isQuit = false;// 退出应用标识符
    private Timer timer = new Timer();// 程序退出定时器

    private Messenger clientMessenger;


    class ServerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:  //得到客户端的信使对象
                    clientMessenger = msg.replyTo;
                    int what = 0;
                    if (!isQuit) {
                        what = 0;
                        isQuit = true;
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                isQuit = false;
                            }
                        };
                        timer.schedule(task, 2000);
                    } else {
                        what = 1;
                    }
                    Message msgFromServer = Message.obtain(null, what);
                    try {
                        clientMessenger.send(msgFromServer);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    final Messenger serverMessenger = new Messenger(new ServerHandler());


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serverMessenger.getBinder();
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        isQuit = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
