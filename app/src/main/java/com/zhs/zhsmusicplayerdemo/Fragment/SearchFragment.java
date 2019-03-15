package com.zhs.zhsmusicplayerdemo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.zhs.zhsmusicplayerdemo.Activities.MusicAdapter;
import com.zhs.zhsmusicplayerdemo.Activities.PlayMusicActivity;
import com.zhs.zhsmusicplayerdemo.Model.MusicDao.MusicInfo;
import com.zhs.zhsmusicplayerdemo.Model.MusicDao.MusicInfoDBManager;
import com.zhs.zhsmusicplayerdemo.R;
import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    private ListView listView;
    private ImageButton search;
    private EditText input;
    private MusicInfoDBManager dm;
    private String curAccount;
    public List<MusicInfo> ret2 = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View content = inflater.inflate(R.layout.fragment_search, container,false);
        search = (ImageButton) content.findViewById(R.id.search);
        input = (EditText)content.findViewById(R.id.input_message);
        listView = (ListView)content.findViewById(R.id.listView1);
        dm = new MusicInfoDBManager(getActivity());

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    int size = ret2.size();
                    if(size>0) {
                        ret2.clear();
                        MusicAdapter adapter = new MusicAdapter(getActivity(),R.layout.music_item,ret2,curAccount);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    showListView();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText=input.getText().toString();
                if(TextUtils.isEmpty(inputText.trim())){
                    Toast.makeText(getActivity(),"请输入搜索内容",Toast.LENGTH_SHORT).show();
                } else {
                    if(ret2.isEmpty()){
                        Toast.makeText(getActivity(),"对不起，没有你要的内容",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle data1 = new Bundle();
                data1.putParcelableArrayList("List",(ArrayList<? extends Parcelable>) ret2);
                int data2 = position;
                Intent intent = new Intent(getActivity(),PlayMusicActivity.class);
                intent.putExtras(data1);
                intent.putExtra("extra_data2",data2);
                intent.putExtra("isonline",0);
                startActivity(intent);
            }
        });
        return content;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }


    public void showListView(){
        ret2.clear();
        String inputText=input.getText().toString();
        ret2 = dm.find(inputText);
        MusicAdapter adapter = new MusicAdapter(getActivity(),R.layout.music_item,ret2,curAccount);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void setCurAccount(String account){
        this.curAccount = account;
    }

}
