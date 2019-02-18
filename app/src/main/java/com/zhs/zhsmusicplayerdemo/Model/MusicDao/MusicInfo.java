package com.zhs.zhsmusicplayerdemo.Model.MusicDao;

import android.media.MediaMetadataRetriever;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MusicInfo implements Parcelable {
    String md5;
    String singerName;
    String songName;
    String filePath;
    String duration;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public MusicInfo(){

    }
    public MusicInfo(String a,String b){

        this.songName = a;
        this.singerName = b;
        this.filePath = null;
        this.duration = null;
        this.md5 = null;

    }

    public static List<MusicInfo> getAllMusicFiles(String path) {

        List<MusicInfo> result = new ArrayList<>();
        File[] allFiles = new File(path).listFiles();
        try{
            for (int i = 0; i < allFiles.length; i++) {
                File file = allFiles[i];
                String filepath = file.getAbsolutePath();
                //  Log.e("TAG", "filepath  is " + filepath);
                //            提取音乐歌名歌手名
                try {
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(filepath);

                    String songName = "";
                    String singerName = "";
                    String duration = "";
                    try {
                        songName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                        if (isMessyCode(songName)) {
                            songName = "未知";
                        }
                        singerName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                        if (isMessyCode(singerName)) {
                            singerName = "未知";
                        }
                        duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        if(isMessyCode(duration)){
                            duration = null;
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.filePath = filepath;
                    musicInfo.songName = songName;
                    musicInfo.singerName = singerName;
                    musicInfo.duration = duration;
                    musicInfo.md5 = getFileMD5(filepath);
                    result.add(musicInfo);
                } catch (Exception e) {

                    e.printStackTrace();
                    continue;
                }

            }
        } catch (Exception e) {
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

    public static String getFileMD5(String filePath) {
        File file = new File(filePath);
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel,int flags){
        parcel.writeString(md5);
        parcel.writeString(singerName);
        parcel.writeString(songName);
        parcel.writeString(filePath);
        parcel.writeString(duration);

    }

    public static final Parcelable.Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {
        @Override
        public MusicInfo createFromParcel(Parcel source) {
            MusicInfo app = new MusicInfo();
            app.md5 = source.readString();
            app.singerName = source.readString();
            app.songName = source.readString();
            app.filePath = source.readString();
            app.duration = source.readString();
            return app;
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };

}
