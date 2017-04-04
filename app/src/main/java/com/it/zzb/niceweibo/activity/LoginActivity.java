package com.it.zzb.niceweibo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.constant.AccessTokenKeeper;
import com.it.zzb.niceweibo.constant.Constants;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

public class LoginActivity extends AppCompatActivity {
    private Button btn;
    private AuthInfo authInfo;
    private Oauth2AccessToken accessToken;
    private SsoHandler ssoHandler;
    private Intent mStartIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn= (Button) findViewById(R.id.btn_login);
        authInfo=new AuthInfo(this, Constants.APP_KEY,Constants.REDIRECT_URL,Constants.SCOPE);

        ssoHandler=new SsoHandler(this,authInfo);
        //按钮点击事件
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ssoHandler.authorize(new LoginActivity.AuthListener());
            }
        });
    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     * @see
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if ( ssoHandler!= null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            accessToken = Oauth2AccessToken.parseAccessToken(values);

            if (accessToken != null && accessToken.isSessionValid()) {
                // 可以获取各种信息
                Toast.makeText(LoginActivity.this,"授权成功", Toast.LENGTH_SHORT).show();

                AccessTokenKeeper.writeAccessToken(getApplicationContext(), accessToken);

                mStartIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mStartIntent);
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");

                Toast.makeText(LoginActivity.this, "失败", Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this,"取消授权", Toast.LENGTH_LONG).show();
        }
        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(LoginActivity.this,"exception:" +e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
