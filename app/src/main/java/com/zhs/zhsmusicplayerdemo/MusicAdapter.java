package com.zhs.zhsmusicplayerdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 木头 on 2016/11/22.
 */
public class MusicAdapter extends ArrayAdapter<MusicInfo> {
    private int resourceId;
    public MusicAdapter(Context context, int textViewResourceId, List<MusicInfo> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent){
        MusicInfo musicInfo = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView songname = (TextView) view.findViewById(R.id.song_name);
        TextView singername = (TextView) view.findViewById(R.id.singer_name);
        songname.setText(musicInfo.getSongName());
        singername.setText(musicInfo.getSingerName());
        return view;
    }

}
