package com.zhs.zhsmusicplayerdemo.Activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zhs.zhsmusicplayerdemo.Model.MusicDao.MusicInfo;
import com.zhs.zhsmusicplayerdemo.Model.MusicDao.MusicInfoDBManager;
import com.zhs.zhsmusicplayerdemo.R;
import com.zhs.zhsmusicplayerdemo.Service.AudioService;

import java.util.ArrayList;
import java.util.List;

public class CollectionMusicActivity extends Activity {
    private ListView collectionListView;
    private List<MusicInfo> ret = new ArrayList<>();
    private MusicInfoDBManager musicInfoDBManager;
    private LinearLayout back;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        musicInfoDBManager = new MusicInfoDBManager(this);
        startMusic();
        collectionListView = (ListView) findViewById(R.id.collectionlistview);
        back = (LinearLayout) findViewById(R.id.collectiontitlebar);
        ret = musicInfoDBManager.getCollection();
        MusicAdapter adapter=new MusicAdapter(this,R.layout.music_item,ret);
        collectionListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        collectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(audioService!=null) {
                    audioService.initMediaPlayer(ret.get(position).getFilePath());
                    audioService.playMusic();
                }
                Bundle data1 = new Bundle();
                data1.putParcelableArrayList("List",(ArrayList<? extends Parcelable>) ret);
                int data2=position;
                Intent intent=new Intent(CollectionMusicActivity.this,PlayMusicActivity.class);
                intent.putExtras(data1);
                intent.putExtra("extra_data2",data2);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent stopIntent=new Intent(CollectionMusicActivity.this,AudioService.class);
//                unbindService(conn);
//                stopService(stopIntent);
                CollectionMusicActivity.this.finish();
            }
        });


    }

    @Override
    protected void onDestroy(){
        Intent stopIntent=new Intent(CollectionMusicActivity.this,AudioService.class);
        audioService.stopSelf();
        this.unbindService(conn);
        this.stopService(stopIntent);
        super.onDestroy();
    }


    public void startMusic(){
        Intent intent = new Intent(this,AudioService.class);
        this.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }
}
