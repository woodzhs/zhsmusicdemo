package com.zhs.zhsmusicplayerdemo;

import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 木头 on 2019/2/2.
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
                    songName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                    if(isMessyCode(songName)){
                        songName = "未知";
                    }
                    singerName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                    if(isMessyCode(singerName)){
                        singerName = "未知";
                    }
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


    static boolean isMessyCode(String strName) {
        try {
            Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
            Matcher m = p.matcher(strName);
            String after = m.replaceAll("");
            String temp = after.replaceAll("\\p{P}", "");
            char[] ch = temp.trim().toCharArray();

            int length = (ch != null) ? ch.length : 0;
            for (int i = 0; i < length; i++) {
                char c = ch[i];

                if (!Character.isLetterOrDigit(c)) {
                    String str = "" + ch[i];
                    if (!str.matches("[\u4e00-\u9fa5]+")) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
//    static String ISO2GBK(String rawString) {
//        try {
//            return new String(rawString.getBytes("ISO-8859-1"), "GBK");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "未知";
//    }
}
