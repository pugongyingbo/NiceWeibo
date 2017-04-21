package com.it.zzb.niceweibo.ui.profile;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.it.zzb.niceweibo.activity.LoginActivity;
import com.it.zzb.niceweibo.R;

import com.it.zzb.niceweibo.api.StatusesAPI;
import com.it.zzb.niceweibo.api.UsersAPI;
import com.it.zzb.niceweibo.constant.AccessTokenKeeper;
import com.it.zzb.niceweibo.constant.Constants;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import com.it.zzb.niceweibo.bean.User;
import com.it.zzb.niceweibo.bean.StatusList;

public class ProfileFragment extends Fragment {

    private RecyclerView profile_list;
    private Oauth2AccessToken mAccessToken;
    private StatusesAPI mStatusApi;
    private UsersAPI userApi;
    private ImageView icon_image;//头像
    private TextView name;//名字
    private TextView description;//介绍
    private TextView weibo;
    private TextView friend;
    private TextView follower;

    private User user;
    private Context mContext ;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    public static ProfileFragment newInstance(String param1) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public Handler mHandler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profile_list = (RecyclerView) view.findViewById(R.id.profile_weibo_list);
        profile_list.setLayoutManager(new LinearLayoutManager(getContext()));
        icon_image = (ImageView) view.findViewById(R.id.icon_image);
        name = (TextView) view.findViewById(R.id.name);
        description = (TextView) view.findViewById(R.id.description);
        weibo = (TextView) view.findViewById(R.id.profile_weibo);
        friend = (TextView) view.findViewById(R.id.profile_friends);
        follower= (TextView) view.findViewById(R.id.profile_follower);

        mHandler=new Handler()
        {
            public void handleMessage(Message msg)
            {
                switch(msg.what)
                {
                    case 1:
                        name.setText(user.screen_name);
                        imageLoader.displayImage(user.avatar_hd,icon_image);
                        description.setText(user.description);
                        weibo.setText("微博 ："+user.statuses_count);
                        friend.setText("关注 ："+user.friends_count);
                        follower.setText("粉丝 ："+user.followers_count);
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };

        loadData();
        Thread thread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                user = User.parse(userApi.showSync(Long.parseLong(mAccessToken.getUid())));

                Message message=new Message();
                message.what=1;
                mHandler.sendMessage(message);
            }
        });
        thread.start();

        //点击关注跳到关注列表
        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FriendsActivity.class);
                startActivity(intent);
            }
        });

        //点击粉丝跳到粉丝列表
       follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FollowerActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void loadData() {
        // 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(getActivity());
        // 获取用户信息接口
        userApi = new UsersAPI(getActivity(), Constants.APP_KEY, mAccessToken);

        mStatusApi = new StatusesAPI(getActivity(), Constants.APP_KEY,
                mAccessToken);


        if (mAccessToken != null && mAccessToken.isSessionValid()) {

            mStatusApi.userTimeline(0, 0, 20, 1, false, 0, false, mListener);
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

                    if (response.startsWith("{\"statuses\"")) {

                        // 调用 StatusList#parse 解析字符串成微博列表对象
                        StatusList statuses = StatusList.parse(response);
                        if (statuses != null && statuses.total_number > 0) {
                            ProfileAdapter profileAdpter = new ProfileAdapter(getContext(),statuses);
                            profile_list.setAdapter(profileAdpter);
                        }
                    }
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                // TODO Auto-generated method stub
                e.printStackTrace();
            }
        };
}
