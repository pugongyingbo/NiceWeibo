package com.it.zzb.niceweibo.fragment;



import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.it.zzb.niceweibo.R;

import java.util.ArrayList;
import java.util.List;



public class MessageFragment extends Fragment {

    private ViewPager mViewPager ;

   // private TabLayout mTabLayout;
    private SlidingTabLayout mTabLayout;


    private List<BaseFragment> mFragments = new ArrayList<>();//页卡视图集合
    private String[] mTitles ={"评论","@我","@我的评论","发出的评论"};

    public static MessageFragment newInstance(String type) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString("ARG_TIMELINE_TYPE", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mTabLayout = (SlidingTabLayout) view.findViewById(R.id.tablayout);

     //  mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        //ViewPager的适配器

        initView();

       // mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    public void initView(){

        mFragments.add(CommentToMeFragment.getInstance(mTitles[0]));
        mFragments.add(MentionMeFragment.getInstance(mTitles[1]));
        mFragments.add(CommentMentionFragment.getInstance(mTitles[2]));
        mFragments.add(CommentByMeFragment.getInstance(mTitles[3]));

        CommentPagerAdapter adapter = new CommentPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        //绑定
        mTabLayout.setViewPager(mViewPager);
       // mViewPager.setCurrentItem(0);

    }
    public class CommentPagerAdapter extends FragmentPagerAdapter {

        private static final int PAGE_COUNT = 4;
        private String[] mTitles ={"评论","@我","@我的评论","发出的评论"};


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
//        switch (position) {
//            case 0:
//                return "评论";
//            case 1:
//                return "@我";
//            case 2:
//                return "@我的评论";
//            case 3:
//                return "发出的评论";
//            default:
//                return "微博";
//        }
            return mTitles[position];

        }
    }

}
