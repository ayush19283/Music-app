package com.ayush.mp3;
import static android.app.Notification.EXTRA_NOTIFICATION_ID;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.games.Player;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final int PICK_AUDIO = 1;
    Uri AudioUri;
    TextView select_Audio;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> artist = new ArrayList<String>();
    ArrayList<String> path = new ArrayList<String>();
    MediaPlayer player;
    TextView chile_txt_view;
    int prev=-1;
    int p=0;

    View.OnClickListener btnClickListener;

    SeekBar seek;
    private Handler mHandler = new Handler();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        window.setNavigationBarColor(this.getResources().getColor(R.color.black));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_main);
        seek=findViewById(R.id.seekbar);




//        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        MainActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(player != null){
                    int mCurrentPosition = player.getCurrentPosition() / 1000;
                    seek.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1000);
            }
        });

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(player != null && fromUser){
                    player.seekTo(progress * 1000);
                }
            }
        });

            runtimePermission();




        LinearLayout li=(LinearLayout)findViewById(R.id.ll);

        li.post(() -> {
            for (int i=0; i<list.size(); i++) {

//                RelativeLayout child_ll = (RelativeLayout) getLayoutInflater().inflate(R.layout.tem, null);
//                child_ll.setId((Integer) i);
//                child_ll.setPadding(20, 45, 20, 10);
//                TextView chile_txt_view = child_ll.findViewById(R.id.textv);
               chile_txt_view = new TextView(this);
                chile_txt_view.setId(i);
                chile_txt_view.setOnClickListener(btnClickListener);
                String art="\n    "+artist.get(i);
                String tsk_title = "  "+list.get(i);
                if (tsk_title.length() > 30){
                    tsk_title = tsk_title.substring(0, 20)+"...";
                }

                if (art.length() > 30){
                    art = art.substring(0, 20)+"...";
                }

                String boldText = tsk_title;// + "" + (Integer) task.get(0);
//                String normalText;
//                if (task.get(1).toString().length() >= 20) {
//                    normalText = "\n" + task.get(1).toString().substring(0, 20) + "...";
//                }
//                else{
//                    normalText = "\n" + task.get(1).toString();
//                }
                SpannableString str = new SpannableString(boldText);
                str.setSpan(new ForegroundColorSpan(Color.WHITE), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                str.setSpan(new RelativeSizeSpan(1.4f), 0,str.length(), 0);//                str.setSpan(new StyleSpan(Typeface.NORMAL), 0, boldText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                str.setSpan(new StyleSpan(Typeface.NORMAL), 0, art.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                chile_txt_view.setText(str);

                Spannable wordTwo = new SpannableString(art);
                wordTwo.setSpan(new ForegroundColorSpan(Color.RED), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                wordTwo.setSpan(new RelativeSizeSpan(0.8f), 0,wordTwo.length(), 0);//                str.setSpan(new StyleSpan(Typeface.NORMAL), 0, boldText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                chile_txt_view.append(wordTwo);
                chile_txt_view.setPadding(10,30,0,30);

//                chile_txt_view.setText(str);
//              //  chile_txt_view.setBackgroundColor(Color.YELLOW);
//                chile_txt_view.setTextColor(Color.parseColor("#FF0000"));

                View viewDivider = new View(MainActivity.this);
                viewDivider.setBackgroundColor(Color.parseColor("#5e5e5e"));
                int dividerHeight = (int) (getResources().getDisplayMetrics().density * 1); // 1dp to pixels
                viewDivider.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dividerHeight));
                li.addView(chile_txt_view);
//                li.addView(child_ll);
//                li.addView(viewDivider);

            }




        });

        btnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(prev<0) {
                }
                else{

                    TextView t1 =findViewById(prev);
                    String tsk_title = "  "+list.get(prev);
                    if (tsk_title.length() > 30){
                        tsk_title = tsk_title.substring(0, 20)+"...";
                    }
                    String boldText = tsk_title;

                    SpannableString str = new SpannableString(boldText);
                    str.setSpan(new ForegroundColorSpan(Color.WHITE), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    str.setSpan(new RelativeSizeSpan(1.4f), 0,str.length(), 0);//                str.setSpan(new StyleSpan(Typeface.NORMAL), 0, boldText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                str.setSpan(new StyleSpan(Typeface.NORMAL), 0, art.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    t1.setText(str);

                    String art="\n    "+artist.get(prev);
                    if (art.length() > 30){
                        art = art.substring(0, 20)+"...";
                    }

                    Spannable wordTwo = new SpannableString(art);
                    wordTwo.setSpan(new ForegroundColorSpan(Color.RED), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    wordTwo.setSpan(new RelativeSizeSpan(0.8f), 0,wordTwo.length(), 0);//                str.setSpan(new StyleSpan(Typeface.NORMAL), 0, boldText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    t1.append(wordTwo);

                }
                prev=v.getId();
                TextView t1 =findViewById(v.getId());
                String tsk_title = "  "+list.get(v.getId());
                if (tsk_title.length() > 30){
                    tsk_title = tsk_title.substring(0, 20)+"...";
                }
                String boldText = tsk_title;

                SpannableString str = new SpannableString(boldText);
                str.setSpan(new ForegroundColorSpan(Color.rgb(135,206,235)), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                str.setSpan(new RelativeSizeSpan(1.4f), 0,str.length(), 0);//                str.setSpan(new StyleSpan(Typeface.NORMAL), 0, boldText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                str.setSpan(new StyleSpan(Typeface.NORMAL), 0, art.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                t1.setText(str);

                String art="\n    "+artist.get(v.getId());
                if (art.length() > 30){
                    art = art.substring(0, 20)+"...";
                }

                Spannable wordTwo = new SpannableString(art);
                wordTwo.setSpan(new ForegroundColorSpan(Color.rgb(135,206,235)), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                wordTwo.setSpan(new RelativeSizeSpan(0.8f), 0,wordTwo.length(), 0);//                str.setSpan(new StyleSpan(Typeface.NORMAL), 0, boldText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                t1.append(wordTwo);

                seek=findViewById(R.id.seekbar);
                TextView tt =findViewById(R.id.set);
                tt.setTypeface(null,Typeface.BOLD);
                tt.setText(list.get(v.getId()));
                tt.setSelected(true);

               findViewById(R.id.start).setBackgroundResource(R.drawable.button);
                p=1;
                System.out.println("fdfffffffffffffffffffffffffffffffff"+v.getId());

                if(player!=null){

                    player.release();
                    player=null;
                }
                 player = new MediaPlayer();
//                player.stop();



                try {
                    player.setDataSource(path.get(v.getId()));
                    player.prepare();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("Exception of type : " + e.toString());
                    e.printStackTrace();
                }
                player.start();
                seek.setMax(player.getDuration()/1000);
            }

        };

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(p==1){
               player.pause();
                    findViewById(R.id.start).setBackgroundResource(R.drawable.button2);
                p=0;}
                else{
                    player.start();
                    findViewById(R.id.start).setBackgroundResource(R.drawable.button);
                    p=1;
                }

            }
        });



//        player.start();

//        Random rnd = new Random();
//        int prevTextViewId = 0;
//        for(int i = 0; i < list.size(); i++)
//        {
//            final TextView textView = new TextView(this);
//            textView.setText(list.get(i));
//            textView.setTextColor(rnd.nextInt() | 0xff000000);
//            textView.setBackgroundColor(Color.GREEN);
//
//            int curTextViewId = prevTextViewId + 1;
//            textView.setId(curTextViewId);
//            final RelativeLayout.LayoutParams params =
//                    new RelativeLayout.LayoutParams(
//                           700,
//                            500
//                    );
//
//            params.addRule(RelativeLayout.BELOW, prevTextViewId);
//            textView.setLayoutParams(params);
//
//            prevTextViewId = curTextViewId;
//            li.addView(textView, params);


//            TextView tv1 = new TextView(this);
//            tv1.setText(list.get(i));
//
//            layout.addView(tv1);
        }


    private void runtimePermission(){

        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getSongListFromStorage(); // display song list, when permission granted.
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(MainActivity.this, "Please grant permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }


                    public void onPermissionRationaleShouldBeShown(android.webkit.PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest(); // if user denied permission.
                    }
                }).check();
    }


    public void getSongListFromStorage() {
        //retrieve song info
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int data = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.DATA);

            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String d= musicCursor.getString(data);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
             //   System.out.println(d);
                list.add(thisTitle);
                artist.add(thisArtist);
                path.add(d);
               // System.out.println("dffffffffffffffffffffffff"+"    "+d);
              //  songList.add(new Song(thisId, thisTitle, thisArtist));
            } while (musicCursor.moveToNext());
        }

//        for(String i:list){
//            System.out.println(i);
//        }
    }

}
