package com.zhs.zhsmusicplayerdemo.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhs.zhsmusicplayerdemo.Model.UserDao.User;
import com.zhs.zhsmusicplayerdemo.Model.UserDao.UserDBManager;
import com.zhs.zhsmusicplayerdemo.R;

public class ChangePasswordActivity extends Activity {
    private EditText changePasswrodAccount;
    private EditText oldPassword;
    private EditText newPassword;
    private Button change;
    private UserDBManager dm;
    private String curaccount;
    private String curpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        changePasswrodAccount = (EditText) findViewById(R.id.changeaccountcontent);
        oldPassword = (EditText) findViewById(R.id.oldpassword);
        newPassword = (EditText) findViewById(R.id.newpassword);
        change = (Button) findViewById(R.id.change);
        dm = new UserDBManager(this);
        Intent intent = getIntent();
        curaccount = intent.getStringExtra("account");
        curpassword = intent.getStringExtra("password");

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = changePasswrodAccount.getText().toString();
                String old = oldPassword.getText().toString();
                String newword = newPassword.getText().toString();
                if(dm.hadUser(new User(account,old))){
                    dm.updatePassword(new User(account,newword));
                    Toast.makeText(ChangePasswordActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(ChangePasswordActivity.this,"该用户不存在",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
