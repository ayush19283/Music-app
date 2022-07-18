package com.ayush.mp3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
public class AudioPlayerBroadcastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if(action.equalsIgnoreCase("com.example.app.ACTION_PLAY")){
            // do your stuff to play action;
        }
    }
}
