package com.it.zzb.niceweibo.ui.profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.activity.LoginActivity;
import com.it.zzb.niceweibo.api.FriendshipsAPI;
import com.it.zzb.niceweibo.bean.User;
import com.it.zzb.niceweibo.constant.AccessTokenKeeper;
import com.it.zzb.niceweibo.constant.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FollowerActivity extends AppCompatActivity {
    private ListView followerList;
    private Oauth2AccessToken mAccessToken;
    private FriendshipsAPI friendshipsAPI;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        toolbar.setTitle("粉丝");

        followerList = (ListView) findViewById(R.id.follower_list);
        loadData();
    }
    private void loadData() {
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        friendshipsAPI = new FriendshipsAPI(this, Constants.APP_KEY,
                mAccessToken);
        if (mAccessToken != null && mAccessToken.isSessionValid()) {

            long uid = Long.parseLong(mAccessToken.getUid());

            friendshipsAPI.followers(uid,100,0,false,mListener);

        } else {
            Toast.makeText(this, "token不存在，请重新授权",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        }
    }


    private RequestListener mListener = new RequestListener() {

        @Override
        public void onComplete(final String response) {
            if (!TextUtils.isEmpty(response)) {
                ArrayList<User> userList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("users");
                    for(int i = 0;i<jsonArray.length();i++){
                        User user = User.parse(jsonArray.get(i).toString());
                        userList.add(user);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                FollowerAdapter adapter = new FollowerAdapter(getBaseContext(), userList);
                followerList.setAdapter(adapter);

            }
        }


        @Override
        public void onWeiboException(WeiboException e) {
            // TODO Auto-generated method stub
            e.printStackTrace();
        }
    };
}
