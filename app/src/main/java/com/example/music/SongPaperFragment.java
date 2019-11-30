package com.example.music;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

}
