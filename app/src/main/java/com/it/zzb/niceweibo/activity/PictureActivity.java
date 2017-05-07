package com.it.zzb.niceweibo.activity;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;


import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.bean.Status;


import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

public class PictureActivity extends AppCompatActivity implements ViewPagerAdapter.OnSingleTagListener{
    private ImageDetailViewPager mViewPager;
    private ArrayList<String> mDatas;
    private int mPosition;
    private int mImgNum;
    private ViewPagerAdapter mAdapter;
    private Context mContext;
    private ImageDetailTopBar mImageDetailTopBar;
    private PhotoViewAttacher.OnPhotoTapListener mPhotoTapListener = new PhotoViewAttacher.OnPhotoTapListener() {
        @Override
        public void onPhotoTap(View view, float v, float v1) {
            finish();
        }

//        @Override
//        public void onOutsidePhotoTap() {
//            finish();
//        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_picture);


        mContext = PictureActivity.this;
        mDatas = this.getIntent().getStringArrayListExtra("imagelist_url");
        mPosition = getIntent().getIntExtra("image_position", 0);
        mImgNum = mDatas.size();
       final Status status = (Status) getIntent().getSerializableExtra("status");

        mViewPager = (ImageDetailViewPager) findViewById(R.id.viewpagerId);
        mImageDetailTopBar = (ImageDetailTopBar) findViewById(R.id.imageTopBar);
        mAdapter = new ViewPagerAdapter(mDatas, this);
        mAdapter.setOnSingleTagListener(this);
        mViewPager.setAdapter(mAdapter);
        if (mImgNum == 1) {
            mImageDetailTopBar.setPageNumVisible(View.GONE);
        } else {
            mImageDetailTopBar.setPageNum((mPosition + 1) + "/" + mImgNum);
        }
        mViewPager.setCurrentItem(mPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 每当页数发生改变时重新设定一遍当前的页数和总页数
                mImageDetailTopBar.setPageNum((position + 1) + "/" + mImgNum);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mImageDetailTopBar.setOnMoreOptionsListener(new ImageDetailTopBar.OnMoreOptionsListener() {
            @Override
            public void onClick(View view) {
                ImageOptionPopupWindow mPopupWindow = new ImageOptionPopupWindow(mDatas.get(mViewPager.getCurrentItem()), mContext);
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                } else {
                    mPopupWindow.showAtLocation(findViewById(R.id.activity_picture), Gravity.BOTTOM, 0, 0);
                }
            }
        });


    }


    @Override
    public void onTag() {
        finish();
    }
}
