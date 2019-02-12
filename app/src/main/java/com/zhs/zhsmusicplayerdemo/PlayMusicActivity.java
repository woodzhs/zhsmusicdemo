package com.zhs.zhsmusicplayerdemo;

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
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 木头 on 2019/2/1.
 */
public class PlayMusicActivity extends Activity {

    private Button last;
    private Button pause;
    private Button next;
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
        setContentView(R.layout.play_music);
        pause = (Button) findViewById(R.id.pause);
        last = (Button) findViewById(R.id.last);
        next = (Button) findViewById(R.id.next);
        name = (TextView) findViewById(R.id.name);
        song = (TextView) findViewById(R.id.song);
        seekBar = (SeekBar) findViewById(R.id.music_bar);
        Intent intent = getIntent();
        data1 = intent.getStringExtra("extra_data1");
        currentMusicIndex = intent.getIntExtra("extra_data2", 0);
        ret = MusicInfo.getAllMusicFiles(data1);
        name.setText(ret.get(currentMusicIndex).getSingerName());
        song.setText(ret.get(currentMusicIndex).songName);
        startMusic();
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
                    audioService.initMediaPlayer(ret.get(--currentMusicIndex).filePath);
                    name.setText(ret.get(currentMusicIndex).singerName);
                    song.setText(ret.get(currentMusicIndex).songName);
                    audioService.playMusic();
                } else {
                    currentMusicIndex = ret.size() - 1;
                    audioService.initMediaPlayer(ret.get(currentMusicIndex).filePath);
                    name.setText(ret.get(currentMusicIndex).singerName);
                    song.setText(ret.get(currentMusicIndex).songName);
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
            audioService.initMediaPlayer(ret.get(++currentMusicIndex).filePath);
            name.setText(ret.get(currentMusicIndex).singerName);
            song.setText(ret.get(currentMusicIndex).songName);
            audioService.playMusic();
        } else {
            currentMusicIndex = 0;
            audioService.initMediaPlayer(ret.get(currentMusicIndex).filePath);
            name.setText(ret.get(currentMusicIndex).singerName);
            song.setText(ret.get(currentMusicIndex).songName);
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
