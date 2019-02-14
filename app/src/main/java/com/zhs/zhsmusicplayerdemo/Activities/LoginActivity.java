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

public class LoginActivity extends Activity {
    private EditText loginAccount;
    private EditText loginpassword;
    private Button loginBtn;
    private UserDBManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginAccount = (EditText) findViewById(R.id.loginaccount);
        loginpassword = (EditText) findViewById(R.id.loginpassword);
        loginBtn = (Button) findViewById(R.id.login);
        dm = new UserDBManager(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = loginAccount.getText().toString();
                String password = loginpassword.getText().toString();
                User user = new User(account,password);
                if(dm.hadUser(user)){
                    Toast.makeText(LoginActivity.this,"登陆成功，请稍等",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("account",account);
                    intent.putExtra("password",password);
                    startActivity(intent);
                }else {
                    Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                }
            }

        });
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        dm.closeDB();
    }
}
