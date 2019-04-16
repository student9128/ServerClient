package com.kevin.serverclient;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * Created by Kevin on 2019/4/9<br/>
 * Blog:https://blog.csdn.net/student9128<br/>
 * Describe:<br/>
 */
public class MessengerService extends Service {
    private static final String TAG = "MessengerService";

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    Log.d(TAG, "receive msg from client:" + msg.getData().getString("msg"));
                    Messenger messenger = msg.replyTo;
                    Message s = Message.obtain(null,1002);
                    Bundle bundle = new Bundle();
                    bundle.putString("replyTo","Nice to receive your message! This is server");
                    s.setData(bundle);
                    try {
                        messenger.send(s);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
