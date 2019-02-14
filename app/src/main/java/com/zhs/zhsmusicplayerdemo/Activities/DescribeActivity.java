package com.zhs.zhsmusicplayerdemo.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhs.zhsmusicplayerdemo.R;

public class DescribeActivity extends Activity {
    private TextView describeTitle;
    private TextView describeContent;
    private LinearLayout describeTitlebar;
    private int type;
    private String aboutContent = "本程序是一个简单的音乐播放器软件，由华南农业大学数学与信息学院（软件学院）郑海松制作，目的是作为毕业设计，使用了Android，Jsoup，LeakCanary等技术，开发工具使用了Android Studio，Git等。";
    private String useHelpContent = "本程序是一个简单的本地音乐播放器，使用要先将mp3文件放在手机本地内存根目录下的Music文件夹下。此外，百度音乐流行音乐前20首，由于没有播放链接因此只能拉取到文字信息，无法在线播放。";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe);
        describeTitle = (TextView) findViewById(R.id.describetitle);
        describeContent = (TextView) findViewById(R.id.describecontent);
        describeTitlebar = (LinearLayout) findViewById(R.id.describetitlebar);
        Intent intent = getIntent();
        type = intent.getIntExtra("type",0);
        if(type == 1){
            describeTitle.setText("关于");
            describeContent.setText(aboutContent);
        }else if(type == 2){
            describeTitle.setText("使用说明");
            describeContent.setText(useHelpContent);

        }

        describeTitlebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
