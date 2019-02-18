package com.zhs.zhsmusicplayerdemo.Model.UserDao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhs.zhsmusicplayerdemo.Model.MusicPlayDBHelper;

import java.util.ArrayList;

public class UserDBManager {
    private MusicPlayDBHelper helper;
    private SQLiteDatabase db;

    public UserDBManager(Context context){
        helper = new MusicPlayDBHelper(context);
        db = helper.getWritableDatabase();
    }

    public void add(User user){
        db.beginTransaction();
        try{
            db.execSQL("INSERT INTO user VALUES(null,?,?,?)",
                                             new Object[]{user.getAccount(),user.getNickName(),user.getPassword()});
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }

    public void updatePassword(User user){
        ContentValues cv = new ContentValues();
        cv.put("password",user.getPassword());
        db.update("user",cv,"account=?",new String[]{user.getAccount()});
    }

    public Boolean hadUser(User user){
        ArrayList<User> users = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM user", null);
        while (c.moveToNext()){
            String account = c.getString(c.getColumnIndex("account"));
            String password = c.getString(c.getColumnIndex("password"));
            if(user.getAccount().equals(account) && user.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }

    public Boolean hadAccount(User user){
        Cursor c = db.rawQuery("SELECT * FROM user", null);
        while (c.moveToNext()){
            String account = c.getString(c.getColumnIndex("account"));
            if(user.getAccount().equals(account)){
                return true;
            }
        }
        return false;
    }

    public void closeDB(){
        db.close();
    }
}
