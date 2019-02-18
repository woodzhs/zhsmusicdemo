package com.zhs.zhsmusicplayerdemo.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MusicPlayDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "musicplay.db";
    private static final int DATABASE_VERSION = 1;
    public MusicPlayDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS user(uid INTEGER PRIMARY KEY AUTOINCREMENT," +
                " account VARCHAR,nickname VARCHAR,password VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS music(md5 VARCHAR PRIMARY KEY ," +
                " songname VARCHAR,singername VARCHAR,filepath VARCHAR,duration VARCHAR)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        db.execSQL("ALTER TABLE user COLUMN other");
    }
}
