package com.zhs.zhsmusicplayerdemo.Activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhs.zhsmusicplayerdemo.Model.MusicDao.MusicInfo;
import com.zhs.zhsmusicplayerdemo.R;
import com.zhs.zhsmusicplayerdemo.Service.AudioService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 木头 on 2019/2/1.
 */
public class PlayMusicActivity extends Activity {

    private ImageView last;
    private ImageView pause;
    private ImageView next;
    private LinearLayout back;
    private ImageView record;
    private TextView name;
    private TextView song;
    private TextView curTime;
    private TextView endTime;
    private int currentMusicIndex;
//    private String data1;
    private SeekBar seekBar;
    private ObjectAnimator animator;
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
                    playNextMusic();
                }
            });
        }


    };


    public  android.os.Handler mHandler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg){
            switch ((msg.what)){
                case 0:
                    int position = audioService.player.getCurrentPosition();
                    int time = audioService.player.getDuration();
                    curTime.setText(formatTime(position));
                    int max = seekBar.getMax();
                    seekBar.setProgress(position*max/time);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onDestroy(){
        super.onDestroy();
        unbindService(conn);
        audioService.stopSelf();
        record.clearAnimation();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playmusic);
        pause = (ImageView) findViewById(R.id.pause);
        last = (ImageView) findViewById(R.id.last);
        next = (ImageView) findViewById(R.id.next);
        back = (LinearLayout) findViewById(R.id.titlebar);
        record = (ImageView) findViewById(R.id.record);
        name = (TextView) findViewById(R.id.name);
        song = (TextView) findViewById(R.id.song);
        curTime = (TextView) findViewById(R.id.curtime);
        endTime = (TextView) findViewById(R.id.endtime);
        seekBar = (SeekBar) findViewById(R.id.music_bar);
        Intent intent = getIntent();
//        data1 = intent.getStringExtra("extra_data1");
        ret = intent.getExtras().getParcelableArrayList("List");
        currentMusicIndex = intent.getIntExtra("extra_data2", 0);
        name.setText(ret.get(currentMusicIndex).getSingerName());
        song.setText(ret.get(currentMusicIndex).getSongName());
        endTime.setText(this.formatTime(Integer.parseInt(ret.get(currentMusicIndex).getDuration())));
        curTime.setText(this.formatTime(0));
        animator = ObjectAnimator.ofFloat(record, "rotation", 0f, 360.0f);
        animator.setDuration(10000);
        animator.setInterpolator(new LinearInterpolator());//匀速
        animator.setRepeatCount(-1);//设置动画重复次数（-1代表一直转）
        animator.setRepeatMode(ValueAnimator.RESTART);//动画重复模式
        startMusic();
        animator.start();
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
                        if(animator.isPaused()){
                            animator.resume();
                        }else {
                            animator.pause();
                        }
                }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextMusic();
            }
        });

        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playLastMusic();
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

    public void playNextMusic() {
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
        endTime.setText(this.formatTime(Integer.parseInt(ret.get(currentMusicIndex).getDuration())));
//        animator.start();
    }

    public void playLastMusic() {
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
        endTime.setText(this.formatTime(Integer.parseInt(ret.get(currentMusicIndex).getDuration())));
        animator.start();
    }

    public void startMusic() {
        Intent intent = new Intent(this, AudioService.class);
        this.bindService(intent, conn, Context.BIND_AUTO_CREATE);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
                mHandler.postDelayed(this,1000);
            }
        },1000);
//        final int milliseconds=1;
//        new Thread(){
//            @Override
//            public void run(){
//                while(true){
//                    try{
//                        sleep(milliseconds);
//                    }catch (InterruptedException e){
//                        e.printStackTrace();
//                    }
//
//                    mHandler.sendEmptyMessage(0);
//                }
//            }
//        }.start();

    }

    private String formatTime(int length){
        Date date = new Date(length);
        //时间格式化工具
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String totalTime = sdf.format(date);
        return totalTime;
    }


}
