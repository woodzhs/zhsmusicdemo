package com.zhs.zhsmusicplayerdemo.Fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhs.zhsmusicplayerdemo.Activities.MusicAdapter;
import com.zhs.zhsmusicplayerdemo.Activities.PlayMusicActivity;
import com.zhs.zhsmusicplayerdemo.Model.MusicInfo;
import com.zhs.zhsmusicplayerdemo.R;
import com.zhs.zhsmusicplayerdemo.Service.AudioService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 木头 on 2019/2/7.
 */
public class MymusicFragment extends Fragment {

    private ListView listView;
    public List<MusicInfo> ret=new ArrayList<>();
    private static String path = Environment.getExternalStorageDirectory().getPath() + "/Music/";

    public AudioService audioService;

    private ServiceConnection conn= new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

            audioService=null;
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            audioService=((AudioService.AudioBinder)binder).getService();
        }


    };


    private Intent intent;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View content = inflater.inflate(R.layout.fragment_mymusic, container,false);
        //listView.invalidate();
        startMusic();
        listView = (ListView)content.findViewById(R.id.listView);
        ret.clear();
        ret=MusicInfo.getAllMusicFiles(path);
        MusicAdapter adapter=new MusicAdapter(this.getActivity(),R.layout.music_item,ret);

        ListView listView=(ListView)content.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(audioService!=null) {
                    audioService.initMediaPlayer(ret.get(position).getFilePath());
                    audioService.playMusic();
                }
                String data1=path;
                int data2=position;
                Intent intent=new Intent(getActivity(),PlayMusicActivity.class);
                intent.putExtra("extra_data1",data1);
                intent.putExtra("extra_data2",data2);
                startActivity(intent);
            }
        });
        return content;
    }


    public void stopMusic(){
        Intent stopIntent=new Intent(getActivity(),AudioService.class);
        getActivity().unbindService(conn);
        getActivity().stopService(stopIntent);

    }

    public void startMusic(){
        Intent intent = new Intent(getActivity(),AudioService.class);

        getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }



}