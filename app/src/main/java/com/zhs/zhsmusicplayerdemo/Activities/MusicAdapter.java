package com.zhs.zhsmusicplayerdemo.Activities;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhs.zhsmusicplayerdemo.Model.CollectionDAO.Collection;
import com.zhs.zhsmusicplayerdemo.Model.CollectionDAO.CollectionDBManager;
import com.zhs.zhsmusicplayerdemo.Model.MusicDao.MusicInfo;
import com.zhs.zhsmusicplayerdemo.Model.MusicDao.MusicInfoDBManager;
import com.zhs.zhsmusicplayerdemo.R;

import java.util.List;
import java.util.zip.Inflater;


public class MusicAdapter extends ArrayAdapter<MusicInfo> {
    private int resourceId;
    private CollectionDBManager collectionDBManager;
    private String curaccount;
    private LayoutInflater inflater;

    public MusicAdapter(Context context, int textViewResourceId, List<MusicInfo> objects , String account){
        super(context,textViewResourceId,objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        collectionDBManager = new CollectionDBManager(getContext());
        resourceId = textViewResourceId;
        curaccount = account;
    }

    @Override
    public View getView(final int position, View converView, ViewGroup parent){
        MusicInfo musicInfo = getItem(position);
        final ViewHolder holder;
        if(converView == null){
            converView = inflater.inflate(resourceId,parent,false);
            holder = new ViewHolder();
            holder.songname = (TextView) converView.findViewById(R.id.song_name);
            holder.singername = (TextView) converView.findViewById(R.id.singer_name);
            holder.like = (ImageView) converView.findViewById(R.id.like);
            converView.setTag(holder);
        }else {
            holder = (ViewHolder) converView.getTag();
        }

        holder.songname.setText(musicInfo.getSongName());
        holder.singername.setText(musicInfo.getSingerName());
//        holder.like.setImageResource(R.drawable.like);

        if(musicInfo.getFilePath() == null){
            holder.like.setVisibility(View.INVISIBLE);
        }else {
            if(collectionDBManager.hadCollection(curaccount,musicInfo.getMd5())){
                holder.like.setImageResource(R.drawable.hadliked);
            }else {
                holder.like.setImageResource(R.drawable.like);
            }
        }

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(collectionDBManager.hadCollection(curaccount,getItem(position).getMd5())){
                    collectionDBManager.delete(curaccount,getItem(position).getMd5());
                    holder.like.setImageResource(R.drawable.like);
                }else {
                    collectionDBManager.add(curaccount,getItem(position).getMd5());
                    holder.like.setImageResource(R.drawable.hadliked);
                }

            }
        });
        return converView;
    }


    static class ViewHolder {
        TextView songname;
        TextView singername;
        ImageView like;


    }

}
