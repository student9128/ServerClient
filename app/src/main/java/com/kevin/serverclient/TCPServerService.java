package com.kevin.serverclient;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Created by Kevin on 2019/4/10<br/>
 * Blog:https://blog.csdn.net/student9128<br/>
 * Describe:<br/>
 */
public class TCPServerService extends Service {
    private static final String TAG = "TCPServerService";
    private boolean mIsServiceDestroyed = false;
    private String[] mDefinedMessage = new String[]{"Hello!",
            "What is your name,please?",
            "It is Sunny,Wonderful!",
            "Do your know,i can chat with more than one person",
            "I am AI"};

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new TCPServer()).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsServiceDestroyed = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class TCPServer implements Runnable {

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(8688);
            } catch (IOException e) {
                Log.d(TAG, "establish tcp server failed,port:8688");
                e.printStackTrace();
                return;
            }
            while (!mIsServiceDestroyed) {
                try {
                    final Socket client = serverSocket.accept();
                    Log.d(TAG, "accept");
                    new Thread(
                    ) {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        private void responseClient(Socket client) throws IOException {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);
            out.println("Welcome to Chatting Room!!!");
            while (!mIsServiceDestroyed) {
                String str = in.readLine();
                Log.d(TAG, "msg from client:" + str);
                if (str == null) {
                    //客户端断开连接
                    break;
                }
                int i = new Random().nextInt(mDefinedMessage.length);
                String msg = mDefinedMessage[i];
                out.println(msg);
                Log.d(TAG, "send:" + msg);
            }

            in.close();
            out.close();
            client.close();
        }
    }
}
