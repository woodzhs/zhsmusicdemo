package com.zhs.zhsmusicplayerdemo.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.zhs.zhsmusicplayerdemo.Fragment.FragmentAdapter;
import com.zhs.zhsmusicplayerdemo.Fragment.MymusicFragment;
import com.zhs.zhsmusicplayerdemo.Fragment.PopularMusic;
import com.zhs.zhsmusicplayerdemo.R;
import com.zhs.zhsmusicplayerdemo.Fragment.SearchFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private ViewPager viewPager;

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();

    private FragmentAdapter mFragmentAdapter;

    ArrayList<View> viewContainter = new ArrayList<View>();

    private MymusicFragment mymusicFragment;
    private SearchFragment searchFragment;
    private PopularMusic popularMusic;

    private ImageButton myMusicBtn;
    private ImageButton searchBtn;
    private ImageButton popularMusicbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myMusicBtn = (ImageButton) findViewById(R.id.myMusic);
        searchBtn = (ImageButton) findViewById(R.id.search);
        popularMusicbtn = (ImageButton) findViewById(R.id.popularMusic);

        mymusicFragment = new MymusicFragment();
        searchFragment = new SearchFragment();
        popularMusic = new PopularMusic();


        mFragmentList.add(mymusicFragment);
        mFragmentList.add(searchFragment);
        mFragmentList.add(popularMusic);

        viewPager = (ViewPager)findViewById(R.id.viewpager);

        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(),mFragmentList);

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
