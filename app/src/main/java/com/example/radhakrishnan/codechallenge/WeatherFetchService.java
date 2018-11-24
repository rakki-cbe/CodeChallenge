package com.example.radhakrishnan.codechallenge;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.radhakrishnan.codechallenge.Network.NetworkHelper;
import com.example.radhakrishnan.codechallenge.Network.WeatherApiResponse;
import com.example.radhakrishnan.codechallenge.Network.WeatherServiceHelper;
import com.example.radhakrishnan.codechallenge.database.DbHelper;
import com.example.radhakrishnan.codechallenge.database.WeatherTabeHelper;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherFetchService extends Service implements InterFaces.WeatherService {
    public static int UPDATE_UI=2;
    MyBinder binder=new MyBinder();
    MyThread thread;
    InterFaces.weatherDataInserted callBack;
    private String log="Service";
    MyHandler handler= new MyHandler(Looper.getMainLooper());
    private  static boolean isBound=false;
    private  static boolean isFirstTime=false;
    WeatherServiceHelper helper;
    DbHelper db;
    @Override
    public void onCreate() {
        super.onCreate();
        db=new DbHelper(this);
        helper = new WeatherServiceHelper(db);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(log,"On Start Called");
        if(thread==null  ||!thread.isAlive() ) {
            thread = new MyThread();
            thread.loop=true;
            thread.start();
        }
        return START_STICKY;
    }

    public void stopThread()
    {
        if(thread!=null ){
            thread.loop=false;
        }
        thread=null;

    }


    @Override
    public void boundFromActivity() {
        stopForegroundService();
    }

    @Override
    public void setCallBack(InterFaces.weatherDataInserted callBack) {
        this.callBack=callBack;
    }
    public void forceDataUpdate(){
        new Thread(new SingleTimeThread()).start();
    }

    @Override
    public SQLiteDatabase getReadDataBase() {
        return db.getReadableDatabase();
    }
    @Override
    public SQLiteDatabase getWriteDataBase() {
        return db.getWritableDatabase();
    }

    @Override
    public void getDataForOnceCity(String city) {
        new Thread(new ForOneCity(city)).start();

    }


    /* Used to build and start foreground service. */
    private void startForegroundService()
    {
        Log.d(log, "Start foreground service.");

        // Create notification default intent.

        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Create notification builder.
        String channelId ="";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId= createNotificationChannel("my_service", "Weather Update");
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,channelId);

        // Make notification show big text.
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Updating weather");
      //  bigTextStyle.bigText("Android foreground service is a android service which can run in foreground always, it can be controlled by user via notification.");
        // Set big text style.
        builder.setStyle(bigTextStyle);

        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        /*Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_music_32);
        builder.setLargeIcon(largeIconBitmap);*/
        // Make the notification max priority.
        builder.setPriority(Notification.PRIORITY_MAX);
        // Make head-up notification.
        builder.setFullScreenIntent(pendingIntent, true);

      /*  // Add Play button intent in notification.
        Intent playIntent = new Intent(this, MyForeGroundService.class);
        playIntent.setAction(ACTION_PLAY);
        PendingIntent pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0);
        NotificationCompat.Action playAction = new NotificationCompat.Action(android.R.drawable.ic_media_play, "Play", pendingPlayIntent);
        builder.addAction(playAction);

        // Add Pause button intent in notification.
        Intent pauseIntent = new Intent(this, MyForeGroundService.class);
        pauseIntent.setAction(ACTION_PAUSE);
        PendingIntent pendingPrevIntent = PendingIntent.getService(this, 0, pauseIntent, 0);
        NotificationCompat.Action prevAction = new NotificationCompat.Action(android.R.drawable.ic_media_pause, "Pause", pendingPrevIntent);
        builder.addAction(prevAction);
*/
        // Build the notification.
        Notification notification = builder.build();

        // Start foreground service.
        startForeground(1, notification);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName){
        NotificationChannel chan = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager service = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId;
    }
    private void stopForegroundService()
    {
        Log.d(log, "Stop foreground service.");

        // Stop foreground service and remove the notification.
        stopForeground(true);


    }


    @Override
    public IBinder onBind(Intent intent) {
        isBound=true;
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isBound=false;
        startForegroundService();
        return super.onUnbind(intent);

    }

    @Override
    public void onDestroy() {
        Log.d(log, "Service Destroyed.");
        super.onDestroy();
    }


    class MyThread extends Thread{
        public boolean loop=true;
        @Override
        public void run() {
            super.run();
            while (loop){
                try {
                    if(isBound || !isFirstTime ){
                        callAndUpdateUi();
                        if(!isBound){
                            isFirstTime=true;
                        }
                    } else {
                        isFirstTime = false;
                    }
                    Thread.sleep(30*60*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    class SingleTimeThread implements Runnable{
        @Override
        public void run() {
                        callAndUpdateUi();


            }


    }
    class ForOneCity implements Runnable{
        String city;

        public ForOneCity(String city) {
            this.city = city;
        }

        @Override
        public void run() {
                callAndUpdateUi(city);



        }


    }

    private void callAndUpdateUi()  {
        boolean b= false;
        try {
            b = helper.fetchData();
        } catch (IOException e) {
            e.printStackTrace();
            b=false;
        }
        Message message = handler.obtainMessage(UPDATE_UI,b);
        message.sendToTarget();
    }

    private void callAndUpdateUi(String city)  {
        boolean b= false;
        try {
            b = helper.fetchData(city);
        } catch (IOException e) {
            e.printStackTrace();
            b=false;
        }
        Message message = handler.obtainMessage(UPDATE_UI,b);
        message.sendToTarget();
    }


    @Override
    public void onRebind(Intent intent) {
        isBound=true;
        super.onRebind(intent);
    }

    class MyBinder extends Binder {
      public   InterFaces.WeatherService getService(){
            return WeatherFetchService.this;
        }


    }

    public boolean isInterNetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;

    }
    class MyHandler extends Handler{


        public MyHandler(Looper mainLooper) {
            super(mainLooper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==UPDATE_UI){
                if(callBack!=null){
                    callBack.weatherDataProcessed((Boolean) msg.obj);
                }
            }
        }
    }

}






