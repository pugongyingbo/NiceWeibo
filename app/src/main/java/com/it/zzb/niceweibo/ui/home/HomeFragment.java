package com.it.zzb.niceweibo.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;


import com.hellosliu.easyrecyclerview.LoadMoreRecylerView;
import com.hellosliu.easyrecyclerview.listener.OnRefreshListener;
import com.it.zzb.niceweibo.activity.LoginActivity;
import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.api.StatusesAPI;
import com.it.zzb.niceweibo.constant.AccessTokenKeeper;
import com.it.zzb.niceweibo.constant.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import com.it.zzb.niceweibo.bean.StatusList;


public class HomeFragment extends Fragment {
    private View view;
   // private RecyclerView content_list;
    private Oauth2AccessToken mAccessToken;
    private StatusesAPI mStatusApi;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private HomeAdapter homeAdpter;
    private LoadMoreRecylerView content_list;
    private LayoutInflater inflate;
    private int curPage = 1;

    public HomeFragment() {
        // Required empty public constructor
    }



    public static HomeFragment newInstance(String param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        View view1 = inflate.inflate(R.layout.view_empty, null);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
       // content_list = (RecyclerView) view.findViewById(R.id.content_list);
        content_list = (LoadMoreRecylerView) view.findViewById(R.id.content_list);

        content_list.setLayoutManager(new LinearLayoutManager(getContext()));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData(1);
      //                  homeAdpter.notifyDataSetChanged();
                        // 加载完数据设置为不刷新状态，将下拉进度收起来
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1200);

            }
        });

        loadData(1);
        setRecycleView();
        return view;
    }

    private void setRecycleView(){

        //设置加载，网络异常，数据到底文字
        setSampleLoadText();
        //自定义加载，网络异常，数据到底 显示view
        setCustomerLoadFoot();

        content_list.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(curPage+1);
            }

            @Override
            public void onReload() {
                loadData(curPage+1);
            }
        });

    }

    private void setSampleLoadText(){
        //设置加载，网络异常，数据到底文字
        content_list.setSampleLoadText("Loading...", "NetWork Error", "Data End");
    }

    private void setCustomerLoadFoot(){
        TextView loadingView = new TextView(getContext());
        loadingView.setGravity(Gravity.CENTER);
        loadingView.setText("Customer Loading...");

        TextView networkErrorView = new TextView(getContext());
        networkErrorView.setGravity(Gravity.CENTER);
        networkErrorView.setText("Customer NetWork Error");

        TextView dataEndView = new TextView(getContext());
        dataEndView.setGravity(Gravity.CENTER);
        dataEndView.setText("Customer Data End");

        //自定义加载，网络异常，数据到底 显示view
        content_list.setCustomerLoadFooter(loadingView, networkErrorView, dataEndView);
    }


    private void loadData(int page) {
        mAccessToken = AccessTokenKeeper.readAccessToken(getActivity());
        mStatusApi = new StatusesAPI(getActivity(), Constants.APP_KEY,
                mAccessToken);
        if (mAccessToken != null && mAccessToken.isSessionValid()) {

            mStatusApi.friendsTimeline(0L, 0L, 20, page, false, 0, false,mListener);
        } else {
            Toast.makeText(getActivity(), "token不存在，请重新授权",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

        }
    }


    private RequestListener mListener = new RequestListener() {

        @Override
        public void onComplete(final String response) {

            if (!TextUtils.isEmpty(response)) {

                if (response.startsWith("{\"statuses\"")) {

                    mSwipeRefreshLayout.setRefreshing(false);
                    // 调用 StatusList#parse 解析字符串成微博列表对象
                    StatusList statusList = StatusList.parse(response);
                    HomeAdapter homeAdpter = new HomeAdapter(getContext(),statusList);
                    if (statusList != null && statusList.total_number > 0) {

                       content_list.setAdapter(homeAdpter);

                    }
                    homeAdpter.notifyDataSetChanged();
                    content_list.setDataEnd();
                    content_list.onRefreshComplete();
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }
        }


        @Override
        public void onWeiboException(WeiboException e) {
            // TODO Auto-generated method stub
            content_list.setNetWorkError();
            e.printStackTrace();
           // showEmptyView();
        }
    };

//    private void showEmptyView(){
//
//        content_list.showEmptyView(view1);
//    }
//
//    private void showNetWorkErrorView(){
//        View view = inflate.inflate(R.layout.view_network_error, null);
//        content_list.showEmptyView(view);
//    }



}
