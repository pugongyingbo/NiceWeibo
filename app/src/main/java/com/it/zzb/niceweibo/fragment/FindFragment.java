package com.it.zzb.niceweibo.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.activity.LoginActivity;
import com.it.zzb.niceweibo.ui.home.HomeAdapter;
import com.it.zzb.niceweibo.api.StatusesAPI;
import com.it.zzb.niceweibo.constant.AccessTokenKeeper;
import com.it.zzb.niceweibo.constant.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.it.zzb.niceweibo.bean.StatusList;
import com.sina.weibo.sdk.openapi.legacy.SearchAPI;


public class FindFragment extends Fragment {
    private Oauth2AccessToken mAccessToken;
    private StatusesAPI mStatusApi;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SearchView mSearchView;

    public static FindFragment newInstance(String param1) {
        FindFragment fragment = new FindFragment();
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
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        mSearchView = (SearchView) view.findViewById(R.id.searchView);

        // 设置该SearchView显示搜索按钮
        mSearchView.setSubmitButtonEnabled(true);
        // 设置该SearchView内默认显示的提示文本
        mSearchView.setQueryHint("请输入搜索内容");
        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAccessToken = AccessTokenKeeper.readAccessToken(getActivity());
                SearchAPI searchAPI = new SearchAPI(getContext(),Constants.APP_KEY,mAccessToken);
                if (mAccessToken != null && mAccessToken.isSessionValid()) {
                    //热门微博需要高级授权。。。暂时不做了。
                    Toast.makeText(getActivity(), "接口限制，暂不能实现",Toast.LENGTH_LONG).show();
                   // searchAPI.statuses(query,20,mListener);

                } else {
                    Toast.makeText(getActivity(), "token不存在，请重新授权",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){

                }else{

                }
                return false;
            }
        });

       return view;
    }

    private void loadData() {
        mAccessToken = AccessTokenKeeper.readAccessToken(getActivity());
        mStatusApi = new StatusesAPI(getActivity(), Constants.APP_KEY,
                mAccessToken);

        if (mAccessToken != null && mAccessToken.isSessionValid()) {
            //热门微博需要高级授权。。。暂时不做了。
            mStatusApi.hotRepostDaily(20,false,mListener);

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
                    StatusList statuses = StatusList.parse(response);

                    if (statuses != null && statuses.total_number > 0) {

                        HomeAdapter homeAdpter = new HomeAdapter(getContext(),statuses);
              //          findList.setAdapter(homeAdpter);
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }
        }


        @Override
        public void onWeiboException(WeiboException e) {
            // TODO Auto-generated method stub
            e.printStackTrace();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };


}
