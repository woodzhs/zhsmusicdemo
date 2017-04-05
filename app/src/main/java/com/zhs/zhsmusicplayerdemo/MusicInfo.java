package com.zhs.zhsmusicplayerdemo;

import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 木头 on 2017/3/2.
 */
public class MusicInfo {
    String singerName;
    String songName;
    String filePath;

    public String getSingerName() {
        return singerName;
    }

    public String getSongName() {
        return songName;
    }

    public String getFilePath() {
        return filePath;
    }

    public MusicInfo(){

    }
    public MusicInfo(String a,String b){
        this.songName=a;
        this.singerName=b;
        this.filePath=null;

    }
    public static List<MusicInfo> getAllMusicFiles(String path) {

        List<MusicInfo> result = new ArrayList<>();
        File[] allFiles = new File(path).listFiles();
        try
        {
            for (int i = 0; i < allFiles.length; i++) {
                File file = allFiles[i];
                String filepath = file.getAbsolutePath();
                //  Log.e("TAG", "filepath  is " + filepath);
                //            提取音乐歌名歌手名
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(filepath);


                String songName = "";
                String singerName = "";
                try {
                    songName = ISO2GBK(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
                    singerName = ISO2GBK(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                } catch (Exception e) {

                    e.printStackTrace();
                }

                MusicInfo musicInfo = new MusicInfo();
                musicInfo.filePath = filepath;
                musicInfo.songName = songName;
                musicInfo.singerName = singerName;
                result.add(musicInfo);

            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }


        return result;
    }


    static String ISO2GBK(String rawString) {
        try {
            return new String(rawString.getBytes("ISO-8859-1"), "GBK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "未知";
    }
}
