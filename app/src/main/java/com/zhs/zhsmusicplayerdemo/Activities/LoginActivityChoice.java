package com.zhs.zhsmusicplayerdemo.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zhs.zhsmusicplayerdemo.R;

public class LoginActivityChoice extends Activity {
    private Button login;
    private Button regist;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginchoice);
        login = (Button) findViewById(R.id.choiselogin);
        regist = (Button) findViewById(R.id.choiseregist);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivityChoice.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivityChoice.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
