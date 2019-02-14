package com.zhs.zhsmusicplayerdemo.Model.UserDao;

public class User {
    private int uid;
    private String account;
    private String nickName;
    private String password;

    public User(){}

    public User(String account,String password){
        this.account = account;
        this.password = password;
        this.nickName = "";
    }

    public String getAccount(){
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickName(){
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
