package com.it.zzb.niceweibo.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.View;


import com.it.zzb.niceweibo.view.ViewPagerFixed;

import java.util.ArrayList;

/**
 * Created by zzb on 2017/5/24.
 */

public class MyPagerAdapter extends PagerAdapter {

    private ArrayList<View> listViews;
    private Activity activity;
    public  static Handler handler;

    private int size;
    public MyPagerAdapter(ArrayList<View> listViews, Activity activity) {
        this.listViews = listViews;
        size = listViews == null ? 0 : listViews.size();
        this.activity = activity;
    }

    public void setListViews(ArrayList<View> listViews) {
        this.listViews = listViews;
        size = listViews == null ? 0 : listViews.size();
    }

    public int getCount() {
        return size;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
    }

    public void finishUpdate(View arg0) {
    }

    /**
     * 图片长按点击保存事件
     * by黄海杰 at:2015年8月4日 08:57:16
     * @param arg0
     * @param arg1
     * @return
     */
    public Object instantiateItem(final View arg0, final int arg1) {
        try {
            ((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);
            listViews.get(arg1 % size).setDrawingCacheEnabled(true);
            listViews.get(arg1 % size).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View view) {
                    return true;
                }
            });

        } catch (Exception e) {
        }
        return listViews.get(arg1 % size);
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

}