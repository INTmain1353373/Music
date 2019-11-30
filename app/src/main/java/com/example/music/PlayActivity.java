package com.example.music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class PlayActivity extends AppCompatActivity {
    private MusicDatabaseHelper dbHelper;
    Button btOn = null;
    MediaPlayer mediaPlayer = new MediaPlayer();
    SeekBar bar = null;
    boolean isChanging = true;
    Song song = null;
    TextView currentTime = null;
    TextView allTime = null;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            currentTime.setText(formatTime(Integer.parseInt(msg.obj.toString())));
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHelper = new MusicDatabaseHelper(this, "SongPaper.db", null, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        btOn = findViewById(R.id.onPause);
        bar = findViewById(R.id.seekbar);
        currentTime = findViewById(R.id.currentTime);
        allTime = findViewById(R.id.allTime);

        Intent intent = getIntent();
        song = (Song) intent.getSerializableExtra("song");
        Log.e("name", song.song);
        Log.e("path", song.path);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "insert into song(name, path, idOfPaper) values(?,?,?)";
        String sql1 = "select * from song where name = ? and idOfPaper = ?";
        Cursor cursor = db.rawQuery(sql1, new String[]{song.song, String.valueOf(1)});
        if(cursor.moveToNext()){
            return;
        }
        else
            db.execSQL(sql, new String[]{song.song, song.path, String.valueOf(1)});



//        try {
//            //mediaPlayer.release();
//            mediaPlayer.reset();
//
//            Intent intent = getIntent();
//            String path = intent.getStringExtra("uri");
//            Log.e("path", path);
//            Uri uri = Uri.parse(path);
//            mediaPlayer.setDataSource(path);
//            mediaPlayer.prepare();
//            mediaPlayer.setLooping(true);
//            bar.setMax(mediaPlayer.getDuration());
//            allTime.setText("/" + formatTime(mediaPlayer.getDuration()));
//            Log.e("max", String.valueOf(bar.getProgress()));
//            mediaPlayer.start();
//            new Thread(new SeekBarThread()).start();
//           // new Thread(new SeekBarThread()).start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isChanging = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

                isChanging = false;
                new Thread(new SeekBarThread()).start();
            }
        });

        btOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()) {
            //hmediaPlayer.pause();
        }
    }

    //@Override
//    protected void onStart() {
//        super.onStart();
//        if (!mediaPlayer.isPlaying()) {
//            mediaPlayer.start();
//            new Thread(new SeekBarThread()).start();
//        }
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            new Thread(new SeekBarThread()).start();
        }
    }

    class SeekBarThread implements Runnable {

        @Override
        public void run() {
            while (mediaPlayer.isPlaying()) {
                Message message = new Message();
                message.obj =  bar.getProgress();
                handler.sendMessage(message);
                //Log.e("jindu", String.valueOf(bar.getProgress()));
                // 将SeekBar位置设置到当前播放位置
                bar.setProgress(mediaPlayer.getCurrentPosition());
                try {
                    // 每100毫秒更新一次位置
                    Thread.sleep(100);
                    //播放进度
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;

        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }

    }
}








