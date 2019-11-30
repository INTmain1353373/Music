package com.example.music;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class SongPaperFragment extends Fragment {

    TextView title;
    ListView listView;
    MySongAdapter adapter;
    List<Song> dataList = new ArrayList<>();
    SQLiteDatabase db;
    MusicDatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.songpaper, container, false);

        title = (TextView) view.findViewById(R.id.title_text);
        listView = (ListView) view.findViewById(R.id.list_view);
        initSong();
        adapter = new MySongAdapter(getContext(), R.layout.song_listview, dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(getContext(), "click", Toast.LENGTH_LONG).show();
                PlayActivity activity =(PlayActivity) getActivity();
                Song song = dataList.get(position);

                activity.drawerLayout.closeDrawers();
                activity.playMusic(song);
            }
        });
    }

    private void initSong(){
        dbHelper = new MusicDatabaseHelper(getContext(), "SongPaper.db", null, 1);
        db = dbHelper.getReadableDatabase();
        String sql = "select * from song where idOfPaper = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(1)});
        while (cursor.moveToNext()){
            Song song = new Song();
            song.song = cursor.getString(0);
            song.path = cursor.getString(1);
            dataList.add(song);
        }
    }

    public void next(Song song){
//        Log.e("0", dataList.get(0).song);
//        Log.e("传入name", song.song);
//        Log.e("path", dataList.get(0).path);
//        Log.e("传入path",song.path);

        int position = dataList.indexOf(song);
        //Log.e("当前位置", String.valueOf(position));
        position = (position + 1) % dataList.size();
        PlayActivity activity =(PlayActivity) getActivity();
        Song song1 = dataList.get(position);

        //activity.drawerLayout.closeDrawers();
        activity.playMusic(song1);

    }

    public void last(Song song){
        int position = dataList.indexOf(song);
        Log.e("当前位置", String.valueOf(position));
        if(position != 0)
        position = (position - 1) % dataList.size();
        else
            position = dataList.size() - 1;
        PlayActivity activity =(PlayActivity) getActivity();
        Song song1 = dataList.get(position);

        //activity.drawerLayout.closeDrawers();
        activity.playMusic(song1);
    }



}
