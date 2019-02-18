package com.zhs.zhsmusicplayerdemo.Activities;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhs.zhsmusicplayerdemo.Model.MusicDao.MusicInfo;
import com.zhs.zhsmusicplayerdemo.Model.MusicDao.MusicInfoDBManager;
import com.zhs.zhsmusicplayerdemo.R;

import java.util.List;


public class MusicAdapter extends ArrayAdapter<MusicInfo> {
    private int resourceId;
    private MusicInfoDBManager musicInfoDBManager;
    public MusicAdapter(Context context, int textViewResourceId, List<MusicInfo> objects){
        super(context,textViewResourceId,objects);
        musicInfoDBManager = new MusicInfoDBManager(getContext());
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(final int position, View converView, ViewGroup parent){
        MusicInfo musicInfo = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView songname = (TextView) view.findViewById(R.id.song_name);
        TextView singername = (TextView) view.findViewById(R.id.singer_name);
        final ImageView like = (ImageView) view.findViewById(R.id.like);
        songname.setText(musicInfo.getSongName());
        singername.setText(musicInfo.getSingerName());
        if(musicInfo.getIsLike() == 1) {
            like.setImageResource(R.drawable.hadliked);
        }

        if(musicInfo.getFilePath() == null){
            like.setVisibility(View.INVISIBLE);
        }

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getItem(position).getIsLike() == 1){
                    getItem(position).setIsLike( 0 );
                    like.setImageResource(R.drawable.like);
                    musicInfoDBManager.updateLike(getItem(position));
                }else {
                    getItem(position).setIsLike( 1 );
                    like.setImageResource(R.drawable.hadliked);
                    musicInfoDBManager.updateLike(getItem(position));
                }
            }
        });
        return view;
    }

}
