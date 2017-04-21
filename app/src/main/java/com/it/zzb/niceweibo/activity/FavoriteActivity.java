package com.it.zzb.niceweibo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.adapter.FavoriteAdapter;
import com.it.zzb.niceweibo.api.FavoritesAPI;
import com.it.zzb.niceweibo.api.StatusesAPI;
import com.it.zzb.niceweibo.bean.FavoriteList;
import com.it.zzb.niceweibo.constant.AccessTokenKeeper;
import com.it.zzb.niceweibo.constant.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

public class FavoriteActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Oauth2AccessToken mAccessToken;
    private StatusesAPI mStatusApi;
    private FavoritesAPI favoritesAPI;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
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
        toolbar.setTitle("收藏");

        recyclerView = (RecyclerView) findViewById(R.id.favorite_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadData();
    }

    private void loadData() {
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        favoritesAPI = new FavoritesAPI(this, Constants.APP_KEY, mAccessToken);

        if (mAccessToken != null && mAccessToken.isSessionValid()) {

            favoritesAPI.favorites(30,1,mListener);

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
                    // 调用 StatusList#parse 解析字符串成微博列表对象
                FavoriteList favoriteList = FavoriteList.parse(response);
                if (favoriteList != null && favoriteList.total_number > 0) {
                    FavoriteAdapter favoriteAdapter = new FavoriteAdapter(getBaseContext(),favoriteList);
                        recyclerView.setAdapter(favoriteAdapter);
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
