package com.it.zzb.niceweibo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.it.zzb.niceweibo.adapter.ViewAdapter;
import com.it.zzb.niceweibo.constant.AccessTokenKeeper;
import com.it.zzb.niceweibo.constant.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.models.CommentList;

/**
 * Created by zzb on 2017/4/3.
 */

public class CommentByMeFragment extends BaseFragment {
    private String title;

    private RecyclerView message_list;

    private Oauth2AccessToken mAccessToken;

    private CommentsAPI commentsAPI;

    public static CommentByMeFragment getInstance(String title) {
        CommentByMeFragment fragment = new CommentByMeFragment();

        fragment.title = title;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutID() {
        return R.layout.comment_by_me;
    }

    @Override
    public void initView() {

        message_list = (RecyclerView) view.findViewById(R.id.message_list);
        message_list.setLayoutManager(new LinearLayoutManager(getContext()));
        loadData();
    }

    private void loadData() {
        // 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(getActivity());
        // 获取用户信息接口
        commentsAPI = new CommentsAPI(getActivity(), Constants.APP_KEY,mAccessToken);
        if (mAccessToken != null && mAccessToken.isSessionValid()) {
            commentsAPI.byME(0,0,20,1,0,mListener);
        } else {
            Toast.makeText(getActivity(), "token不存在，请重新授权", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(final String response) {

            if (!TextUtils.isEmpty(response)) {

                if (response.startsWith("{\"comments\"")) {

                    // 调用 StatusList#parse 解析字符串成微博列表对象
                    CommentList commentList = CommentList.parse(response);

                    if (commentList != null && commentList.total_number > 0) {

                        ViewAdapter viewAdpter = new ViewAdapter(getContext(),commentList);
                        message_list.setAdapter(viewAdpter);
                    }
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            // TODO Auto-generated method stub

        }
    };

}
