package com.zhs.zhsmusicplayerdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 木头 on 2016/11/7.
 */
public class SearchFragment extends Fragment {
    private ListView listView;
    private Button search;
    private EditText input;
    public List<MusicInfo> ret1=new ArrayList<>();
    public List<MusicInfo> ret2=new ArrayList<>();
    private static String path="/sdcard/Musicdemo";
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View content = inflater.inflate(R.layout.search, container,false);
        startMusic();
        search=(Button)content.findViewById(R.id.search);
        input=(EditText)content.findViewById(R.id.input_message);
        listView=(ListView)content.findViewById(R.id.listView1);
        ret1.clear();
        ret1=MusicInfo.getAllMusicFiles(path);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ret2.clear();
                String inputText=input.getText().toString();
               // Log.d("ret2", inputText);
                for(int i=0;i<ret1.size();i++){
                    if(inputText.equals(ret1.get(i).songName)||inputText.equals(ret1.get(i).singerName)){
                        ret2.add(ret1.get(i));
                        Log.d("ret2",ret1.get(i).songName);

                    }
                }
                MusicAdapter adapter=new MusicAdapter(getActivity(),R.layout.music_item,ret2);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(audioService!=null) {
                    audioService.initMediaPlayer(ret2.get(position).filePath);
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
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    public void startMusic(){
        Intent intent = new Intent(getActivity(),AudioService.class);

        getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }


}
