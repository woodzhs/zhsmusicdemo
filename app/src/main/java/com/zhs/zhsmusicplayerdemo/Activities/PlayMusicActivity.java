package com.zhs.zhsmusicplayerdemo.Activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhs.zhsmusicplayerdemo.Model.MusicInfo;
import com.zhs.zhsmusicplayerdemo.R;
import com.zhs.zhsmusicplayerdemo.Service.AudioService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 木头 on 2019/2/1.
 */
public class PlayMusicActivity extends Activity {

    private ImageView last;
    private ImageView pause;
    private ImageView next;
    private ImageView back;
    private TextView name;
    private TextView song;
    private int currentMusicIndex;
    private String data1;
    private SeekBar seekBar;
    public List<MusicInfo> ret = new ArrayList<>();




    public AudioService audioService;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

            audioService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            audioService = ((AudioService.AudioBinder) binder).getService();
            audioService.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
//                    currentMusicIndex++;
//                    audioService.initMediaPlayer(ret.get(currentMusicIndex).filePath);
//                    audioService.playMusic();
                    playeNextMusic();
                }
            });
        }


    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public android.os.Handler mHandler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg){
            switch ((msg.what)){
                case 0:
                    int position = audioService.player.getCurrentPosition();
                    int time = audioService.player.getDuration();
                    int max = seekBar.getMax();
                    seekBar.setProgress(position*max/time);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playmusic);
        pause = (ImageView) findViewById(R.id.pause);
        last = (ImageView) findViewById(R.id.last);
        next = (ImageView) findViewById(R.id.next);
        back = (ImageView) findViewById(R.id.title_back);
        name = (TextView) findViewById(R.id.name);
        song = (TextView) findViewById(R.id.song);
        seekBar = (SeekBar) findViewById(R.id.music_bar);
        Intent intent = getIntent();
        data1 = intent.getStringExtra("extra_data1");
        currentMusicIndex = intent.getIntExtra("extra_data2", 0);
        ret = MusicInfo.getAllMusicFiles(data1);
        name.setText(ret.get(currentMusicIndex).getSingerName());
        song.setText(ret.get(currentMusicIndex).getSongName());
        startMusic();
        back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                }
        );
        pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (audioService != null)
                        audioService.pauseMusic();
                }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playeNextMusic();
            }
        });
        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioService != null && currentMusicIndex != 0) {
                    audioService.initMediaPlayer(ret.get(--currentMusicIndex).getFilePath());
                    name.setText(ret.get(currentMusicIndex).getSingerName());
                    song.setText(ret.get(currentMusicIndex).getSongName());
                    audioService.playMusic();
                } else {
                    currentMusicIndex = ret.size() - 1;
                    audioService.initMediaPlayer(ret.get(currentMusicIndex).getFilePath());
                    name.setText(ret.get(currentMusicIndex).getSingerName());
                    song.setText(ret.get(currentMusicIndex).getSongName());
                    audioService.playMusic();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int dest = seekBar.getProgress();
                int time = audioService.player.getDuration();
                int max = seekBar.getMax();

                audioService.player.seekTo(time * dest / max);
            }
        });



    }

    public void playeNextMusic() {
        if (audioService != null && currentMusicIndex != ret.size() - 1) {
            audioService.initMediaPlayer(ret.get(++currentMusicIndex).getFilePath());
            name.setText(ret.get(currentMusicIndex).getSingerName());
            song.setText(ret.get(currentMusicIndex).getSongName());
            audioService.playMusic();
        } else {
            currentMusicIndex = 0;
            audioService.initMediaPlayer(ret.get(currentMusicIndex).getFilePath());
            name.setText(ret.get(currentMusicIndex).getSingerName());
            song.setText(ret.get(currentMusicIndex).getSongName());
            audioService.playMusic();
        }
    }

    public void startMusic() {
        Intent intent = new Intent(this, AudioService.class);
        this.bindService(intent, conn, Context.BIND_AUTO_CREATE);

        final int milliseconds=1;
        new Thread(){
            @Override
            public void run(){
                while(true){
                    try{
                        sleep(milliseconds);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                    mHandler.sendEmptyMessage(0);
                }
            }
        }.start();

    }

}
