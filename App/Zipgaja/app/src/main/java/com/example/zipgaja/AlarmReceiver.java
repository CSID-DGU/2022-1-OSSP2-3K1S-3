package com.example.zipgaja;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    public AlarmReceiver() { }

    NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    // 오레오 이상은 반드시 채널을 설정해줘야 Notification이 작동함
    public static String channel_ID = "channelID";
    public static String channel_NAME = "channelName";

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        builder = null;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationManager.createNotificationChannel(
                    new NotificationChannel(channel_ID, channel_NAME, NotificationManager.IMPORTANCE_HIGH)
            );
            builder = new NotificationCompat.Builder(context, channel_ID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        // 알림창 클릭 시 activity 화면 부름
        Intent intent2 = new Intent(context, SplashActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 101, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 101, intent2, PendingIntent.FLAG_MUTABLE);


        builder.setContentTitle("집가자")
                .setContentText("현재 시각 23시! 막차 놓치지 마세요 :)")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Notification notification = builder.build();
        notificationManager.notify(1, notification);

    }

}

// https://always-21.tistory.com/4