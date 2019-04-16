package com.kevin.serverclient;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * Created by Kevin on 2019/4/9<br/>
 * Blog:https://blog.csdn.net/student9128<br/>
 * Describe:<br/>
 */
public class TestService extends Service {
    private static final String TAG = "serverTestService";
    private Binder mBinder = new ITest.Stub() {
        @Override
        public void test() throws RemoteException {
            Log.d(TAG, "Server...");
        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                android.os.Process.killProcess(android.os.Process.myPid());
                Log.d(TAG, "server stop self......");

            }
        }).start();
        return mBinder;
    }
}
