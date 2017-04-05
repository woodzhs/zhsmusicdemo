package com.zhs.zhsmusicplayerdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private ViewPager viewPager;

    private List<Fragment> mFragmentList =new ArrayList<Fragment>();

    private FragmentAdapter mFragmentAdapter;

    ArrayList<View> viewContainter =new ArrayList<View>();

    private MymusicFragment mymusicFragment;
    private SearchFragment searchFragment;
    private PopularMusic popularMusic;

    private Button myMusicBtn;
    private Button searchBtn;
    private Button popularMusicbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myMusicBtn=(Button)findViewById(R.id.myMusic);
        searchBtn=(Button)findViewById(R.id.search);
        popularMusicbtn=(Button)findViewById(R.id.popularMusic);

        mymusicFragment=new MymusicFragment();
        searchFragment=new SearchFragment();
        popularMusic=new PopularMusic();


        mFragmentList.add(mymusicFragment);
        mFragmentList.add(searchFragment);
        mFragmentList.add(popularMusic);

        viewPager=(ViewPager)findViewById(R.id.viewpager);

        mFragmentAdapter =new FragmentAdapter(this.getSupportFragmentManager(),mFragmentList);

        viewPager.setAdapter(mFragmentAdapter);

        myMusicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        popularMusicbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
                popularMusic.getInternetData();
            }
        });


    }
}
