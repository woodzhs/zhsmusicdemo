package com.zhs.zhsmusicplayerdemo.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zhs.zhsmusicplayerdemo.Activities.MusicAdapter;
import com.zhs.zhsmusicplayerdemo.Model.MusicDao.MusicInfo;
import com.zhs.zhsmusicplayerdemo.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PopularMusicFragment extends Fragment {

    private ListView listView1;
    public List<MusicInfo> ret1 = new ArrayList<>();
    MusicAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View chatView = inflater.inflate(R.layout.fragment_popularmusic, container,false);
        listView1 = (ListView)chatView.findViewById(R.id.listView1);
         adapter = new MusicAdapter(this.getActivity(),R.layout.music_item,ret1);
        listView1.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return chatView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void getInternetData()
    {
        ret1.clear();
        Loadhtml loadhtml = new Loadhtml();
        loadhtml.execute("");
    }

    class Loadhtml extends AsyncTask<String, String, String>
    {
        ProgressDialog bar;
        Document doc;
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                doc = Jsoup.connect("http://music.baidu.com/tag/%E6%B5%81%E8%A1%8C").timeout(5000).get();
                Document content = Jsoup.parse(doc.toString());
                Elements ul = content.select(".th-songlist");
                Elements divs = ul.select(".info");
                for(Element div : divs)
                {
                    Elements spans =div.select("span");
                    Elements elements1 = spans.select(".name");
                    Elements elements2 = spans.select(".author");
                    String name = "";
                    String singer = "";
                    if(!elements1.isEmpty()){
                        name = elements1.get(0).text();
                    }
                    if(!elements2.isEmpty()){
                        singer = elements2.get(0).text();
                    }
                    if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(singer))
                    {
                        MusicInfo newitem=new MusicInfo(name,singer);
                        ret1.add(newitem);
                       // Log.d("music",String.format("name : %s singer: %s",spans.get(0).text(),spans.get(2).text()));
                    }

                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //            Log.d("doc", doc.toString().trim());
            bar.dismiss();
            PopularMusicFragment.this.adapter.notifyDataSetChanged();

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            bar = new ProgressDialog(getActivity());
            bar.setMessage("正在加载数据····");
            bar.setIndeterminate(false);
            bar.setCancelable(false);
            bar.show();
        }

    }
}


