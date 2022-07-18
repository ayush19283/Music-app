package com.ayush.mp3;

import android.app.Notification;
import android.content.ContentResolver;
import android.media.MediaPlayer;
import android.widget.RemoteViews;

import java.util.ArrayList;

public class Globals {
    public static ArrayList<String> list = new ArrayList<String>();
    public static ArrayList<String> artist = new ArrayList<String>();
    public static ArrayList<String> path = new ArrayList<String>();

    public static Notification.Builder builder;
    public static RemoteViews contentView;
    public static ContentResolver musicResolver;
    public static songlist sn;
    public static MediaPlayer player;
    public static String a;
    public static String s;
}
