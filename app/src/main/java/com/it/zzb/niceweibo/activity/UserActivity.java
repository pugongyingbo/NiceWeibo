package com.it.zzb.niceweibo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.api.StatusesAPI;
import com.it.zzb.niceweibo.api.UsersAPI;
import com.it.zzb.niceweibo.bean.Status;
import com.it.zzb.niceweibo.bean.StatusList;
import com.it.zzb.niceweibo.bean.User;
import com.it.zzb.niceweibo.constant.AccessTokenKeeper;
import com.it.zzb.niceweibo.constant.Constants;
import com.it.zzb.niceweibo.ui.profile.FollowerActivity;
import com.it.zzb.niceweibo.ui.profile.FriendsActivity;
import com.it.zzb.niceweibo.ui.profile.ProfileAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

public class UserActivity extends AppCompatActivity {

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
    private LinearLayout ll_profile_friends;
    private LinearLayout ll_profile_followers;
    private User user;
    private Status status;
    private Context mContext ;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    public Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        initView();
    }
    public void initView(){
        profile_list = (RecyclerView) findViewById(R.id.profile_weibo_list);
        profile_list.setLayoutManager(new LinearLayoutManager(this));
        icon_image = (ImageView) findViewById(R.id.icon_image);
        name = (TextView) findViewById(R.id.name);
        description = (TextView)findViewById(R.id.description);
        weibo = (TextView) findViewById(R.id.profile_weibo);
        friend = (TextView) findViewById(R.id.profile_friends);
        follower= (TextView) findViewById(R.id.profile_follower);

        mHandler=new Handler()
        {
            public void handleMessage(Message msg)
            {
                switch(msg.what)
                {
                    case 1:
                        name.setText(user.screen_name);
                        imageLoader.displayImage(user.avatar_hd,icon_image);
                        if(user.description!=null){
                        description.setText(user.description);
                        }else{
                        description.setText("暂无简介");
                        }
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
                user = status.user;
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
                Intent intent = new Intent(UserActivity.this,FriendsActivity.class);
                startActivity(intent);
            }
        });

        //点击粉丝跳到粉丝列表
        follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, FollowerActivity.class);
                startActivity(intent);
            }
        });

    }
    private void loadData() {
        // 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(this);

        mStatusApi = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);
        status = (Status) getIntent().getSerializableExtra("status");

        if (mAccessToken != null && mAccessToken.isSessionValid()) {


            String screen_name = status.user.screen_name;
            mStatusApi.userTimeline(screen_name,0,0,30,1,false,0,false,mListener);

        } else {
            Toast.makeText(this, "token不存在，请重新授权", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
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

                        ProfileAdapter profileAdpter = new ProfileAdapter(UserActivity.this,statuses);
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
