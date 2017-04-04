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
import android.widget.TextView;
import android.widget.Toast;

import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.activity.LoginActivity;
import com.it.zzb.niceweibo.adapter.HomeAdapter;
import com.it.zzb.niceweibo.api.StatusesAPI;
import com.it.zzb.niceweibo.constant.AccessTokenKeeper;
import com.it.zzb.niceweibo.constant.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.models.StatusList;


public class FindFragment extends Fragment {
    private RecyclerView findList;
    private Oauth2AccessToken mAccessToken;
    private StatusesAPI mStatusApi;
    private SwipeRefreshLayout mSwipeRefreshLayout;

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
//        Bundle bundle = getArguments();
//        String agrs1 = bundle.getString("agrs1");
//        TextView tv = (TextView)view.findViewById(R.id.tv_find);
//        tv.setText(agrs1);
        findList = (RecyclerView) view.findViewById(R.id.find_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        findList.setLayoutManager(new LinearLayoutManager(getContext()));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        loadData();
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
                        findList.setAdapter(homeAdpter);
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }
        }


        @Override
        public void onWeiboException(WeiboException e) {
            // TODO Auto-generated method stub

            mSwipeRefreshLayout.setRefreshing(false);
        }
    };


}
