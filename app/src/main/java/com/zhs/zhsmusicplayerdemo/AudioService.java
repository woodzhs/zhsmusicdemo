package com.zhs.zhsmusicplayerdemo;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by 木头 on 2019/2/9.
 */
public class AudioService extends Service implements MediaPlayer.OnCompletionListener{

    MediaPlayer player;

    static String form = "";

    private final IBinder binder = new AudioBinder();
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return binder;
    }
    /**
     * 当Audio播放完的时候触发该动作
     */
    MediaPlayer.OnCompletionListener mListener;
    public  void setOnCompletionListener(MediaPlayer.OnCompletionListener listener)
    {
        mListener = listener;

    }
    @Override
    public void onCompletion(MediaPlayer player) {
        // TODO Auto-generated method stub
//        stopSelf();//结束了，则结束Service
        mListener.onCompletion(player);
    }

    //在这里我们需要实例化MediaPlayer对象
    public void onCreate(){
        super.onCreate();
        //我们从raw文件夹中获取一个应用自带的mp3文件
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
    }

    public void initMediaPlayer(String path){
        if(!form.equals(path)) {
            try {
                player.stop();
                player.reset();
                player.setDataSource(path);
                player.prepare();

                form=path;


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void playMusic()
    {
            player.start();
    }

    public void pauseMusic(){
        if(player.isPlaying()){
            player.pause();
        }else{
            player.start();
        }
    }

    /**
     * 该方法在SDK2.0才开始有的，替代原来的onStart方法
     */
    public int onStartCommand(Intent intent, int flags, int startId){

        return START_STICKY;
    }

    public void onDestroy(){
        //super.onDestroy();
        if(player.isPlaying()){
            player.stop();
        }
        player.release();
    }

    //为了和Activity交互，我们需要定义一个Binder对象
    class AudioBinder extends Binder{

        //返回Service对象
        AudioService getService(){
            return AudioService.this;
        }
    }

    //后退播放进度
    public void haveFun(){
        if(player.isPlaying() && player.getCurrentPosition()>2500){
            player.seekTo(player.getCurrentPosition()-2500);
        }
    }
}