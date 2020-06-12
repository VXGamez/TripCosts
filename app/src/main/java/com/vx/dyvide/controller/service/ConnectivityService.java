package com.vx.dyvide.controller.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.vx.dyvide.model.DB.DB;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectivityService extends Service{

    public static String TAG_INTERVAL = "interval";
    private int interval;
    private boolean hadInternet=true;
    private Timer mTimer = null;
    public static final String Broadcast_CONNECTION_REGAINED = "com.prpr.androidpprog2.entregable.CONNECTION_REGAINED";
    public static final String Broadcast_CONNECTION_LOST = "com.prpr.androidpprog2.entregable.CONNECTION_LOST";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    void hasInternetConnection(){
        if(!hadInternet){
            hadInternet = true;
            Intent broadcastIntent = new Intent(Broadcast_CONNECTION_REGAINED);
            sendBroadcast(broadcastIntent);
        }
    }

    void hasNoInternetConnection(){
        if(hadInternet){
            hadInternet=false;
            Intent broadcastIntent = new Intent(Broadcast_CONNECTION_LOST);
            sendBroadcast(broadcastIntent);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        interval = intent.getIntExtra(TAG_INTERVAL, 10);

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new CheckForConnection(), 0, interval * 1000);

        return super.onStartCommand(intent, flags, startId);
    }


    class CheckForConnection extends TimerTask{
        @Override
        public void run() {
            isNetworkAvailable();
        }
    }

    @Override
    public void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }

    private boolean isNetworkAvailable(){
        boolean internet = false;
        if (!DB.noInternet(getApplication())) {
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("https://www.google.es/").openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                internet =  (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0);
                if(!internet){
                    hasInternetConnection();
                }
                return internet;
            } catch (IOException e) {
                hasNoInternetConnection();
            }
        } else {
           hasNoInternetConnection();
        }
        return false;
    }

}

