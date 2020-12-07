package com.example.foser;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Timer;
import java.util.TimerTask;

public class MyForegroundService extends Service {
    public static final String CHANNEL_ID = "MyForegroundServiceChannel";
    public static final String CHANNEL_NAME = "FoSer service channel";

    //2. Odczyt danych zapisanych w Intent
    public static final String MESSAGE = "message";
    public static final String TIME = "time";
    public static final String WORK = "work";
    public static final String WORK_DOUBLE = "work_double";

    public static final String TIME_2S = "work2s";
    public static final String TIME_5S = "work5s";
    public static final String TIME_10S = "work10s";

    //3. Wartości ustawień
    private String message;
    private Boolean show_time, do_work, double_speed, work2s, work5s, work10s;

    private final long period2s = 2000; //2s
    private final long period5s = 5000; //5s
    private final long period10s = 10000; //10s

    //4.
    private Context ctx;
    private Intent notificationIntent;
    private PendingIntent pendingIntent;

    //5.
    private int counter;
    private Timer timer;
    private TimerTask timerTask;
    final Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = this;
        notificationIntent = new Intent(ctx, MainActivity.class);
        pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

        counter = 0;

        timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                counter++;
                handler.post(runnable);
            }
        };
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        timer.cancel();
        timer.purge();
        timer = null;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        message = intent.getStringExtra(MESSAGE);
        show_time = intent.getBooleanExtra(TIME,false);
        do_work = intent.getBooleanExtra(WORK,false);
        double_speed = intent.getBooleanExtra(WORK_DOUBLE,false);
        work2s = intent.getBooleanExtra(TIME_2S, true);
        work5s = intent.getBooleanExtra(TIME_5S, false);
        work10s = intent.getBooleanExtra(TIME_10S, false);


        createNotificationChannel();

        Notification notification = new Notification.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_my_icon)
                .setContentTitle(getString(R.string.ser_title))
                .setShowWhen(show_time)
                .setContentText(message)
                .setLargeIcon(BitmapFactory.decodeResource (getResources() , R.drawable.circle ))
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1,notification);

        doWork();

        return START_NOT_STICKY;
    }

    private void doWork() {
        if(do_work) {
            if (work2s){
                timer.schedule(timerTask, 0L, double_speed ? period2s / 2L : period2s);
            }
            else if (work5s){
                timer.schedule(timerTask, 0L, double_speed ? period5s / 2L : period5s);
            }
            else if (work10s){
                timer.schedule(timerTask, 0L, double_speed ? period10s / 2L : period10s);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
    }

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Notification notification = new Notification.Builder(ctx, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_my_icon)
                    .setContentTitle(getString(R.string.ser_title))
                    .setShowWhen(show_time)
                    .setContentText(message + " " + String.valueOf(counter))
                    .setLargeIcon(BitmapFactory.decodeResource (getResources() , R.drawable.circle ))
                    .setContentIntent(pendingIntent)
                    .build();

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.notify(1,notification);
        }
    };
}
