package com.example.music;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MusicDatabaseHelper dbHelper;
    MediaPlayer mediaPlayer = new MediaPlayer();
    ListView mListView;
    List<Song> list;
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MusicDatabaseHelper(this, "SongPaper.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "insert into paper(id, name) values(1, '默认歌单')";
        String sql1 = "select * from paper where id = 1";
        Cursor cursor = db.rawQuery(sql1, new String[]{});

        if(!cursor.moveToNext()){
        db.execSQL(sql);}


        initView();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = list.get(position);
//                try{
//                    mediaPlayer.reset();
//                    Uri uri = Uri.parse(song.path);
//                    mediaPlayer.setDataSource(MainActivity.this, uri);
//                    mediaPlayer.prepare();
//                    mediaPlayer.start();
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("song", list.get(position));

                intent.putExtras(bundle);
                intent.putExtra("uri", song.path);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.main_listview);
        list = new ArrayList<>();
        //把扫描到的音乐赋值给list
        list = MusicUtils.getMusicData(this);
        adapter = new MyAdapter(this,list);
        mListView.setAdapter(adapter);
    }
}




