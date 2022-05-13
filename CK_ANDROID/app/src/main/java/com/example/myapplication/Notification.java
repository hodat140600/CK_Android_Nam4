package com.example.myapplication;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.View;

import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class Notification extends Application {

    public void SendNotification(Context context, String textTitle, String textContent){
        Bitmap bitmapIcon = BitmapFactory.decodeResource(context.getResources(),R.drawable._xdlogo);
        android.app.Notification notification = new NotificationCompat.Builder(context, MyApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_notification)
                .setLargeIcon(bitmapIcon)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null) {
            notificationManager.notify(GetNotificationID(), notification);
        }
    }
    private int GetNotificationID(){
        return (int) new Date().getTime();
    }

}
