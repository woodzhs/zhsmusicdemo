package com.zhs.zhsmusicplayerdemo.Fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.zhs.zhsmusicplayerdemo.Activities.MusicAdapter;
import com.zhs.zhsmusicplayerdemo.Activities.PlayMusicActivity;
import com.zhs.zhsmusicplayerdemo.Model.MusicDao.MusicInfo;
import com.zhs.zhsmusicplayerdemo.R;
import com.zhs.zhsmusicplayerdemo.Service.AudioService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 木头 on 2019/2/7.
 */
public class SearchFragment extends Fragment {
    private ListView listView;
    private ImageButton search;
    private EditText input;
    public List<MusicInfo> ret1 = new ArrayList<>();
    public List<MusicInfo> ret2 = new ArrayList<>();
    private static String path = Environment.getExternalStorageDirectory().getPath() + "/Music/";;
    public AudioService audioService;

    private ServiceConnection conn= new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

            audioService = null;
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            audioService = ((AudioService.AudioBinder)binder).getService();
        }


    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View content = inflater.inflate(R.layout.fragment_search, container,false);
        startMusic();
        search = (ImageButton) content.findViewById(R.id.search);
        input = (EditText)content.findViewById(R.id.input_message);
        listView = (ListView)content.findViewById(R.id.listView1);
        ret1.clear();
        ret1 = MusicInfo.getAllMusicFiles(path);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ret2.clear();
                String inputText=input.getText().toString();
               // Log.d("ret2", inputText);
                for(int i = 0; i < ret1.size();i++){
                    if(inputText.equals(ret1.get(i).getSongName())||inputText.equals(ret1.get(i).getSingerName())){
                        ret2.add(ret1.get(i));
                        Log.d("ret2",ret1.get(i).getSongName());

                    }
                }
                MusicAdapter adapter = new MusicAdapter(getActivity(),R.layout.music_item,ret2);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(audioService != null) {
                    audioService.initMediaPlayer(ret2.get(position).getFilePath());
                    audioService.playMusic();
                }
                String data1 = path;
                int data2 = position;
                Intent intent = new Intent(getActivity(),PlayMusicActivity.class);
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
