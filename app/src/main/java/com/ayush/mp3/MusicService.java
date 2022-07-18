package com.ayush.mp3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class MusicService extends Service {


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            createNotificationChannel("channelID", "channelName");
        }

        Globals.contentView = new RemoteViews(getPackageName(), R.layout.notification);
        Globals.contentView.setImageViewResource(R.id.notif_play_pause, R.drawable.ic_pause_notif);
        Globals.contentView.setTextViewText(R.id.song, Globals.s);
        Globals.contentView.setTextViewText(R.id.artistAlbum, Globals.a);

        Globals.builder =
                new Notification.Builder(this, "channelID")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setVibrate(new long[]{-1})
                        .setOnlyAlertOnce(true)
                        .setContent(Globals.contentView);
        startForeground(1, Globals.builder.build());


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId ,String channelName){
        NotificationChannel chan = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_HIGH);
        chan.setLightColor(Color.BLUE);
        chan.setDescription("hi");
        chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        android.app.NotificationManager manager = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(chan);
        return channelId;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
