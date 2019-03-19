package com.zhs.zhsmusicplayerdemo.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.zhs.zhsmusicplayerdemo.Activities.MusicAdapter;
import com.zhs.zhsmusicplayerdemo.Activities.PlayMusicActivity;
import com.zhs.zhsmusicplayerdemo.Model.MusicDao.MusicInfo;
import com.zhs.zhsmusicplayerdemo.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import static com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES;
import static com.android.volley.DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;


public class PopularMusicFragment extends Fragment {

    private ListView listView;
    private static List<MusicInfo> ret1 = new ArrayList<>();
    private static RequestQueue queue;
    MusicAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View chatView = inflater.inflate(R.layout.fragment_popularmusic, container,false);
        listView = (ListView)chatView.findViewById(R.id.listView1);
        adapter = new MusicAdapter(this.getActivity(),R.layout.music_item,ret1,"");
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle data1 = new Bundle();
                data1.putParcelableArrayList("List",(ArrayList<? extends Parcelable>) ret1);
                int data2=position;
                Intent intent=new Intent(getActivity(),PlayMusicActivity.class);
                intent.putExtras(data1);
                intent.putExtra("extra_data2",data2);
                intent.putExtra("isonline",1);
                intent.putExtra("account","");
                startActivity(intent);
            }
        });
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
        queue = Volley.newRequestQueue(getContext());

        Loadhtml loadhtml = new Loadhtml();
        loadhtml.execute("");
    }


    class Loadhtml extends AsyncTask<String, String, String>
    {
        ProgressDialog bar;
        String url = "http://192.168.1.101:9999/musicinfo.json";
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject s) {//s为请求返回的字符串数据
                            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                            try {
                                JSONArray musics = s.getJSONArray("musics");

                                for(int i=0;i< musics.length();i++){
                                    JSONObject musicItem = musics.getJSONObject(i);
                                    String songName = musicItem.getString("songname");
                                    String singerName = musicItem.getString("singer");
                                    String path = musicItem.getString("url");
                                    String duration = musicItem.getString("time");
                                    MusicInfo musicInfo = new MusicInfo(songName,singerName,path);
                                    musicInfo.setLocal(0);
                                    musicInfo.setDuration(duration);
                                    ret1.add(musicInfo);
                                }
                                PopularMusicFragment.this.adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }finally {
                                mmr.release();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getActivity(),volleyError.toString(),Toast.LENGTH_LONG).show();
                        }
                    })
            {
                protected Response<JSONObject>  parseNetworkResponse(NetworkResponse response)
                {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(new String(response.data,"UTF-8"));
                        return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return Response.error(new ParseError(e));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return Response.error(new ParseError(e));
                    }
                }

            };
            //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
            request.setTag("testGet");
            request.setRetryPolicy(new DefaultRetryPolicy(
                    DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            //将请求加入全局队列中
            queue.add(request);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //            Log.d("doc", doc.toString().trim());
            bar.dismiss();

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


