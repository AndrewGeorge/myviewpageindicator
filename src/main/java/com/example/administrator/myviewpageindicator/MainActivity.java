package com.example.administrator.myviewpageindicator;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.view.ViewPageIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private ViewPageIndicator mPageIndicator;
    private List<String> mTitles = Arrays.asList("短信1", "收藏2", "推荐3", "短信4", "收藏5", "推荐6", "短信7", "收藏8", "推荐8");
    private List<VpSimplaeFragment> mContents = new ArrayList<VpSimplaeFragment>();
    private FragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);

        initViews();
        initData();
        mPageIndicator.setmTabVisableCount(3);
        mPageIndicator.setTabItemTittles(mTitles);

        mViewPager.setAdapter(mAdapter);
        mPageIndicator.setViewPage(mViewPager, 0);

    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mPageIndicator = (ViewPageIndicator) findViewById(R.id.id_indlicator);
    }

    private void initData() {
        for (String title : mTitles) {
            VpSimplaeFragment vpSimplaeFragment = VpSimplaeFragment.newInstance(title);
            mContents.add(vpSimplaeFragment);

        }
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mContents.get(position);
            }

            @Override
            public int getCount() {
                return mContents.size();
            }
        };
    }
}
