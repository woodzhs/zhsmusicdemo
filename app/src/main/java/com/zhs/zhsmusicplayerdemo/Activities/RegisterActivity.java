package com.zhs.zhsmusicplayerdemo.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zhs.zhsmusicplayerdemo.Model.UserDao.User;
import com.zhs.zhsmusicplayerdemo.Model.UserDao.UserDBManager;
import com.zhs.zhsmusicplayerdemo.R;

public class RegisterActivity extends Activity {
    private EditText registerAccount;
    private EditText registerpassword;
    private Button registerBtn;
    private UserDBManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerAccount = (EditText) findViewById(R.id.registeraccount);
        registerpassword = (EditText) findViewById(R.id.registerpassword);
        registerBtn = (Button) findViewById(R.id.register);
        dm = new UserDBManager(this);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = registerAccount.getText().toString();
                String password = registerpassword.getText().toString();
                User user = new User(account,password);
                if(!dm.hadAccount(user)){
                    dm.add(user);
                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);

                }else {
                    Toast.makeText(RegisterActivity.this,"账号已经被注册",Toast.LENGTH_SHORT).show();
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
