package com.zhs.zhsmusicplayerdemo;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 木头 on 2016/11/7.
 */
public class PopularMusic extends Fragment {

    private ListView listView1;
    public List<MusicInfo> ret1 = new ArrayList<>();
    MusicAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View chatView = inflater.inflate(R.layout.popularmusic, container,false);
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
                Elements divs = content.select("#main");
                Elements lis = divs.get(0).select("li");
                for(Element li : lis)
                {
                    Elements spans =li.select("span");
                    if(spans.size()==3)
                    {
                        MusicInfo newitem=new MusicInfo(spans.get(0).text(),spans.get(2).text());
                        ret1.add(newitem);
                       // Log.d("music",String.format("name : %s singer: %s",spans.get(0).text(),spans.get(2).text()));
                    }

                }
//                Document divcontions = Jsoup.parse(divs.toString());
//                Elements element = divcontions.getElementsByTag("li");
//                Log.d("element", element.toString());
//                for(Element links : element)
//                {
//                    String title = links.getElementsByTag("a").text();
//
//                    String link   = links.select("a").attr("href").replace("/", "").trim();
//                    String url  = "http://music.163.com/#/discover/toplist"+link;
//                    ContentValues values = new ContentValues();
//                    values.put("Title", title);
//                    values.put("Url", url);
//
//                }

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
            PopularMusic.this.adapter.notifyDataSetChanged();

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


