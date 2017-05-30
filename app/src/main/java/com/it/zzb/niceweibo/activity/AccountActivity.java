package com.it.zzb.niceweibo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.api.UsersAPI;
import com.it.zzb.niceweibo.bean.User;
import com.it.zzb.niceweibo.constant.AccessTokenKeeper;
import com.it.zzb.niceweibo.constant.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.LogoutAPI;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {
    private Button logout;


    private LinearLayout add;

    private Oauth2AccessToken mAccessToken;
    private UsersAPI userApi;
    private TextView userName;//名字
    private CircleImageView icon_image;//头像
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("账号管理");
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

        logout = (Button) findViewById(R.id.btn_logout);
        icon_image = (CircleImageView) findViewById(R.id.iv_avatar);
        userName= (TextView) findViewById(R.id.tv_subhead);
        add = (LinearLayout) findViewById(R.id.add_account);
        loadData();

        //点击添加账号
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        //点击退出账号
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogoutAPI(AccountActivity.this, Constants.APP_KEY,
                        AccessTokenKeeper.readAccessToken(AccountActivity.this)).logout(mLogoutListener);
                Toast.makeText(AccountActivity.this, "已退出应用，请重新登录" ,
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void loadData() {
        // 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        // 获取用户信息接口
        userApi = new UsersAPI(this, Constants.APP_KEY, mAccessToken);
        if(mAccessToken != null && mAccessToken.isSessionValid()){
            long uid = Long.parseLong(mAccessToken.getUid());
            userApi.show(uid, mListener);
        }else {
            Toast.makeText(this, "token不存在，请重新授权", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                // 调用 User#parse 将JSON串解析成User对象
                User user = User.parse(response);

                userName.setText(user.screen_name);
                imageLoader.displayImage(user.avatar_hd,icon_image);

            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            e.printStackTrace();
        }
    };

    /**
     * 登出按钮的监听器，接收登出处理结果。（API 请求结果的监听器）
     */
    private RequestListener mLogoutListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");

                    if ("true".equalsIgnoreCase(value)) {
                        AccessTokenKeeper.clear(AccountActivity.this);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            e.printStackTrace();
        }

    };

}
