package com.zhs.zhsmusicplayerdemo.Model.CollectionDAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhs.zhsmusicplayerdemo.Model.MusicDao.MusicInfo;
import com.zhs.zhsmusicplayerdemo.Model.MusicPlayDBHelper;

import java.util.ArrayList;
import java.util.List;

public class CollectionDBManager {
    private MusicPlayDBHelper helper;
    private SQLiteDatabase db;

    public CollectionDBManager(Context context){
        helper = new MusicPlayDBHelper(context);
        db = helper.getWritableDatabase();
    }

    public void add(String accound,String md5){
        db.beginTransaction();
        try{

            db.execSQL("INSERT INTO collection VALUES(null,?,?)",
                        new Object[]{accound,md5});
            db.setTransactionSuccessful();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }

    public List<MusicInfo> findCollectionMusic(String account){
        ArrayList<MusicInfo> arrayList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM music where md5 in (SELECT md5 FROM collection where account = ? )",new String[]{account});
        while (c.moveToNext()){
            MusicInfo musicInfo = new MusicInfo();
            musicInfo.setMd5(c.getString(c.getColumnIndex("md5")));
            musicInfo.setSongName(c.getString(c.getColumnIndex("songname")));
            musicInfo.setSingerName(c.getString(c.getColumnIndex("singername")));
            musicInfo.setFilePath(c.getString(c.getColumnIndex("filepath")));
            musicInfo.setDuration(c.getString(c.getColumnIndex("duration")));
            arrayList.add(musicInfo);
        }
        c.close();
        return arrayList;
    }

    public Boolean hadCollection(String account,String md5){
        Cursor c = db.rawQuery("SELECT * FROM collection where account = ? and md5 = ?",new String[]{account,md5});
        if(c.getCount() == 0){
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public void delete(String account,String md5){
        db.execSQL("delete from collection where account= ? and md5 = ?",new Object[]{account,md5});
    }
}
