package com.it.zzb.niceweibo.ui.message;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.fragment.BaseFragment;
import com.it.zzb.niceweibo.ui.message.CommentByMeFragment;
import com.it.zzb.niceweibo.ui.message.CommentMentionFragment;
import com.it.zzb.niceweibo.ui.message.CommentToMeFragment;
import com.it.zzb.niceweibo.ui.message.MentionMeFragment;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private ViewPager mViewPager ;

    private SlidingTabLayout mTabLayout;
    private Toolbar toolbar;

    private List<BaseFragment> mFragments = new ArrayList<>();//页卡视图集合
    private String[] mTitles ={"评论","@我","@我的评论","发出的评论"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("消息");
        //设置为ActionBar
        setSupportActionBar(toolbar);
        //显示那个箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        initView();
    }
    private void initView() {

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (SlidingTabLayout) findViewById(R.id.tablayout);
        mFragments.add(CommentToMeFragment.getInstance(mTitles[0]));
        mFragments.add(MentionMeFragment.getInstance(mTitles[1]));
        mFragments.add(CommentMentionFragment.getInstance(mTitles[2]));
        mFragments.add(CommentByMeFragment.getInstance(mTitles[3]));

        CommentPagerAdapter adapter = new CommentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        //绑定
        mTabLayout.setViewPager(mViewPager);
        // mViewPager.setCurrentItem(0);
    }
    public class CommentPagerAdapter extends FragmentPagerAdapter {

        private static final int PAGE_COUNT = 4;


        public CommentPagerAdapter( FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
        //标题
        @Override
        public CharSequence getPageTitle(int position) {

            return mTitles[position];

        }
    }
}
