package com.zhs.zhsmusicplayerdemo.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.zhs.zhsmusicplayerdemo.Activities.MusicAdapter;
import com.zhs.zhsmusicplayerdemo.Activities.PlayMusicActivity;
import com.zhs.zhsmusicplayerdemo.Model.MusicDao.MusicInfo;
import com.zhs.zhsmusicplayerdemo.Model.MusicDao.MusicInfoDBManager;
import com.zhs.zhsmusicplayerdemo.R;
import java.util.ArrayList;
import java.util.List;


@SuppressLint("ValidFragment")
public class MymusicFragment extends Fragment {

    private ListView listView;
    public List<MusicInfo> ret=new ArrayList<>();
    public MusicInfoDBManager dm;
    public String curAccount;
    private static String path = Environment.getExternalStorageDirectory().getPath() + "/Music/";


    @SuppressLint("ValidFragment")
    public MymusicFragment(String account){
        super();
        setCurAccount(account);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View content = inflater.inflate(R.layout.fragment_mymusic, container,false);
        dm = new MusicInfoDBManager(getContext());
        listView = (ListView)content.findViewById(R.id.listView);
        ret.clear();
        ret=MusicInfo.getAllMusicFiles(path);
        dm.add(ret);
        dm.clearDB(ret);
        ret.clear();
        ret = dm.findAll();
        MusicAdapter adapter=new MusicAdapter(this.getActivity(),R.layout.music_item,ret,curAccount);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle data1 = new Bundle();
                data1.putParcelableArrayList("List",(ArrayList<? extends Parcelable>) ret);
                int data2=position;
                Intent intent=new Intent(getActivity(),PlayMusicActivity.class);
                intent.putExtras(data1);
                intent.putExtra("extra_data2",data2);
                intent.putExtra("isonline",0);
                startActivity(intent);
            }
        });
        return content;
    }

    public void setCurAccount(String account){
        this.curAccount = account;
    }

}