package com.zhs.zhsmusicplayerdemo.Model.CollectionDAO;

public class Collection {
    private String accound;
    private String md5;

    public Collection(){

    }

    public Collection(String accound,String md5){
        this.accound = accound;
        this.md5 = md5;
    }

    public String getAccound() {
        return accound;
    }

    public void setAccound(String accound) {
        this.accound = accound;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
