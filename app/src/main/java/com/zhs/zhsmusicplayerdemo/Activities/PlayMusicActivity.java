package com.zhs.zhsmusicplayerdemo.Activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhs.zhsmusicplayerdemo.Model.MusicDao.MusicInfo;
import com.zhs.zhsmusicplayerdemo.R;
import com.zhs.zhsmusicplayerdemo.Service.AudioService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;


public class PlayMusicActivity extends Activity {
    private ImageView last;
    private ImageView pause;
    private ImageView next;
    private ImageView record;
    private LinearLayout back;
    private TextView name;
    private TextView song;
    private TextView curTime;
    private TextView endTime;
    private SeekBar seekBar;
    private ObjectAnimator animator;
    public List<MusicInfo> ret = new ArrayList<>();
    private int currentMusicIndex;

    private NotificationManager nm;
    private RemoteViews contentViews;
    private Notification notify;
    private static final int NOTIFICATION_ID = 123;
    private static final String CHANNEL_ID = "MUSICNOTIFICATIONID";
    private static final String CHANNEL_NAME = "MUSICNOTIFICATION";
    private boolean showNotification;

    public AudioService audioService;

    private BroadcastReceiver playMusicReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "action = " + action);
            if (action.equals("ACTION_NEXT_SONG")) {
                playNextMusic();
            } else if (action.equals("ACTION_PRE_SONG")) {
                playLastMusic();
            } else if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {//在播放器中点击home键时显示通知栏图标
                showNotification();
            } else if (action.equals("ACTION_EXIT")) {//通知栏点击退出图标
                finish();
            } else if (action.equals("ACTION_PLAY_AND_PAUSE")) {
                pauseMusic();
            }
        }

    };

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

        initNotification();

        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_NEXT_SONG");
        filter.addAction("ACTION_PRE_SONG");
        filter.addAction("ACTION_EXIT");
        filter.addAction("ACTION_PLAY_AND_PAUSE");
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //注册广播接收
        registerReceiver(playMusicReceiver,filter);

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
                pauseMusic();
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

    private void initNotification(){

        if(nm == null){
            nm = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        }

        Intent mainIntent = new Intent(PlayMusicActivity.this, PlayMusicActivity.class);
        PendingIntent pi = PendingIntent.getActivity(PlayMusicActivity.this, 0, mainIntent, 0);
        if(Build.VERSION.SDK_INT >= 26){
            createNotificationChannel(nm);
            notify = new Notification.Builder(getApplicationContext(),CHANNEL_ID).build();
        }else {
            notify = new Notification();
        }
        notify.when = System.currentTimeMillis();
        notify.icon = R.drawable.musictap;
        notify.contentIntent = pi;//点击通知跳转到MainActivity
        notify.flags = Notification.FLAG_AUTO_CANCEL;
        contentViews = new RemoteViews(getPackageName(), R.layout.notification);
        contentViews.setOnClickPendingIntent(R.id.playtag, pi);
        contentViews.setOnClickPendingIntent(R.id.currentmusic, pi);
        //上一首图标添加点击监听
        Intent previousButtonIntent = new Intent("ACTION_PRE_SONG");
        PendingIntent pendPreviousButtonIntent = PendingIntent.getBroadcast(this, 0, previousButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.pre, pendPreviousButtonIntent);
        //播放/暂停添加点击监听
        Intent playPauseButtonIntent = new Intent("ACTION_PLAY_AND_PAUSE");
        PendingIntent playPausePendingIntent = PendingIntent.getBroadcast(this, 0, playPauseButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.playandpause, playPausePendingIntent);
        //下一首图标添加监听
        Intent nextButtonIntent = new Intent("ACTION_NEXT_SONG");
        PendingIntent pendNextButtonIntent = PendingIntent.getBroadcast(this, 0, nextButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.next, pendNextButtonIntent);
        //退出监听
//        Intent exitButton = new Intent("ACTION_EXIT");
//        PendingIntent pendingExitButtonIntent = PendingIntent.getBroadcast(this,0,exitButton,0);
//        contentViews.setOnClickPendingIntent(R.id.close,pendingExitButtonIntent);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel(NotificationManager notificationManager) {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
    }

    private void showNotification() {
        showNotification = true;

        contentViews.setTextViewText(R.id.currentmusic, ret.get(currentMusicIndex).getSongName() + "—" + ret.get(currentMusicIndex).getSingerName());

        notify.contentView = contentViews;
        nm.notify(NOTIFICATION_ID, notify);//调用notify方法后即可显示通知
    }



    @Override
    protected void onStart() {
        //回到音乐播放器时关闭通知
        if (showNotification) {
            nm.cancel(NOTIFICATION_ID);
            showNotification = false;
        }

        super.onStart();
    }


    public void pauseMusic(){
        if (audioService != null)
            audioService.pauseMusic();
        if(animator.isPaused()){
            animator.resume();
        }else {
            animator.pause();
        }
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
        contentViews.setTextViewText(R.id.currentmusic, ret.get(currentMusicIndex).getSongName() + "—" + ret.get(currentMusicIndex).getSingerName());
        nm.notify(NOTIFICATION_ID, notify);
        animator.start();
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
        contentViews.setTextViewText(R.id.currentmusic, ret.get(currentMusicIndex).getSongName() + "—" + ret.get(currentMusicIndex).getSingerName());
        nm.notify(NOTIFICATION_ID, notify);
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
