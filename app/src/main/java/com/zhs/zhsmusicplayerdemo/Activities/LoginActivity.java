package com.zhs.zhsmusicplayerdemo.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
        checkPermission();
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
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("account",account);
                    intent.putExtra("password",password);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this,"登陆成功，请稍等",Toast.LENGTH_SHORT).show();
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

    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        } else {
            Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();
        }
    }
}
