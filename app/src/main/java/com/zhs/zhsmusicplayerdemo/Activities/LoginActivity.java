package com.zhs.zhsmusicplayerdemo.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhs.zhsmusicplayerdemo.R;

public class LoginActivity extends Activity {
    private EditText loginAccount;
    private EditText loginpassword;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginAccount = (EditText) findViewById(R.id.loginaccount);
        loginpassword = (EditText) findViewById(R.id.loginpassword);
        loginBtn = (Button) findViewById(R.id.login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = loginAccount.getText().toString();
                String password = loginpassword.getText().toString();
                if(account.equals("1234") && password.equals("1234")){
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("account",account);
                    startActivity(intent);
                }else {
                    Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}
