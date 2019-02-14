package com.zhs.zhsmusicplayerdemo.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhs.zhsmusicplayerdemo.R;

public class RegistActivity extends Activity {
    private EditText registAccount;
    private EditText registpassword;
    private Button registBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        registAccount = (EditText) findViewById(R.id.registaccount);
        registpassword = (EditText) findViewById(R.id.registpassword);
        registBtn = (Button) findViewById(R.id.regist);

        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
