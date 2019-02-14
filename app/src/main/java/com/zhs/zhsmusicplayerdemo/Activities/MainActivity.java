package com.zhs.zhsmusicplayerdemo.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.zhs.zhsmusicplayerdemo.Fragment.FragmentAdapter;
import com.zhs.zhsmusicplayerdemo.Fragment.MymusicFragment;
import com.zhs.zhsmusicplayerdemo.Fragment.PopularMusicFragment;
import com.zhs.zhsmusicplayerdemo.Fragment.UserCenterFragment;
import com.zhs.zhsmusicplayerdemo.R;
import com.zhs.zhsmusicplayerdemo.Fragment.SearchFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private ViewPager viewPager;

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();

    private FragmentAdapter mFragmentAdapter;

    private MymusicFragment mymusicFragment;
    private SearchFragment searchFragment;
    private PopularMusicFragment popularMusicFragment;
    private UserCenterFragment userCenterFragment;

    private ImageButton myMusicBtn;
    private ImageButton searchBtn;
    private ImageButton popularMusicbtn;
    private ImageButton userCenterbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myMusicBtn = (ImageButton) findViewById(R.id.mymusicbtn);
        searchBtn = (ImageButton) findViewById(R.id.searchbtn);
        popularMusicbtn = (ImageButton) findViewById(R.id.popularmusicbtn);
        userCenterbtn = (ImageButton) findViewById(R.id.usercenterbtn);

        mymusicFragment = new MymusicFragment();
        searchFragment = new SearchFragment();
        popularMusicFragment = new PopularMusicFragment();
        userCenterFragment = new UserCenterFragment();

        mFragmentList.add(mymusicFragment);
        mFragmentList.add(searchFragment);
        mFragmentList.add(popularMusicFragment);
        mFragmentList.add(userCenterFragment);

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
                popularMusicFragment.getInternetData();
            }
        });

        userCenterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
            }
        });


    }
}
