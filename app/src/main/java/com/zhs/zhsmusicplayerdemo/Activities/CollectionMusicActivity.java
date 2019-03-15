package com.zhs.zhsmusicplayerdemo.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.zhs.zhsmusicplayerdemo.Model.CollectionDAO.CollectionDBManager;
import com.zhs.zhsmusicplayerdemo.Model.MusicDao.MusicInfo;
import com.zhs.zhsmusicplayerdemo.R;
import java.util.ArrayList;
import java.util.List;

public class CollectionMusicActivity extends Activity {
    private ListView collectionListView;
    private List<MusicInfo> ret = new ArrayList<>();
    private CollectionDBManager collectionDBManager;
    private LinearLayout back;
    private String curAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        collectionListView = (ListView) findViewById(R.id.collectionlistview);
        back = (LinearLayout) findViewById(R.id.collectiontitlebar);

        Intent intent = getIntent();
        curAccount = intent.getStringExtra("account");
        collectionDBManager = new CollectionDBManager(this);
        ret = collectionDBManager.findCollectionMusic(curAccount);
        MusicAdapter adapter=new MusicAdapter(this,R.layout.music_item,ret,curAccount);
        collectionListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        collectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle data1 = new Bundle();
                data1.putParcelableArrayList("List",(ArrayList<? extends Parcelable>) ret);
                int data2=position;
                Intent intent=new Intent(CollectionMusicActivity.this,PlayMusicActivity.class);
                intent.putExtras(data1);
                intent.putExtra("extra_data2",data2);
                intent.putExtra("isonline",0);
                intent.putExtra("account",curAccount);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionMusicActivity.this.finish();
            }
        });


    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }


}
