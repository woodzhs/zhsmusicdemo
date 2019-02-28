package com.zhs.zhsmusicplayerdemo.Model.MusicDao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhs.zhsmusicplayerdemo.Model.MusicPlayDBHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicInfoDBManager {
    private MusicPlayDBHelper helper;
    private SQLiteDatabase db;

    public MusicInfoDBManager(Context context){
        helper = new MusicPlayDBHelper(context);
        db = helper.getWritableDatabase();
    }

    public void add(List<MusicInfo> musicInfos){
        for(MusicInfo musicInfo : musicInfos){
            db.beginTransaction();
            try{
                if(!hadMusic(musicInfo)){
                    db.execSQL("INSERT INTO music VALUES(?,?,?,?,?,?,?)",
                            new Object[]{musicInfo.getMd5(),musicInfo.getSongName(),musicInfo.getSingerName(),musicInfo.getFilePath(),musicInfo.getDuration(),musicInfo.getIsLike(),musicInfo.getLocal()});
                    db.setTransactionSuccessful();
                }
                else {
                    continue;
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                db.endTransaction();
            }
        }

    }

    public void clearDB(List<MusicInfo> musicInfos){
        Cursor c = db.rawQuery("SELECT * FROM music ", null );
        while (c.moveToNext()){
            String filePath = c.getString(c.getColumnIndex("filepath"));
            File file = new File(filePath);
            if(!file.exists()){
                db.execSQL("delete from collection where md5= ?",new Object[]{c.getString(c.getColumnIndex("md5"))});
                db.execSQL("delete from music where md5= ?",new Object[]{c.getString(c.getColumnIndex("md5"))});
            }
        }
    }

    public List<MusicInfo> find(String str){
        ArrayList<MusicInfo> musicInfos = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM music where songname like '%" + str + "%' OR singername like '%" + str + "%'",null);
        while (c.moveToNext()){
            MusicInfo musicInfo = new MusicInfo();
            musicInfo.setMd5(c.getString(c.getColumnIndex("md5")));
            musicInfo.setSongName(c.getString(c.getColumnIndex("songname")));
            musicInfo.setSingerName(c.getString(c.getColumnIndex("singername")));
            musicInfo.setFilePath(c.getString(c.getColumnIndex("filepath")));
            musicInfo.setDuration(c.getString(c.getColumnIndex("duration")));
            musicInfo.setLocal(c.getInt(c.getColumnIndex("islocal")));
            musicInfos.add(musicInfo);
        }
        c.close();
        return musicInfos;
    }

    public List<MusicInfo> findAll(){
        ArrayList<MusicInfo> musicInfos = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM music",null);
        while (c.moveToNext()){
            MusicInfo musicInfo = new MusicInfo();
            musicInfo.setMd5(c.getString(c.getColumnIndex("md5")));
            musicInfo.setSongName(c.getString(c.getColumnIndex("songname")));
            musicInfo.setSingerName(c.getString(c.getColumnIndex("singername")));
            musicInfo.setFilePath(c.getString(c.getColumnIndex("filepath")));
            musicInfo.setDuration(c.getString(c.getColumnIndex("duration")));
            musicInfo.setIsLike(c.getInt(c.getColumnIndex("islike")));
            musicInfo.setLocal(c.getInt(c.getColumnIndex("islocal")));
            musicInfos.add(musicInfo);
        }
        c.close();
        return musicInfos;
    }

    public List<MusicInfo> getCollection(){
        ArrayList<MusicInfo> musicInfos = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM music where islike = 1",null);
        while (c.moveToNext()){
            MusicInfo musicInfo = new MusicInfo();
            musicInfo.setMd5(c.getString(c.getColumnIndex("md5")));
            musicInfo.setSongName(c.getString(c.getColumnIndex("songname")));
            musicInfo.setSingerName(c.getString(c.getColumnIndex("singername")));
            musicInfo.setFilePath(c.getString(c.getColumnIndex("filepath")));
            musicInfo.setDuration(c.getString(c.getColumnIndex("duration")));
            musicInfo.setIsLike(c.getInt(c.getColumnIndex("islike")));
            musicInfo.setLocal(c.getInt(c.getColumnIndex("islocal")));
            musicInfos.add(musicInfo);
        }
        c.close();
        return musicInfos;
    }

    public void updateLike(MusicInfo musicInfo){
        ContentValues cv = new ContentValues();
        cv.put("islike",musicInfo.getIsLike());
        db.update("music",cv,"md5=?",new String[]{musicInfo.getMd5()});
    }



    public Boolean hadMusic(MusicInfo musicInfo){
        Cursor c = db.rawQuery("SELECT * FROM music where md5 = ?", new String[]{ musicInfo.getMd5() });
        if(c.getCount() == 0){
            return false;
        }
        return true;
    }

}
